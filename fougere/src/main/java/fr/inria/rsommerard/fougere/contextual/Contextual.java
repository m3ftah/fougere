package fr.inria.rsommerard.fougere.contextual;

import android.app.Activity;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import fr.inria.rsommerard.fougere.Fougere;
import fr.inria.rsommerard.fougere.FougereDistance;
import fr.inria.rsommerard.fougere.FougereModule;
import fr.inria.rsommerard.fougere.data.Data;
import fr.inria.rsommerard.fougere.data.contextual.ContextualData;
import fr.inria.rsommerard.fougere.data.contextual.ContextualDataPool;
import retrofit2.Retrofit;

/**
 * Created by Romain on 15/08/2016.
 */
public class Contextual implements FougereModule {

    public static final String NAME = "Contextual";

    private static final String SERVER = "http://10.32.0.45:8080/";

    private final ContextualDataPool contextualDataPool;
    private final FougereDistance fougereDistance;
    private final ContextualAPI api;
    private int ratio;

    public Contextual(final Activity activity, final FougereDistance fougereDistance) {
        this.contextualDataPool = new ContextualDataPool(activity);
        this.ratio = 100;
        this.fougereDistance = fougereDistance;

        this.api = new Retrofit.Builder().baseUrl(SERVER).build().create(ContextualAPI.class);
    }

    @Override
    public void addData(final Data data) {
        this.contextualDataPool.insert(ContextualData.fromData(data));
    }

    @Override
    public void start() {
        List<ContextualUser> users;

        try {
            users = this.api.listUsers().execute().body();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(Fougere.TAG, "[Contextual] Cannot get the contextual user list");
            return;
        }

        Log.d(Fougere.TAG, "[Contextual] Users: " + users.toString());
    }

    public void disseminate() {
        // send/recover data to/from server
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
