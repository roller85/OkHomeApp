package id.co.okhome.okhomeapp.gcm;

import android.content.Intent;

import com.google.android.gms.iid.InstanceIDListenerService;

/**
 * http://blog.saltfactory.net/android/implement-push-service-via-gcm.html
 */

public class OkhomeInstanceIDListenerService extends InstanceIDListenerService {

    @Override
    public void onTokenRefresh() {
        Intent intent = new Intent(this, OkhomeRegistrationIntentService.class);
        startService(intent);
    }
}