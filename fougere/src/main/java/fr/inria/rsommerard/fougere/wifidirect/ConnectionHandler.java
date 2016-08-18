package fr.inria.rsommerard.fougere.wifidirect;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

import java.io.IOException;
import java.net.ServerSocket;
import java.security.SecureRandom;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import fr.inria.rsommerard.fougere.Fougere;
import fr.inria.rsommerard.fougere.data.DataPool;
import fr.inria.rsommerard.fougere.data.wifidirect.WiFiDirectData;
import fr.inria.rsommerard.fougere.data.wifidirect.WiFiDirectDataPool;

/**
 * Created by Romain on 03/08/16.
 */
public class ConnectionHandler {

    private static final int CONNECTION_TIMEOUT = 60000;
    private static final int DELAY = 3000;

    private final WifiP2pManager manager;
    private final Channel channel;
    private final FougereActionListener cancelConnectActionListener;
    private final FougereActionListener removeGroupActionListener;
    private final Handler handler;
    private final Runnable timeout;
    private final Runnable passive;
    private final WiFiDirectDataPool wiFiDirectDataPool;
    private final DataPool dataPool;
    private ScheduledExecutorService executor;
    private final Context activity;
    private final IntentFilter intentFilter;
    private final ConnectionReceiver connectionReceiver;
    private Runnable active;

    private enum ConnectionState {
        DISCONNECTED,
        CONNECTED,
        CONNECTING
    }

    private ConnectionState state;

    public ConnectionHandler(final Activity activity, final WifiP2pManager manager,
                             final Channel channel, final DataPool dataPool,
                             final WiFiDirectDataPool wiFiDirectDataPool) {
        this.manager = manager;
        this.channel = channel;

        this.activity = activity;

        this.dataPool = dataPool;
        this.wiFiDirectDataPool = wiFiDirectDataPool;

        this.passive = new Passive(this.dataPool, this.wiFiDirectDataPool);

        HandlerThread handlerThread = new HandlerThread("ConnectionHandlerThread");
        handlerThread.start();
        this.handler = new Handler(handlerThread.getLooper());

        this.timeout = new Runnable() {
            @Override
            public void run() {
                Log.d(Fougere.TAG, "[ConnectionHandler] TIMEOUT");
                ConnectionHandler.this.disconnect();
            }
        };

        this.state = ConnectionState.DISCONNECTED;

        this.cancelConnectActionListener =
                new FougereActionListener("Cancel connect succeeded", "Cancel connect failed: ");
        this.removeGroupActionListener =
                new FougereActionListener("Remove group succeeded", "Remove group failed: ");

        this.connectionReceiver = new ConnectionReceiver();

        this.intentFilter = new IntentFilter();
        this.intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
    }

    public void connect(final WifiP2pDevice device) {
        Log.d(Fougere.TAG, "[ConnectionHandler] Call connect");

        if (ConnectionState.CONNECTING.equals(this.state) ||
                ConnectionState.CONNECTED.equals(this.state)) {
            Log.d(Fougere.TAG, "[ConnectionHandler] Connecting or already connected");
            return;
        }

        this.state = ConnectionState.CONNECTING;

        this.executor.schedule(this.timeout, CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS);

        WifiP2pConfig wifiP2pConfig = new WifiP2pConfig();
        wifiP2pConfig.deviceAddress = device.deviceAddress;
        wifiP2pConfig.wps.setup = WpsInfo.PBC;
        wifiP2pConfig.groupOwnerIntent = 15;

        this.manager.connect(this.channel, wifiP2pConfig,
                new FougereActionListener("Connect call succeeded", "Connect call failed: ") {

                    @Override
                    public void onFailure(int reason) {
                        super.onFailure(reason);

                        ConnectionHandler.this.disconnect();
                    }
                });
    }

    private void disconnect() {
        Log.d(Fougere.TAG, "[ConnectionHandler] Call disconnect");

        this.handler.removeCallbacks(this.passive);
        this.handler.removeCallbacks(this.active);

        this.manager.cancelConnect(this.channel, this.cancelConnectActionListener);
        this.manager.removeGroup(this.channel, this.removeGroupActionListener);

        this.state = ConnectionState.DISCONNECTED;
    }

    public void start() {
        this.manager.cancelConnect(this.channel, this.cancelConnectActionListener);
        this.manager.removeGroup(this.channel, this.removeGroupActionListener);

        this.activity.registerReceiver(this.connectionReceiver, this.intentFilter);

        this.executor = Executors.newSingleThreadScheduledExecutor();
    }

    public void stop() {
        this.disconnect();

        if (this.executor != null) {
            this.executor.shutdown();
        }

        this.activity.unregisterReceiver(this.connectionReceiver);
    }

    private class ConnectionReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(final Context context, final Intent intent) {
            String action = intent.getAction();

            if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {
                NetworkInfo networkInfo = intent.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);
                WifiP2pInfo wiFiP2pInfo = intent.getParcelableExtra(WifiP2pManager.EXTRA_WIFI_P2P_INFO);

                Log.d(Fougere.TAG, "[ConnectionHandler] " + networkInfo);
                Log.d(Fougere.TAG, "[ConnectionHandler] " + wiFiP2pInfo);

                if (networkInfo.isConnected()) {
                    Log.d(Fougere.TAG, "[ConnectionHandler] Devices connected");

                    if (wiFiP2pInfo.isGroupOwner) {
                        ConnectionHandler.this.handler.post(ConnectionHandler.this.passive);
                    } else {
                        ConnectionHandler.this.active = new Active(wiFiP2pInfo.groupOwnerAddress,
                                ConnectionHandler.this.dataPool,
                                ConnectionHandler.this.wiFiDirectDataPool);
                        ConnectionHandler.this.handler.postDelayed(ConnectionHandler.this.active,
                                DELAY);
                    }
                } /*else {
                    if (NetworkInfo.DetailedState.FAILED.equals(networkInfo.getDetailedState())) {
                        ConnectionHandler.this.disconnect();
                    }
                }*/
            }
        }
    }
}
