package fr.inria.rsommerard.fougere.data.wifidirect;

import android.util.Log;

import java.util.Random;
import java.util.UUID;

import fr.inria.rsommerard.fougere.Fougere;

/**
 * Created by Romain on 14/08/2016.
 */
public class WiFiDirectDataProducer {

    public static WiFiDirectData produce() {
        String key = Integer.toString(new Random().nextInt());
        WiFiDirectData data = new WiFiDirectData(null, UUID.randomUUID().toString(), "#" + key + "#");
        Log.d(Fougere.TAG, "[WiFiDirectDataProducer] WiFiDirectData produced: " + data);
        return data;
    }
}
