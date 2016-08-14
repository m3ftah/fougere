package fr.inria.rsommerard.fougere.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

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

    public static List<Data> deGsonify(final String json) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        Type type = new TypeToken<ArrayList<Data>>() {}.getType();

        return gson.fromJson(json, type);
    }

    public static String gsonify(final List<Data> data) {
        GsonBuilder builder = new GsonBuilder();
        builder.serializeNulls();
        Gson gson = builder.create();

        Type type = new TypeToken<ArrayList<Data>>() {}.getType();

        return gson.toJson(data, type);
    }

    @Override
    public String toString() {
        return this.content;
    }
}
