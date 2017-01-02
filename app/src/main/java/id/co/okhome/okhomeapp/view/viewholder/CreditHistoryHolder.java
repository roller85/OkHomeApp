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
import id.co.okhome.okhomeapp.model.CreditLogModel;

import static java.lang.Integer.parseInt;

/**
 * Created by josongmin on 2016-08-17.
 */
@LayoutMatcher(layoutId = R.layout.item_point_usage)
public class CreditHistoryHolder extends JoViewHolder<CreditLogModel> implements View.OnClickListener{

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
    public void onBind(CreditLogModel m, int pos, int absPos) {
        super.onBind(m, pos, absPos);

        Integer credit = parseInt(m.credit);
        if(m.type.equals("CHARGE")){
            tvMoney.setTextColor(Color.parseColor("#35a9f3"));
        }else{
            tvMoney.setTextColor(Color.parseColor("#787878"));
        }
        int totalCredit = Integer.parseInt(m.credit) + Integer.parseInt(m.bonusCredit);

        tvMoney.setText(Util.getMoneyString(totalCredit, '.') + " Credit");
        tvTime.setText(Util.getFormattedDateString(m.insertDate, "MM-dd hh:mma"));
        String typeTag = "";
        if(m.typeValue.equals("S_REFUND")){
            typeTag = "환불";
        }else if(m.typeValue.equals("CARD")){
            typeTag = "카드";
        }

        tvType.setText(typeTag);

        tvComment.setText(m.comment);
        //

    }

    @Override
    public void onClick(View v) {
    }
}