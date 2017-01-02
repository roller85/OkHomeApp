package id.co.okhome.okhomeapp.restclient;


import id.co.okhome.okhomeapp.model.VirtualAccountResultModel;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NicepayClient {

    @GET("nicepay/va/issue")
    Call<VirtualAccountResultModel> issueVirtualAccount(@Query("orderNo") String orderNo, @Query("userId") String userId, @Query("price") int price, @Query("bankCode") String bankCode);
}


