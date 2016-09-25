package id.co.okhome.okhomeapp.restclient;


import java.util.List;

import id.co.okhome.okhomeapp.model.NoticeModel;
import retrofit2.Call;
import retrofit2.http.GET;

public interface NoticeClient {

    @GET("notice")
    Call<List<NoticeModel>> getNoticeList();
}


