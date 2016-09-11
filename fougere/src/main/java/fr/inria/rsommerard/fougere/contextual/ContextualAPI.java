package fr.inria.rsommerard.fougere.contextual;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by romain on 06/09/16.
 */
public interface ContextualAPI {
    @GET("users")
    Call<List<ContextualUser>> listUsers();
}
