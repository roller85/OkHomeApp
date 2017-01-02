package id.co.okhome.okhomeapp.restclient;


import java.util.List;

import id.co.okhome.okhomeapp.model.CreditLogModel;
import id.co.okhome.okhomeapp.model.CreditProductModel;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface CreditClient {

    @GET("credit/{userId}")
    Call<List<CreditLogModel>> getCreditList(@Path("userId") String userId, @Query("type") String type, @Query("rownum") String rownum, @Query("limit") String limit);

    //http://192.168.0.103:3111/credit/product
    @GET("credit/product")
    Call<List<CreditProductModel>> getCreditProductList();


}


