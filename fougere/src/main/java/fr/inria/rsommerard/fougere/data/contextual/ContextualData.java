package fr.inria.rsommerard.fougere.data.contextual;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Property;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Romain on 15/08/2016.
 */
@Entity
public class ContextualData {

    @Id(autoincrement = true)
    private Long id;

    @Property
    @NotNull
    private String identifier;

    @Property
    @NotNull
    private String content;

    @Generated(hash = 547844602)
    public ContextualData(Long id, @NotNull String identifier,
            @NotNull String content) {
        this.id = id;
        this.identifier = identifier;
        this.content = content;
    }

    @Generated(hash = 56720291)
    public ContextualData() {
    }

    public static List<ContextualData> deGsonify(final String json) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        Type type = new TypeToken<ArrayList<ContextualData>>() {}.getType();

        return gson.fromJson(json, type);
    }

    public static String gsonify(final List<ContextualData> data) {
        GsonBuilder builder = new GsonBuilder();
        builder.serializeNulls();
        Gson gson = builder.create();

        Type type = new TypeToken<ArrayList<ContextualData>>() {}.getType();

        return gson.toJson(data, type);
    }

    @Override
    public String toString() {
        return "{\"id\":\"" + this.id + "\",\"identifier\":\"" + this.identifier +
                "\",\"content\":\"" + this.content + "\"}";
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getIdentifier() {
        return this.identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
