package fr.inria.rsommerard.fougere.contextual;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

import fr.inria.rsommerard.fougere.FougereModule;
import fr.inria.rsommerard.fougere.data.Data;
import fr.inria.rsommerard.fougere.data.contextual.ContextualData;
import fr.inria.rsommerard.fougere.data.contextual.ContextualDataPool;

/**
 * Created by Romain on 15/08/2016.
 */
public class Contextual implements FougereModule {

    public static final String NAME = "Contextual";

    private final ContextualDataPool contextualDataPool;
    private int ratio;

    public Contextual(final Activity activity) {
        this.contextualDataPool = new ContextualDataPool(activity);
        this.ratio = 10;
    }

    @Override
    public void addData(final Data data) {
        this.contextualDataPool.insert(ContextualData.fromData(data));
    }

    @Override
    public void start() {
        // Nothing
    }

    @Override
    public void stop() {
        // Nothing
    }

    @Override
    public int getRatio() {
        return this.ratio;
    }

    @Override
    public void setRatio(int ratio) {
        this.ratio = ratio;
    }

    @Override
    public List<Data> getAllData() {
        List<ContextualData> contextualData = this.contextualDataPool.getAll();
        List<Data> data = new ArrayList<>();

        for (ContextualData dt : contextualData) {
            data.add(ContextualData.toData(dt));
        }

        return data;
    }

    @Override
    public void removeData(final Data data) {
        this.contextualDataPool.delete(ContextualData.fromData(data));
    }

    @Override
    public String getName() {
        return Contextual.NAME;
    }
}
