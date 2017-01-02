package id.co.okhome.okhomeapp.view.viewholder;

import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.mrjodev.jorecyclermanager.JoViewHolder;
import com.mrjodev.jorecyclermanager.annotations.LayoutMatcher;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.okhome.okhomeapp.R;
import id.co.okhome.okhomeapp.lib.Util;

/**
 * Created by josongmin on 2016-08-17.
 */
@LayoutMatcher(layoutId = R.layout.header_pointusage)
public class PointUsageHeaderHolder extends JoViewHolder<String> implements View.OnClickListener{

    @BindView(R.id.headerPointUsage_tvPoint)        TextView tvPoint;
    @BindView(R.id.headerPointUsage_vbtnCharge)     View vbtnCharge;

    public PointUsageHeaderHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void onViewCreated() {
        ButterKnife.bind(this, getView());
    }

    @Override
    public void onBind(String s, int pos, int absPos) {
        super.onBind(s, pos, absPos);
        tvPoint.setText(Util.getMoneyString(s, '.') + " 포인트");

        vbtnCharge.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Handler handler = getRcvParams().getParam("handlerChargeCredit");
        if(handler != null){
            handler.sendEmptyMessage(0);
        }
//        String item = getModel();
//        int pos = getPos();
//        Object caller = getRcvParams().getParam("CALLER");
//        if(caller instanceof CommonListDialog){
//            ((CommonListDialog) caller).onItemClick(item);
//        }
    }
}