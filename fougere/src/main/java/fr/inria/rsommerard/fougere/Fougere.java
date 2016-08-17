package fr.inria.rsommerard.fougere;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.security.SecureRandom;
import java.util.List;

import fr.inria.rsommerard.fougere.contextual.Contextual;
import fr.inria.rsommerard.fougere.data.Data;
import fr.inria.rsommerard.fougere.data.DataPool;
import fr.inria.rsommerard.fougere.data.contextual.ContextualData;
import fr.inria.rsommerard.fougere.data.social.SocialData;
import fr.inria.rsommerard.fougere.data.wifidirect.WiFiDirectData;
import fr.inria.rsommerard.fougere.social.Social;
import fr.inria.rsommerard.fougere.wifidirect.WiFiDirect;

/**
 * Created by Romain on 01/08/16.
 */
public class Fougere {

    public static final String TAG = "Fougere";

    public static final int WIFIDIRECT_ALLOCATION = 60;
    public static final int SOCIAL_ALLOCATION = 90;
    public static final int CONTEXTUAL_ALLOCATION = 100;

    private final WiFiDirect wiFiDirect;
    private final DataPool dataPool;
    private final Contextual contextual;
    private final Social social;
    private final SecureRandom random;

    public Fougere(final Activity activity) {
        this.dataPool = new DataPool(activity);

        this.random = new SecureRandom();

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
        List<Data> data = this.dataPool.getAll();
        int nb = data.size();

        if (nb < 3) {
            for (Data dt : data) {
                this.wiFiDirect.addData(WiFiDirectData.fromData(dt));
            }
        }

        for (Data dt : data) {
            int rnd = this.random.nextInt(100);

            if (rnd < WIFIDIRECT_ALLOCATION) {
                this.wiFiDirect.addData(WiFiDirectData.fromData(dt));
            } else if (rnd < SOCIAL_ALLOCATION) {
                this.social.addData(SocialData.fromData(dt));
            } else if (rnd < CONTEXTUAL_ALLOCATION) {
                this.contextual.addData(ContextualData.fromData(dt));
            }
        }
    }

    private void recoverData() {
        Log.d(Fougere.TAG, "[Fougere] " + this.dataPool.getAll().size() +
                " data in the DataPool");

        List<WiFiDirectData> wiFiDirectData = this.wiFiDirect.getAllData();
        Log.d(Fougere.TAG, "[Fougere] Recover " + wiFiDirectData.size() +
                " data from the WiFiDirectDataPool");
        for (WiFiDirectData data : wiFiDirectData) {
            this.dataPool.insert(WiFiDirectData.toData(data));
            this.wiFiDirect.removeData(data);
        }

        List<ContextualData> contextualData = this.contextual.getAllData();
        Log.d(Fougere.TAG, "[Fougere] Recover " + contextualData.size() +
                " data from the ContextualDataPool");
        for (ContextualData data : contextualData) {
            this.dataPool.insert(ContextualData.toData(data));
            this.contextual.removeData(data);
        }

        List<SocialData> socialData = this.social.getAllData();
        Log.d(Fougere.TAG, "[Fougere] Recover " + socialData.size() +
                " data from the SocialDataPool");
        for (SocialData data: socialData) {
            this.dataPool.insert(SocialData.toData(data));
            this.social.removeData(data);
        }
    }

    public void addData(final Data data) {
        this.dataPool.insert(data);
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
