package id.co.okhome.okhomeapp.view.viewholder;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.mrjodev.jorecyclermanager.JoViewHolder;
import com.mrjodev.jorecyclermanager.annotations.LayoutMatcher;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.okhome.okhomeapp.R;
import id.co.okhome.okhomeapp.lib.Util;
import id.co.okhome.okhomeapp.model.CreditHistoryModel;

/**
 * Created by josongmin on 2016-08-17.
 */
@LayoutMatcher(layoutId = R.layout.item_history)
public class CreditHistoryHolder extends JoViewHolder<CreditHistoryModel> implements View.OnClickListener{

    @BindView(R.id.itemHistory_tvComment)       TextView tvComment;
    @BindView(R.id.itemHistory_tvMoney)         TextView tvMoney;
    @BindView(R.id.itemHistory_tvTime)          TextView tvTime;
    @BindView(R.id.itemHistory_tvType)          TextView tvType;



    public CreditHistoryHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void onViewCreated() {
        ButterKnife.bind(this, getView());
    }

    @Override
    public void onBind(CreditHistoryModel m, int pos, int absPos) {
        super.onBind(m, pos, absPos);

        Integer credit = Integer.parseInt(m.credit);
        if(credit < 0){
            //사용함
            tvMoney.setTextColor(Color.parseColor("#787878"));
        }else{
            tvMoney.setTextColor(Color.parseColor("#35a9f3"));
        }

        tvMoney.setText(Util.getMoneyString(credit, '.') + " Credit");
        tvType.setText(m.type);
    }

    @Override
    public void onClick(View v) {
    }
}