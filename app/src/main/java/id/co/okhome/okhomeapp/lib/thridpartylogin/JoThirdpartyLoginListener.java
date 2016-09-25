package id.co.okhome.okhomeapp.lib.thridpartylogin;

import java.util.Map;

/**
 * Created by josongmin on 2016-08-28.
 */

public interface JoThirdpartyLoginListener {
    public void onLogin(Map<String, Object> params);
    public void onFailed(String message);
    public void onLogout();
}
