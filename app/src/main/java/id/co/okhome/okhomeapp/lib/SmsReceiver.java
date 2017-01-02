package id.co.okhome.okhomeapp.lib;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.telephony.SmsMessage;

/**
 * Created by josong on 2016-11-25.
 */

public class SmsReceiver {

    BroadcastReceiver smsReceiver = null;
    Activity activity;

    public SmsReceiver(Activity activity){
        this.activity = activity;
        ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.RECEIVE_SMS},1);
    }

    public void init(final OnSmsReceivedListener onSmsReceivedListener){
        smsReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")){
                    StringBuilder ab = new StringBuilder();
                    Bundle bundle = intent.getExtras();

                    if(bundle != null){
                        Object[] pdusObj = (Object[]) bundle.get("pdus");
                        SmsMessage[] messages = new SmsMessage[pdusObj.length];

                        for (int i = 0; i < pdusObj.length; i++) {
                            messages[i] = SmsMessage.createFromPdu ((byte[])pdusObj[i]);
                            ab.append(messages[i].getDisplayMessageBody());
                        }
                    }

                    onSmsReceivedListener.onSmsReceive(ab.toString());
                    onSmsReceivedListener.onOkhomeVerificationSmsReceive(ab.toString().substring(0, 4));
                    activity.unregisterReceiver(smsReceiver);
                }
            }
        };

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");

        activity.registerReceiver(smsReceiver, intentFilter);

    }

    public interface OnSmsReceivedListener{
        public void onSmsReceive(String message);
        public void onOkhomeVerificationSmsReceive(String message);

    }

}
