package fr.inria.rsommerard.fougere.data.social;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.List;

import fr.inria.rsommerard.fougere.Fougere;
import fr.inria.rsommerard.fougere.data.global.DaoMaster;
import fr.inria.rsommerard.fougere.data.global.DaoSession;

/**
 * Created by Romain on 14/08/2016.
 */
public class SocialDataPool {

    private final SocialDataDao socialDataDao;

    public SocialDataPool(final Context context) {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, "fougere-db", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession daoSession = daoMaster.newSession();
        this.socialDataDao = daoSession.getSocialDataDao();

        // TODO: to remove
        this.socialDataDao.deleteAll();
    }

    public void insert(final SocialData data) {
        if (data.getIdentifier() == null || data.getContent() == null) {
            return;
        }

        SocialData found = this.socialDataDao.queryBuilder()
                .where(SocialDataDao.Properties.Identifier.eq(data.getIdentifier()))
                .unique();

        if (found != null) {
            Log.d(Fougere.TAG, "[SocialDataPool] A data with the same identifier already exists");
            return;
        }

        this.socialDataDao.insert(data);
    }

    public void delete(final SocialData data) {
        if (data.getIdentifier() == null) {
            return;
        }

        SocialData found = this.socialDataDao.queryBuilder()
                .where(SocialDataDao.Properties.Identifier.eq(data.getIdentifier()))
                .unique();

        if (found == null) {
            Log.d(Fougere.TAG, "[SocialDataPool] The data does not found");
            return;
        }

        this.socialDataDao.delete(found);
    }

    public List<SocialData> getAll() {
        return this.socialDataDao.loadAll();
    }
}