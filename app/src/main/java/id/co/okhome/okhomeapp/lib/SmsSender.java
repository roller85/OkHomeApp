package id.co.okhome.okhomeapp.lib;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.SmsManager;
import android.widget.Toast;

/**
 * Created by josongmin on 2016-08-28.
 */

public class SmsSender {

    Context context;
    SmsManager smsManager;
    PendingIntent sentIntent;
    BroadcastReceiver receiver;
    boolean isInit = false;

    private static SmsSender Instance;
    public final static SmsSender with(Context context){
        if(Instance == null){
            SmsSender smsSender = new SmsSender(context);
            Instance = smsSender;
        }
        return Instance;
    }


    public SmsSender(Context context) {
        this.context = context;
    }

    public void unregister(){
        context.unregisterReceiver(receiver);
    }

    public void sendMessage(String to, String msg){
        smsManager.sendTextMessage(to, null, msg, sentIntent, null);
    }

    public SmsSender init(){
        if(isInit) return this;

        PendingIntent sentIntent = PendingIntent.getBroadcast(context, 0, new Intent("SMS_SENT_ACTION"), 0);
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch(getResultCode()){
                    case Activity.RESULT_OK:
                        // 전송 성공
                        Toast.makeText(context, "전송 완료", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        // 전송 실패
                        Toast.makeText(context, "전송 실패", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        // 서비스 지역 아님
                        Toast.makeText(context, "서비스 지역이 아닙니다", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        // 무선 꺼짐
                        Toast.makeText(context, "무선(Radio)가 꺼져있습니다", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        // PDU 실패
                        Toast.makeText(context, "PDU Null", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };

        context.registerReceiver(receiver, new IntentFilter("SMS_SENT_ACTION"));
        smsManager = SmsManager.getDefault();
        isInit = true;
        return this;
    }
}
