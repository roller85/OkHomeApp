package id.co.okhome.okhomeapp.gcm;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

/**
 * Created by josongmin on 2016-08-18.
 * 기존 gcm소스 기반 팩토리.
 */

public class OkhomeGcmController {

    public final static String REGISTRATION_COMPLETE = "registrationComplete";
    public final static String REGISTRATION_GENERATING = "registrationGenerating";
    public final static String REGISTRATION_READY = "registrationReady";

    private Activity activitiy;
    private BroadcastReceiver registrationBcr;
    private OkHomeGcmListener okHomeGcmListener;

    public final static OkhomeGcmController with(Activity act){
        return new OkhomeGcmController(act).init();
    }

    public OkhomeGcmController(Activity activitiy) {
        this.activitiy = activitiy;
    }

    public OkhomeGcmController setOkHomeGcmListener(OkHomeGcmListener okHomeGcmListener) {
        this.okHomeGcmListener = okHomeGcmListener;
        return this;
    }

    public void requestGcmToken() {
        if (checkPlayServices()) {
            Intent intent = new Intent(activitiy, OkhomeRegistrationIntentService.class);
            activitiy.startService(intent);
        }
    }

    public OkhomeGcmController init(){
        registrationBcr = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();


                if(action.equals(REGISTRATION_READY)){
                    ;
                } else if(action.equals(REGISTRATION_GENERATING)){
                    ;
                } else if(action.equals(REGISTRATION_COMPLETE)){

                    //requestToken() 후 완료되면이쪽으로 들어옴.
                    String token = intent.getStringExtra("token");
                    Log.d("OKHOME", token);
                    if(okHomeGcmListener != null){
                        okHomeGcmListener.onGcmTokenReady(token);
                    }

                }

            }
        };

        LocalBroadcastManager.getInstance(activitiy).registerReceiver(registrationBcr, new IntentFilter(REGISTRATION_READY));
        LocalBroadcastManager.getInstance(activitiy).registerReceiver(registrationBcr, new IntentFilter(REGISTRATION_GENERATING));
        LocalBroadcastManager.getInstance(activitiy).registerReceiver(registrationBcr, new IntentFilter(REGISTRATION_COMPLETE));

        return this;
    }

    public void stop(){
        //해제
        LocalBroadcastManager.getInstance(activitiy).unregisterReceiver(registrationBcr);
    }


    private boolean checkPlayServices() {
        final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(activitiy);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, activitiy, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                activitiy.finish();
            }
            return false;
        }
        return true;
    }

    public interface OkHomeGcmListener{
        public void onGcmTokenReady(String token);
    }
}
