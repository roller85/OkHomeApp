package id.co.okhome.okhomeapp.lib.retrofit;


import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import id.co.okhome.okhomeapp.lib.OkhomeException;
import id.co.okhome.okhomeapp.lib.Util;
import id.co.okhome.okhomeapp.lib.retrofit.restmodel.ErrorModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by josongmin on 2016-02-18.
 */
public abstract class RetrofitCallback<T> implements Callback<T>{

    private static Context context = null;
    public static void setApplicationContext(Context context){
        RetrofitCallback.context = context;
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        Util.Log(call.request().toString());
        onFinish();
        if(!response.isSuccessful()){
            //http response code가 실패면
            try{
                Gson gson = new GsonBuilder()
                        .setDateFormat("yyyy'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'")
                        .create();

                ErrorModel errorModel = gson.fromJson(response.errorBody().string(), ErrorModel.class);
                onError(errorModel);
            }catch(IOException e){
                onError(null);
            }

        }else{
            Object obj = response.body();
            if(obj == null){
                onFailure(call, new OkhomeException("obj is null"));
            }
            else if(obj instanceof ErrorModel){
                onError((ErrorModel)obj);
            }else{
                onSuccess((T)obj);
            }
        }

    }

    private void onError(ErrorModel errorModel){
        Util.Log(errorModel.code + " " + errorModel.message + " " + errorModel.obj);
        onJodevError(errorModel);
    }

    public void onFinish(){};
    abstract public void onSuccess(T result);
    public void onJodevError(ErrorModel jodevErrorModel){
        //오버라이딩안되면 자동으로 토스트만 듸움
        Util.showToast(RetrofitCallback.context, jodevErrorModel.message);
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        onFinish();
        onJodevError(new ErrorModel(-1111+"", t.getMessage(), null));

    }


}
