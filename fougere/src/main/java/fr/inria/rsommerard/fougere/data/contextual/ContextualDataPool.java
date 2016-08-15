package fr.inria.rsommerard.fougere.data.contextual;

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
        if (data.getIdentifier() == null || data.getContent() == null) {
            return;
        }

        ContextualData found = this.contextualDataDao.queryBuilder()
                .where(ContextualDataDao.Properties.Identifier.eq(data.getIdentifier()))
                .unique();

        if (found != null) {
            Log.d(Fougere.TAG, "[ContextualDataPool] A data with the same identifier already exists");
            return;
        }

        this.contextualDataDao.insert(data);
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

        this.contextualDataDao.delete(found);
    }

    public List<ContextualData> getAll() {
        return this.contextualDataDao.loadAll();
    }
}