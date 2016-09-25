package id.co.okhome.okhomeapp.restclient;

import java.util.HashMap;
import java.util.Map;

import id.co.okhome.okhomeapp.lib.retrofit.RetrofitFactory;

/**
 * Created by josongmin on 2016-02-18.
 */

//각 retrofit클라이언트들을 싱글톤으로 관리함.
public class RestClient {

    private static Map<String, Object> mapClient = new HashMap<>();

    public final static <T> T getInstance(final Class<T> service){
        final String key = service.toString();
        T client = (T)mapClient.get(key);
        if(client == null){
            client = RetrofitFactory.getRestClient(service);
            mapClient.put(key, client);
        }
        return client;
    }
//    /**유저용 레스트 클라이언트*/
    public final static UserRestClient getUserRestClient(){
        return RestClient.getInstance(UserRestClient.class);
    }

    public final static CommonClient getCommonClient(){
        return RestClient.getInstance(CommonClient.class);
    }

    public final static CertificationClient getCertificationClient(){
        return RestClient.getInstance(CertificationClient.class);
    }

    public final static HomeRestClient getHomeClient(){
        return RestClient.getInstance(HomeRestClient.class);
    }

    public final static NoticeClient getNoticeClient(){
        return RestClient.getInstance(NoticeClient.class);
    }

    public final static CreditClient getCreditClient(){
        return RestClient.getInstance(CreditClient.class);
    }

    public final static CleaningClient getCleaningClient(){
        return RestClient.getInstance(CleaningClient.class);
    }

    public final static CleaningRequestClient getCleaningRequestClient(){
        return RestClient.getInstance(CleaningRequestClient.class);
    }
}
