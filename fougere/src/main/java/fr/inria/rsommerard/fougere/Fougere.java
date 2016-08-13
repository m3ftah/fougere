package fr.inria.rsommerard.fougere;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;

import fr.inria.rsommerard.fougere.data.Data;
import fr.inria.rsommerard.fougere.wifidirect.WiFiDirect;

/**
 * Created by Romain on 01/08/16.
 */
public class Fougere {

    public static final String TAG = "Fougere";

    private final WiFiDirect wiFiDirect;

    public Fougere(final Activity activity) {
        this.wiFiDirect = new WiFiDirect(activity);

        WiFiReceiver wiFiReceiver = new WiFiReceiver();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION);
        activity.registerReceiver(wiFiReceiver, intentFilter);

        WifiManager manager = (WifiManager) activity.getSystemService(Context.WIFI_SERVICE);
        if (manager.isWifiEnabled()) {
            this.wiFiDirect.start();
        }
    }

    /*public void disseminate(final Data data) {
        // TODO: Calculate distance

        if (this.wiFiDirect.canSendData()) {
            this.wiFiDirect.sendData(new Data("Réfléchir, c'est fléchir deux fois."));
        }
    }*/

    private class WiFiReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(final Context context, final Intent intent) {
            final String action = intent.getAction();

            if (action.equals(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION)) {
                if (intent.getBooleanExtra(WifiManager.EXTRA_SUPPLICANT_CONNECTED, false)){
                    Fougere.this.wiFiDirect.start();
                } else {
                    Fougere.this.wiFiDirect.stop();
                }
            }
        }
    }
}
