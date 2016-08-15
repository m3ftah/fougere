package fr.inria.rsommerard.fougere.data.global;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.List;

import fr.inria.rsommerard.fougere.Fougere;
import fr.inria.rsommerard.fougere.data.wifidirect.WiFiDirectData;

/**
 * Created by Romain on 14/08/2016.
 */
public class GlobalDataPool {

    private final GlobalDataDao globalDataDao;

    public GlobalDataPool(final Context context) {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, "fougere-db", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession daoSession = daoMaster.newSession();
        this.globalDataDao = daoSession.getGlobalDataDao();

        // TODO: to remove
        this.globalDataDao.deleteAll();
    }

    public void insert(final GlobalData data) {
        if (data.getIdentifier() == null || data.getContent() == null) {
            return;
        }

        GlobalData found = this.globalDataDao.queryBuilder()
                .where(GlobalDataDao.Properties.Identifier.eq(data.getIdentifier()))
                .unique();

        if (found != null) {
            Log.d(Fougere.TAG, "[GlobalDataPool] A data with the same identifier already exists");
            return;
        }

        this.globalDataDao.insert(data);
    }

    public void delete(final GlobalData data) {
        if (data.getIdentifier() == null) {
            return;
        }

        GlobalData found = this.globalDataDao.queryBuilder()
                .where(GlobalDataDao.Properties.Identifier.eq(data.getIdentifier()))
                .unique();

        if (found == null) {
            Log.d(Fougere.TAG, "[GlobalDataPool] The data does not found");
            return;
        }

        this.globalDataDao.delete(found);
    }

    public List<GlobalData> getAll() {
        return this.globalDataDao.loadAll();
    }
}
