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
import android.util.Log;

import java.io.IOException;
import java.net.ServerSocket;
import java.security.SecureRandom;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import fr.inria.rsommerard.fougere.Fougere;

/**
 * Created by Romain on 03/08/16.
 */
public class ConnectionHandler {

    private static final int CONNECTION_TIMEOUT = 60000;

    private final WifiP2pManager manager;
    private final Channel channel;
    private final FougereActionListener cancelConnectActionListener;
    private final FougereActionListener removeGroupActionListener;
    private final Handler handler;
    private final Runnable timeout;

    private enum ConnectionState {
        DISCONNECTED,
        CONNECTED,
        CONNECTING
    }

    private ConnectionState state;

    public ConnectionHandler(final Activity activity, final WifiP2pManager manager,
                             final Channel channel) {
        this.manager = manager;
        this.channel = channel;

        Passive passive = new Passive();
        passive.start();

        this.handler = new Handler();

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

        ConnectionReceiver connectionReceiver = new ConnectionReceiver();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        activity.registerReceiver(connectionReceiver, intentFilter);

        this.manager.cancelConnect(this.channel, this.cancelConnectActionListener);
        this.manager.removeGroup(this.channel, this.removeGroupActionListener);
    }

    public void connect(final WifiP2pDevice device) {
        Log.d(Fougere.TAG, "[ConnectionHandler] Call connect");

        if (ConnectionState.CONNECTING.equals(this.state) ||
                ConnectionState.CONNECTED.equals(this.state)) {
            Log.d(Fougere.TAG, "[ConnectionHandler] Connecting or already connected");
            return;
        }

        this.state = ConnectionState.CONNECTING;

        this.handler.postDelayed(this.timeout, CONNECTION_TIMEOUT);

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

        this.handler.removeCallbacks(this.timeout);

        this.manager.cancelConnect(this.channel, this.cancelConnectActionListener);
        this.manager.removeGroup(this.channel, this.removeGroupActionListener);

        this.state = ConnectionState.DISCONNECTED;
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

                    if ( ! wiFiP2pInfo.isGroupOwner) {
                        Active active = new Active(wiFiP2pInfo.groupOwnerAddress);
                        active.start();
                    }
                }
            }
        }
    }
}
