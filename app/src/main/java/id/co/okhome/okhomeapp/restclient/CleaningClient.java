package id.co.okhome.okhomeapp.restclient;


import java.util.List;
import java.util.Map;

import id.co.okhome.okhomeapp.model.CleaningTicketModel;
import id.co.okhome.okhomeapp.model.SpcCleaningModel;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface CleaningClient {

    @GET("/cleaning/extra")
    Call<List<SpcCleaningModel>> getCleaningList();

    @GET("/cleaning/ticket/summary/{userId}")
    Call<Map<String, String>> getCleaningCountSummary(@Path("userId") String userId);

    @GET("/cleaning/ticket/{userId}")
    Call<List<CleaningTicketModel>> getCleaningTicketList(@Path("userId") String userId);

    @POST("/cleaning/chk_change_spc_cleaning")
    Call<Integer> chkAbleToChangeSpcCleaning(@Query("userId") String userId, @Query("cleaningReqId") String cleaningReqId, @Query("spcCleaningIds") String spcCleaningIds);

    @POST("/cleaning/change_spc_cleaning")
    Call<String> changeSpcCleaning(@Query("userId") String userId, @Query("cleaningReqId") String cleaningReqId, @Query("spcCleaningIds") String spcCleaningIds);

    @DELETE("/cleaning/cancel_cleaning")
    Call<String> cancelCleaning(@Query("userId") String userId, @Query("cleaningReqId") String cleaningReqId);

    ///cleaning/
}



