package fr.inria.rsommerard.fougere.data;

import java.util.UUID;

/**
 * Created by Romain on 10/08/16.
 */
public class DataProducer {

    public Data produce() {
        return new Data(UUID.randomUUID().toString(), "This is a data.");
    }
}
