package id.co.okhome.okhomeapp.config;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import id.co.okhome.okhomeapp.R;
import id.co.okhome.okhomeapp.lib.JoSharedPreference;
import id.co.okhome.okhomeapp.lib.Util;
import id.co.okhome.okhomeapp.model.UserModel;

/**
 * Created by josongmin on 2016-08-29.
 */

public class CurrentUserInfo {

    private static List<OnUserInfoChangeListener> listListener = new ArrayList();
    private static final String key = "MyUserModel";
    public final static UserModel get(Context context){
        return JoSharedPreference.with(context).get(key);
    }

    public final static String getId(Context context){
        try{
            return JoSharedPreference.with(context).<UserModel>get(key).id;
        }catch(Exception e){
            return null;
        }
    }

    public final static String getHomeId(Context context){
        try{
            if(JoSharedPreference.with(context).<UserModel>get(key).listHomeModel != null){
                return JoSharedPreference.with(context).<UserModel>get(key).listHomeModel.get(0).id;
            }
            return null;
        }catch(Exception e){
            return null;
        }
    }

    public final static void set(Context context, UserModel userModel){
        JoSharedPreference.with(context).push(key, userModel);
        notifyChanged(userModel);
    }

    public final static void clear(Context context){
        JoSharedPreference.with(context).push(key, null);
    }

    public final static boolean isLogin(Context context){
        return JoSharedPreference.with(context).get(key) != null ? true : false;
    }

    private static void notifyChanged(UserModel userModel){
        for(OnUserInfoChangeListener listener : listListener){
           try{
               if(listener != null){
                   listener.onUserInfoChanged(userModel);
               }
           }catch(Exception e){
                //문제발생
               Util.Log(e.toString());
           }
        }
    }

    public static final void loadUserImg(Context context, String userPhotoUrl, ImageView iv){
        Glide.with(context)
                .load(userPhotoUrl)
                .thumbnail(0.5f)
                .placeholder(R.drawable.img_user_blank)
                .error(R.drawable.img_user_blank)
                .dontAnimate()
                .into(iv);
    }

    /**특정리스너 실행하기*/
    public static void invokeCallback(Context context, OnUserInfoChangeListener listener){
        listener.onUserInfoChanged(get(context));
    }

    /**등록*/
    public static void registerUserInfoChangeListener(OnUserInfoChangeListener listener){
        listListener.add(listener);
    }

    public static void unregisterUserInfoChangeListener(OnUserInfoChangeListener listener){
        listListener.remove(listener);
    }

    public interface OnUserInfoChangeListener{
        public void onUserInfoChanged(UserModel userModel);
    }
}
