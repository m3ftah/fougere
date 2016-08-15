package fr.inria.rsommerard.fougere.data.global;

import android.util.Log;

import java.util.Random;
import java.util.UUID;

import fr.inria.rsommerard.fougere.Fougere;

/**
 * Created by Romain on 10/08/16.
 */
public class GlobalDataProducer {

    public static GlobalData produce() {
        String key = Integer.toString(new Random().nextInt());
        GlobalData data = new GlobalData(null, UUID.randomUUID().toString(), "#" + key + "#");
        Log.d(Fougere.TAG, "[GlobalDataProducer] GlobalData produced: " + data);
        return data;
    }
}
