package id.co.okhome.okhomeapp.restclient;


import java.util.List;

import id.co.okhome.okhomeapp.model.CleaningScheduleModel;
import id.co.okhome.okhomeapp.model.PeriodicCleaningPlanModel;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface CleaningRequestClient {


    ///request/oneday/{
    @POST("/cleaning_request/oneday/{userId}")
    Call<String> onedayRequest(@Path("userId") String userId, @Query("paramJson") String paramJson);

    @POST("/cleaning_request/periodic/{userId}")
    Call<String> periodicRequest(@Path("userId") String userId, @Query("paramJson") String paramJson);

    @GET("/cleaning_request/schedule/{userId}")
    Call<List<CleaningScheduleModel>> getMonthlyFilteredSchedule(@Path("userId") String userId, @Query("homeId") String homeId, @Query("yyyymm") String yyyymm);

    @GET("/cleaning_request/periodic/{userId}")
    Call<PeriodicCleaningPlanModel> getPeriodicCleaningPlan(@Path("userId") String userId, @Query("homeId") String homeId);

    @PATCH("/cleaning_request/periodic/{userId}")
    Call<String> updatePeriodicCleaning(@Path("userId") String userId, @Query("homeId") String homeId
            , @Query("weekType") String weekType, @Query("weekdays") String weekdays, @Query("beginDate") String beginDate
            , @Query("holdingOffBeginDate") String holdingOffBeginDate, @Query("holdingOffEndDate") String holdingOffEndDate);

    @PATCH("/cleaning_request/ignore/{userId}")
    Call<String> ignore(@Path("userId") String userId, @Query("homeId") String homeId, @Query("yyyymmdd") String yyyymmdd);

    @DELETE("/cleaning_request/{cleaningReqId}")
    Call<String> cancel(@Path("cleaningReqId") String cleaningReqId);



}



