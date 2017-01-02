package id.co.okhome.okhomeapp.restclient;


import id.co.okhome.okhomeapp.model.UserModel;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UserRestClient {

    @GET("user/login")
    Call<UserModel> login(@Query("email") String email, @Query("password") String password);

    @GET("user/{userId}")
    Call<UserModel> getUser(@Path("userId") String userId);

    @GET("user/by/phone/{phone}")
    Call<UserModel> getUserByPhone(@Path("phone") String phone);

    @GET("user/{userId}")
    Call<String> getUserFieldValue(@Path("userId") String userId, @Query("field") String field);

    @POST("user")
    Call<String> join(@Query("name") String name, @Query("email") String email, @Query("phone") String phone, @Query("password") String password, @Query("photoUrl") String photoUrl);

    @POST("user/authentication")
    Call<UserModel> authentication(
            @Query("accountType") String accountType
            , @Query("authKey") String authKey
            , @Query("email") String email, @Query("name") String name, @Query("phone") String phone, @Query("photoUrl") String photoUrl);


    @PATCH("user/{userId}/single")
    Call<String> updateUserSingleInfo(@Path("userId") String userId, @Query("key") String key, @Query("value") String value);

    @PATCH("user/{userId}/phone/{phone}")
    Call<String> updatePhone(@Path("userId") String userId, @Path("phone") String phone);


    @PATCH("user/password/{email}")
    Call<String> updatePassword(@Path("email") String email, @Query("password") String password);

    //logout
    @PATCH("user/logout/{userId}")
    Call<String> logout(@Path("userId") String userId);

    @DELETE("user/quit/{userId}")
    Call<String> quit(@Path("userId") String userId);

    //비밀번호 재설정 메일보내기
//    mail/resetting_password/{email:.+}
    @GET("user/mail/resetting_password/{email}")
    Call<String> requestResettingPassword(@Path("email") String email);


}


