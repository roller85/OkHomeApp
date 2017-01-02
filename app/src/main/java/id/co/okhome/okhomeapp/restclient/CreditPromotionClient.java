package id.co.okhome.okhomeapp.restclient;


import java.util.List;

import id.co.okhome.okhomeapp.model.CreditPromotionModel;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface CreditPromotionClient {

    @GET("creditpromotion/freecharge/{userId}")
    Call<List<CreditPromotionModel>> getCreditPromotionList(@Path("userId") String userId);

    @POST("creditpromotion/applypromotion_friend/{userId}")
    Call<String> applyFriendPromotionCode(@Path("userId") String userId, @Query("promotionId") String promotionId, @Query("friendCode") String friendCode);

    @POST("creditpromotion/applypromotion/{userId}")
    Call<String> applyPromotionCode(@Path("userId") String userId, @Query("promotionId") String promotionId, @Query("promotionCode") String promotionCode);

    @POST("creditpromotion/applypromotion_poll")
    Call<String> applyPromotionPoll(@Query("userId") String userId, @Query("promotionId") String promotionId, @Query("pollResult") String promotionCode);


}


