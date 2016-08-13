package fr.inria.rsommerard.fougere.wifidirect;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.os.Handler;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import fr.inria.rsommerard.fougere.Fougere;

/**
 * Created by Romain on 01/08/16.
 */
public class WiFiDirect {

    private final WifiP2pManager manager;
    private final Channel channel;

    private final ServiceDiscovery serviceDiscovery;
    private final ConnectionHandler connectionHandler;

    public WiFiDirect(final Activity activity) {
        this.manager = (WifiP2pManager) activity.getSystemService(Context.WIFI_P2P_SERVICE);
        this.channel = this.manager.initialize(activity, activity.getMainLooper(),
                new FougereChannelListener());

        this.connectionHandler = new ConnectionHandler(activity, this.manager, this.channel);

        this.serviceDiscovery = new ServiceDiscovery(this.manager, this.channel,
                this.connectionHandler);
    }

    public void start() {
        this.cleanAllGroupsRegistered();

        this.connectionHandler.start();

        this.serviceDiscovery.start();
    }

    public void stop() {
        this.serviceDiscovery.stop();

        this.connectionHandler.stop();
    }

    private void cleanAllGroupsRegistered() {
        try {
            Method deletePersistentGroupMethod =
                    WifiP2pManager.class.getMethod("deletePersistentGroup",
                            WifiP2pManager.Channel.class,
                            int.class,
                            WifiP2pManager.ActionListener.class);

            for (int netid = 0; netid < 32; netid++) {
                deletePersistentGroupMethod.invoke(this.manager, this.channel, netid, null);
            }

            Log.d(Fougere.TAG, "[WiFiDirect] Groups are successfully removed");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            Log.e(Fougere.TAG, "[WiFiDirect] Failed to deletePersistentGroup: method NOT found");
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            Log.e(Fougere.TAG, "[WiFiDirect] Failed to deletePersistentGroup");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            Log.e(Fougere.TAG, "[WiFiDirect] Failed to deletePersistentGroup");
        }
    }

    private class FougereChannelListener implements WifiP2pManager.ChannelListener {

        @Override
        public void onChannelDisconnected() {
            Log.d(Fougere.TAG, "[FougereChannelListener] Channel disconnected");
        }
    }
}
