package fr.inria.rsommerard.fougere;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.util.List;

import fr.inria.rsommerard.fougere.contextual.Contextual;
import fr.inria.rsommerard.fougere.data.contextual.ContextualData;
import fr.inria.rsommerard.fougere.data.global.GlobalData;
import fr.inria.rsommerard.fougere.data.global.GlobalDataPool;
import fr.inria.rsommerard.fougere.data.social.SocialData;
import fr.inria.rsommerard.fougere.data.wifidirect.WiFiDirectData;
import fr.inria.rsommerard.fougere.social.Social;
import fr.inria.rsommerard.fougere.wifidirect.WiFiDirect;

/**
 * Created by Romain on 01/08/16.
 */
public class Fougere {

    public static final String TAG = "Fougere";

    private final WiFiDirect wiFiDirect;
    private final GlobalDataPool globalDataPool;
    private final Contextual contextual;
    private final Social social;

    public Fougere(final Activity activity) {
        this.globalDataPool = new GlobalDataPool(activity);

        this.wiFiDirect = new WiFiDirect(activity);
        this.contextual = new Contextual(activity);
        this.social = new Social(activity);

        this.recoverData();

        this.allocateData();

        /*WiFiReceiver wiFiReceiver = new WiFiReceiver();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION);
        activity.registerReceiver(wiFiReceiver, intentFilter);

        WifiManager manager = (WifiManager) activity.getSystemService(Context.WIFI_SERVICE);
        if (manager.isWifiEnabled()) {
            this.wiFiDirect.start();
        }*/
    }

    private void allocateData() {

    }

    private void recoverData() {
        Log.d(Fougere.TAG, "[Fougere] " + this.globalDataPool.getAll().size() +
                " data in the GlobalDataPool");

        List<WiFiDirectData> wiFiDirectData = this.wiFiDirect.getAllData();
        Log.d(Fougere.TAG, "[Fougere] Recover " + wiFiDirectData.size() +
                " data from the WiFiDirectDataPool");
        for (WiFiDirectData data : wiFiDirectData) {
            this.globalDataPool.insert(new GlobalData(null, data.getIdentifier(),
                    data.getContent()));
            this.wiFiDirect.removeData(data);
        }

        List<ContextualData> contextualData = this.contextual.getAllData();
        Log.d(Fougere.TAG, "[Fougere] Recover " + contextualData.size() +
                " data from the ContextualDataPool");
        for (ContextualData data : contextualData) {
            this.globalDataPool.insert(new GlobalData(null, data.getIdentifier(),
                    data.getContent()));
            this.contextual.removeData(data);
        }

        List<SocialData> socialData = this.social.getAllData();
        Log.d(Fougere.TAG, "[Fougere] Recover " + socialData.size() +
                " data from the SocialDataPool");
        for (SocialData data: socialData) {
            this.globalDataPool.insert(new GlobalData(null, data.getIdentifier(),
                    data.getContent()));
            this.social.removeData(data);
        }
    }

    public void addData(final GlobalData data) {
        this.globalDataPool.insert(data);
    }

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
