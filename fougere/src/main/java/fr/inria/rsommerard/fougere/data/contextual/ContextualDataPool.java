package fr.inria.rsommerard.fougere.data.contextual;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.List;

import fr.inria.rsommerard.fougere.Fougere;
import fr.inria.rsommerard.fougere.data.ContextualDataDao;
import fr.inria.rsommerard.fougere.data.DaoMaster;
import fr.inria.rsommerard.fougere.data.DaoSession;

/**
 * Created by Romain on 14/08/2016.
 */
public class ContextualDataPool {

    private final ContextualDataDao contextualDataDao;

    public ContextualDataPool(final Context context) {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, "fougere-db", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession daoSession = daoMaster.newSession();
        this.contextualDataDao = daoSession.getContextualDataDao();

        // TODO: to remove
        this.contextualDataDao.deleteAll();
    }

    public void insert(final ContextualData data) {
        if (data.getIdentifier() == null || data.getContent() == null || data.getTtl() < 0 ||
                data.getDisseminate() < 0 || data.getSent() < 0) {
            return;
        }

        ContextualData found = this.contextualDataDao.queryBuilder()
                .where(ContextualDataDao.Properties.Identifier.eq(data.getIdentifier()))
                .unique();

        if (found != null) {
            Log.d(Fougere.TAG,
                    "[ContextualDataPool] A data with the same identifier already exists");
            return;
        }

        Log.d(Fougere.TAG, "[ContextualDataPool] Insert: " + data.toString());

        this.contextualDataDao.insert(data);
    }

    public void update(final ContextualData data) {
        if (data.getIdentifier() == null || data.getContent() == null || data.getTtl() < 0 ||
                data.getDisseminate() < 0 || data.getSent() < 0) {
            return;
        }

        ContextualData found = this.contextualDataDao.queryBuilder()
                .where(ContextualDataDao.Properties.Identifier.eq(data.getIdentifier()))
                .unique();

        if (found == null) {
            Log.d(Fougere.TAG, "[ContextualDataPool] The data does not found");
            return;
        }

        Log.d(Fougere.TAG, "[ContextualDataPool] Update: " + found.toString());
        Log.d(Fougere.TAG, "[ContextualDataPool] To: " + data.toString());

        this.contextualDataDao.update(data);
    }

    public void delete(final ContextualData data) {
        if (data.getIdentifier() == null) {
            return;
        }

        ContextualData found = this.contextualDataDao.queryBuilder()
                .where(ContextualDataDao.Properties.Identifier.eq(data.getIdentifier()))
                .unique();

        if (found == null) {
            Log.d(Fougere.TAG, "[ContextualDataPool] The data does not found");
            return;
        }

        Log.d(Fougere.TAG, "[ContextualDataPool] Delete: " + found.toString());

        this.contextualDataDao.delete(found);
    }

    public List<ContextualData> getAll() {
        return this.contextualDataDao.loadAll();
    }
}