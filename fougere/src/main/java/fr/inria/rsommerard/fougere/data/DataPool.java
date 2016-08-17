package fr.inria.rsommerard.fougere.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.List;

import fr.inria.rsommerard.fougere.Fougere;
import fr.inria.rsommerard.fougere.data.contextual.DaoMaster;
import fr.inria.rsommerard.fougere.data.contextual.DaoSession;

/**
 * Created by Romain on 17/08/16.
 */
public class DataPool {

    private final DataDao dataDao;

    public DataPool(final Context context) {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, "fougere-db", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession daoSession = daoMaster.newSession();
        this.dataDao = daoSession.getDataDao();

        // TODO: to remove
        this.dataDao.deleteAll();
    }

    public void insert(final Data data) {
        if (data.getIdentifier() == null || data.getContent() == null || data.getTtl() < 0 ||
                data.getDisseminate() < 0 || data.getSent() < 0) {
            return;
        }

        Data found = this.dataDao.queryBuilder()
                .where(DataDao.Properties.Identifier.eq(data.getIdentifier()))
                .unique();

        if (found != null) {
            Log.d(Fougere.TAG, "[DataPool] A data with the same identifier already exists");
            return;
        }

        this.dataDao.insert(data);
    }

    public void update(final Data data) {
        if (data.getIdentifier() == null || data.getContent() == null || data.getTtl() < 0 ||
                data.getDisseminate() < 0 || data.getSent() < 0) {
            return;
        }

        Data found = this.dataDao.queryBuilder()
                .where(DataDao.Properties.Identifier.eq(data.getIdentifier()))
                .unique();

        if (found == null) {
            Log.d(Fougere.TAG, "[DataPool] The data does not found");
            return;
        }

        this.dataDao.update(data);
    }

    public void delete(final Data data) {
        if (data.getIdentifier() == null) {
            return;
        }

        Data found = this.dataDao.queryBuilder()
                .where(DataDao.Properties.Identifier.eq(data.getIdentifier()))
                .unique();

        if (found == null) {
            Log.d(Fougere.TAG, "[DataPool] The data does not found");
            return;
        }

        this.dataDao.delete(found);
    }

    public List<Data> getAll() {
        return this.dataDao.loadAll();
    }
}
