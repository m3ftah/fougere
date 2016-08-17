package fr.inria.rsommerard.fougere.data.contextual;

import android.util.Log;

import java.util.Random;
import java.util.UUID;

import fr.inria.rsommerard.fougere.Fougere;
import fr.inria.rsommerard.fougere.data.Data;
import fr.inria.rsommerard.fougere.data.DataProducer;

/**
 * Created by Romain on 15/08/2016.
 */
public class ContextualDataProducer {

    public static ContextualData produce() {
        Data dt = DataProducer.produce();

        ContextualData data = new ContextualData(null, dt.getIdentifier(), dt.getContent(),
                dt.getTtl(), dt.getDisseminate(), dt.getSent());

        Log.d(Fougere.TAG, "[ContextualDataProducer] ContextualData produced: " + data);

        return data;
    }
}
