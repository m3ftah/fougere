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

import fr.inria.rsommerard.fougere.data.Data;

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

    @Property
    @NotNull
    private int ttl;

    @Property
    @NotNull
    private int disseminate;

    @Property
    @NotNull
    private int sent;

    @Generated(hash = 938852588)
    public ContextualData(Long id, @NotNull String identifier, @NotNull String content, int ttl,
            int disseminate, int sent) {
        this.id = id;
        this.identifier = identifier;
        this.content = content;
        this.ttl = ttl;
        this.disseminate = disseminate;
        this.sent = sent;
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

    public static Data toData(final ContextualData data) {
        return new Data(null, data.getIdentifier(), data.getContent(), data.getTtl(),
                data.getDisseminate(), data.getSent());
    }

    public static ContextualData fromData(final Data data) {
        return new ContextualData(null, data.getIdentifier(), data.getContent(), data.getTtl(),
                data.getDisseminate(), data.getSent());
    }

    @Override
    public String toString() {
        return "{\"id\":\"" + this.id + "\",\"identifier\":\"" + this.identifier +
                "\",\"content\":\"" + this.content + "\",\"ttl\":\"" + this.ttl +
                "\",\"disseminate\":\"" + this.disseminate + "\",\"sent\":\"" + this.sent + "\"}";
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

    public int getSent() {
        return this.sent;
    }

    public void setSent(int sent) {
        this.sent = sent;
    }

    public int getDisseminate() {
        return this.disseminate;
    }

    public void setDisseminate(int disseminate) {
        this.disseminate = disseminate;
    }

    public int getTtl() {
        return this.ttl;
    }

    public void setTtl(int ttl) {
        this.ttl = ttl;
    }
}
