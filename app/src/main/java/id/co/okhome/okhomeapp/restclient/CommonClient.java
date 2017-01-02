package id.co.okhome.okhomeapp.restclient;


import java.util.List;
import java.util.Map;

import id.co.okhome.okhomeapp.model.AddressItemModel;
import id.co.okhome.okhomeapp.model.UserModel;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface CommonClient {

    @GET("version/{os}") //map은 id, value로 구성됨.
    Call<Map> getVersion(@Path("os") String os);

    /**key하나 넘기면 키 하나 리턴. key1,key2,key3으로 넘기면 맵으로 각각에 맞춰서 리턴*/
    @GET("common/variable/{key}")
    Call<String> getVariableValue(@Path("key") String key);

    @POST("sms")
    Call<UserModel> sendSms(@Query("to") String to, @Query("sms") String sms);

    @GET("address") //map은 id, value로 구성됨.
    Call<List<AddressItemModel>> getAddress(@Query("parentId") String parentId);

    @GET("common/check_koreacard/{bin}") //map은 id, value로 구성됨.
    Call<Boolean> isKoreaCard(@Path("bin") String bin);

    @Multipart
    @POST("file")
    Call<String> uploadFile(@Part("description") RequestBody description, @Part MultipartBody.Part file);


}


