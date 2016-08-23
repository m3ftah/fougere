package fr.inria.rsommerard.fougere.social;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutCompat;

import java.util.ArrayList;
import java.util.List;

import fr.inria.rsommerard.fougere.FougereModule;
import fr.inria.rsommerard.fougere.data.Data;
import fr.inria.rsommerard.fougere.data.social.SocialData;
import fr.inria.rsommerard.fougere.data.social.SocialDataPool;

/**
 * Created by Romain on 15/08/2016.
 */
public class Social implements FougereModule {

    public static final String NAME = "Social";

    private final SocialDataPool socialDataPool;
    private int ratio;

    public Social(final Activity activity) {
        this.socialDataPool = new SocialDataPool(activity);
        this.ratio = 30;
    }

    @Override
    public void addData(final Data data) {
        this.socialDataPool.insert(SocialData.fromData(data));
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
        List<SocialData> socialData = this.socialDataPool.getAll();
        List<Data> data = new ArrayList<>();

        for (SocialData dt : socialData) {
            data.add(SocialData.toData(dt));
        }

        return data;
    }

    @Override
    public void removeData(final Data data) {
        this.socialDataPool.delete(SocialData.fromData(data));
    }

    @Override
    public String getName() {
        return Social.NAME;
    }
}
