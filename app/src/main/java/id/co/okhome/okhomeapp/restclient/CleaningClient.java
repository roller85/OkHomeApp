package id.co.okhome.okhomeapp.restclient;


import java.util.List;

import id.co.okhome.okhomeapp.model.CleaningModel;
import retrofit2.Call;
import retrofit2.http.GET;

public interface CleaningClient {

    @GET("/cleaning/extra")
    Call<List<CleaningModel>> getCleaningList();

}



