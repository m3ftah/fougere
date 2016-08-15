package fr.inria.rsommerard.fougere.contextual;

import android.app.Activity;

import java.util.List;

import fr.inria.rsommerard.fougere.data.contextual.ContextualData;
import fr.inria.rsommerard.fougere.data.contextual.ContextualDataPool;

/**
 * Created by Romain on 15/08/2016.
 */
public class Contextual {

    private final ContextualDataPool contextualDataPool;

    public Contextual(final Activity activity) {
        this.contextualDataPool = new ContextualDataPool(activity);
    }

    public void addData(final ContextualData data) {
        this.contextualDataPool.insert(data);
    }

    public List<ContextualData> getAllData() {
        return this.contextualDataPool.getAll();
    }

    public void removeData(final ContextualData data) {
        this.contextualDataPool.delete(data);
    }
}
