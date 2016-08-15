package fr.inria.rsommerard.fougere.data.contextual;

import android.util.Log;

import java.util.Random;
import java.util.UUID;

import fr.inria.rsommerard.fougere.Fougere;

/**
 * Created by Romain on 15/08/2016.
 */
public class ContextualDataProducer {

    public static ContextualData produce() {
        String key = Integer.toString(new Random().nextInt());
        ContextualData data = new ContextualData(null, UUID.randomUUID().toString(), "#" + key + "#");
        Log.d(Fougere.TAG, "[ContextualDataProducer] ContextualData produced: " + data);
        return data;
    }
}
