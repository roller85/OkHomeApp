package id.co.okhome.okhomeapp.lib.thridpartylogin;

import android.app.Activity;
import android.content.Intent;

/**
 * Created by josongmin on 2016-08-28.
 */

public abstract class JoThirdpartyLogin {

    public final static String PARAM_TYPE   = "TYPE";
    public final static String PARAM_AUTHKEY   = "AUTHKEY";
    public final static String PARAM_NAME   = "NAME";
    public final static String PARAM_ID     = "ID";
    public final static String PARAM_EMAIL  = "EMAIL";
    public final static String PARAM_IMG_URL  = "IMG_URL";

    public final static String PARAM_RAW    = "RAW";

    JoThirdpartyLoginListener loginListener;

    public void setLoginListener(JoThirdpartyLoginListener loginListener){
        this.loginListener = loginListener;
    }

    public abstract void init(Activity activity);
    public abstract void login();
    public abstract void logout();
    public abstract void onActivityResult(final int requestCode, final int resultCode, final Intent data);
}
