package fr.inria.rsommerard.fougere.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by Romain on 05/08/16.
 */
public class Data {

    public final String identifier;
    public final String content;

    public Data(final String identifier, final String content) {
        this.identifier = identifier;
        this.content = content;
    }

    public static Data deGsonify(final String json) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        return gson.fromJson(json, Data.class);
    }

    public static String gsonify(Data data) {
        GsonBuilder builder = new GsonBuilder();
        builder.serializeNulls();
        Gson gson = builder.create();

        return gson.toJson(data);
    }

    @Override
    public String toString() {
        return this.content;
    }
}
