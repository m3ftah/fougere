package fr.inria.rsommerard.fougere.social;

/**
 * Created by romain on 06/09/16.
 */

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface SocialAPI {
    @GET
    Call<List<SocialUser>> listUsers();
}
