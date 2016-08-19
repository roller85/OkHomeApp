package id.co.okhome.okhomeapp.lib;

import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by josongmin on 2016-08-18.
 */

public class AnimatedTooltipTextController {

    final int tooltipDelay = 2200;
    TextView tvToolTip;
    String[] arrTooltips;

    static Map<TextView, AnimatedTooltipTextController> mapInstance = new HashMap<>();

    public static final AnimatedTooltipTextController with(TextView tvToolTip){
        AnimatedTooltipTextController controller = mapInstance.get(tvToolTip);
        if(controller == null){
            controller = new AnimatedTooltipTextController(tvToolTip);
        }
        return controller;
    }


    public AnimatedTooltipTextController(TextView tvToolTip) {
        this.tvToolTip = tvToolTip;
    }

    public AnimatedTooltipTextController setArrTooltips(String[] arrTooltips) {
        this.arrTooltips = arrTooltips;
        return this;
    }

    public void start(){
        tvToolTip.setText(arrTooltips[0]);
        handlerForTooltip.sendEmptyMessageDelayed(0, 0);
    }

    public void stop(){
        handlerForTooltip.removeMessages(0);
    }

    Handler handlerForTooltip = new Handler(){
        int pos = 0;
        @Override
        public void dispatchMessage(Message msg) {
            tvToolTip.setText(arrTooltips[pos % arrTooltips.length]);
            tvToolTip.animate().translationX(20).alpha(0f).setDuration(0).start();
            tvToolTip.animate().translationX(0).alpha(1f).setDuration(100).start();
            pos++;
            sendEmptyMessageDelayed(0, tooltipDelay);
        }
    };
}
