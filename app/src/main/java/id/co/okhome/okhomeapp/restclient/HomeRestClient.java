package id.co.okhome.okhomeapp.restclient;


import retrofit2.Call;
import retrofit2.http.PATCH;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface HomeRestClient {

    @PATCH("home/{homeId}/address")
    Call<String> updateHomeAddress(@Path("homeId") String userId, @Query("address1") String address1, @Query("address2") String address2
            , @Query("address3") String address3, @Query("address4") String address4);

    @PATCH("home/{homeId}/extra")
    Call<String> updateHomeExtraInfo(@Path("homeId") String userId, @Query("homeType") String homeType, @Query("homeSize") String homeSize
            , @Query("floorCnt") String floorCnt, @Query("restroomCnt") String restroomCnt, @Query("pets") String pets);


}


