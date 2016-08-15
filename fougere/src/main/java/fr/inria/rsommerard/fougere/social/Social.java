package fr.inria.rsommerard.fougere.social;

import android.app.Activity;

import java.util.List;

import fr.inria.rsommerard.fougere.data.social.SocialData;
import fr.inria.rsommerard.fougere.data.social.SocialDataPool;

/**
 * Created by Romain on 15/08/2016.
 */
public class Social {

    private final SocialDataPool socialDataPool;

    public Social(final Activity activity) {
        this.socialDataPool = new SocialDataPool(activity);
    }

    public void addData(final SocialData data) {
        this.socialDataPool.insert(data);
    }

    public List<SocialData> getAllData() {
        return this.socialDataPool.getAll();
    }

    public void removeData(final SocialData data) {
        this.socialDataPool.delete(data);
    }
}
