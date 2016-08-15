package fr.inria.rsommerard.fougere.data.social;

import android.util.Log;

import java.util.Random;
import java.util.UUID;

import fr.inria.rsommerard.fougere.Fougere;

/**
 * Created by Romain on 15/08/2016.
 */
public class SocialDataProducer {

    public static SocialData produce() {
        String key = Integer.toString(new Random().nextInt());
        SocialData data = new SocialData(null, UUID.randomUUID().toString(), "#" + key + "#");
        Log.d(Fougere.TAG, "[SocialDataProducer] SocialData produced: " + data);
        return data;
    }
}
