package fr.inria.rsommerard.fougere.data;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Romain on 10/08/16.
 */
public abstract class DataPool {

    private final Map<String, Data> pool;

    public DataPool() {
        this.pool = new HashMap<>();
    }

    public void addData(final Data data) {
        this.pool.put(data.identifier, data);
    }

    public void clear() {
        this.pool.clear();
    }
}
