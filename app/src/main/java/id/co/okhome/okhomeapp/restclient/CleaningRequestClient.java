package id.co.okhome.okhomeapp.restclient;


import java.util.List;

import id.co.okhome.okhomeapp.model.CleaningReservationModel;
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

    //ver2------------

    //http://192.168.1.228:3111/cleaning_request/period_check

    @GET("/cleaning_request/period_check")
    Call<List<CleaningReservationModel>> requestPeriodCleaningCheck(@Query("orderNo") String orderNo, @Query("userId") String userId, @Query("homeId") String homeId
            , @Query("periodType") String periodType, @Query("periodValue") String periodValue, @Query("cleaningCount") String cleaningCount
            , @Query("beginDate") String beginDate);

    @POST("/cleaning_request/days")
    Call<String> requestCleanings(
            @Query("cleaningType") String cleaningType
            , @Query("orderNo") String orderNo, @Query("userId") String userId, @Query("homeId") String homeId
            , @Query("cleaningCount") String cleaningCount
            , @Query("totalBasicCleaningPrice") int totalBasicCleaningPrice
            , @Query("cleaningDateTimes") String cleaningDateTimes, @Query("cleaningNumbersForSpc") String cleaningNumbersForSpc, @Query("spcCleaningIdsSets") String spcCleaningIdsSets);


    @POST("/cleaning_request/period")
    Call<String> requestPeriodCleaning(@Query("orderNo") String orderNo, @Query("userId") String userId, @Query("homeId") String homeId
            , @Query("periodType") String periodType, @Query("periodValue") String periodValue, @Query("cleaningCount") String cleaningCount
            , @Query("beginDate") String beginDate, @Query("totalBasicCleaningPrice") int totalBasicCleaningPrice, @Query("cleaningNumbersForSpc") String cleaningNumbersForSpc, @Query("spcCleaningIdsSets") String spcCleaningIdsSets);

    @GET("/cleaning/monthly_schedule")
    Call<List<CleaningScheduleModel>> getMonthlyCleaningSchedule(@Query("userId") String userId, @Query("homeId") String homeId, @Query("yearMonth") String yearMonth);

    //ver1----------------

    ///request/oneday/{
    @POST("/cleaning_request/oneday/{userId}")
    Call<String> onedayRequest(@Path("userId") String userId, @Query("paramJson") String paramJson);


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



