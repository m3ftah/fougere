package fr.inria.rsommerard.fougere.social;

import android.app.Activity;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import fr.inria.rsommerard.fougere.Fougere;
import fr.inria.rsommerard.fougere.FougereDistance;
import fr.inria.rsommerard.fougere.FougereModule;
import fr.inria.rsommerard.fougere.data.Data;
import fr.inria.rsommerard.fougere.data.social.SocialData;
import fr.inria.rsommerard.fougere.data.social.SocialDataPool;
import retrofit2.Call;
import retrofit2.Retrofit;

/**
 * Created by Romain on 15/08/2016.
 */
public class Social implements FougereModule {

    public static final String NAME = "Social";

    private static final String SERVER = "http://10.32.0.41:28017/";

    private final SocialDataPool socialDataPool;
    private final FougereDistance fougereDistance;
    private final SocialAPI api;
    private int ratio;
    private List<SocialUser> selUsers;

    public Social(final Activity activity, final FougereDistance fougereDistance) {
        this.socialDataPool = new SocialDataPool(activity);
        this.ratio = 100;
        this.fougereDistance = fougereDistance;

        this.api = new Retrofit.Builder().baseUrl(SERVER).build().create(SocialAPI.class);
    }

    @Override
    public void addData(final Data data) {
        this.socialDataPool.insert(SocialData.fromData(data));
    }

    @Override
    public void start() {
        List<SocialUser> users = null;

        try {
            users = this.api.listUsers().execute().body();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(Fougere.TAG, "[Social] Cannot get the social user list");
            return;
        }

        Log.d(Fougere.TAG, "[Social] Users: " + users.toString());

        Random rand = new Random();
        int nb = rand.nextInt(4);

        this.selUsers = new ArrayList<>();
        for (int i = 0; i < nb; i++) {
            this.selUsers.add(users.get(rand.nextInt(users.size())));
        }
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
