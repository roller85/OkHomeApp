package id.co.okhome.okhomeapp.lib.retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import id.co.okhome.okhomeapp.config.Variables;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by josongmin on 2016-02-16.
 */
public class RetrofitFactory {
    private static Retrofit mRetrofit = null;

    public static final <T> T getRestClient(final Class<T> service){
//        if(mRetrofit == null){

            //기본 인터넷ㅂ터 설정
            Interceptor interceptor = new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request request = chain.request();
                    //공용 파라메터 설정
//                    HttpUrl url = request.url().newBuilder().addQueryParameter("staticParam","JOSONGMIN").build();
//                    request = request.newBuilder().url(url).build();

                    Headers headers = request.headers().newBuilder()
                            .add("Accept", "application/json")
                            .add("Accept-Language", "kr")
                            .build();
                    request = request.newBuilder().headers(headers).build();

                    return chain.proceed(request);
                }
            };

            //클라이언트 설정
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(20, TimeUnit.SECONDS)
                    .readTimeout(20, TimeUnit.SECONDS)
                    .addInterceptor(interceptor).
                    build();
            //gSON설정
            Gson gson = new GsonBuilder()
                    .registerTypeAdapterFactory(new OkhomeGsonTypeAdapter())
                    .setDateFormat("yyyy'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'")
                    .create();

            mRetrofit = new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .baseUrl(Variables.APP_BASE_URL)
                    .client(client)
                    .build();
//        }

        return mRetrofit.create(service);
    }



}
