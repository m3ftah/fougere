package fr.inria.rsommerard.fougere.data.social;

import android.util.Log;

import java.util.Random;
import java.util.UUID;

import fr.inria.rsommerard.fougere.Fougere;
import fr.inria.rsommerard.fougere.data.Data;
import fr.inria.rsommerard.fougere.data.DataProducer;

/**
 * Created by Romain on 15/08/2016.
 */
public class SocialDataProducer {

    public static SocialData produce() {
        Data dt = DataProducer.produce();

        SocialData data = new SocialData(null, dt.getIdentifier(), dt.getContent(),
                dt.getTtl(), dt.getDisseminate(), dt.getSent());

        Log.d(Fougere.TAG, "[SocialDataProducer] SocialData produced: " + data);

        return data;
    }
}
