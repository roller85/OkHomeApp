package id.co.okhome.okhomeapp.view.dialog;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.okhome.okhomeapp.R;
import id.co.okhome.okhomeapp.config.CurrentUserInfo;
import id.co.okhome.okhomeapp.lib.Util;
import id.co.okhome.okhomeapp.lib.dialog.DialogController;
import id.co.okhome.okhomeapp.lib.dialog.ViewDialog;

/**
 * Created by josongmin on 2016-08-09.
 */

public class PaySpecialCleaningDialog extends ViewDialog{

    @BindView(R.id.dialogPayPoint_tvCurrentPoint)
    TextView tvCurrentPoint;

    @BindView(R.id.dialogPayPoint_tvDesc)
    TextView tvDesc;

    @BindView(R.id.dialogPayPoint_tvNeedPoint)
    TextView tvNeedPoint;

    @BindView(R.id.dialogPayPoint_tvNotEnough)
    TextView tvNotEnough;

    int price, hour, diffPrice;
    String desc;

    StatusListener statusListener;
    public PaySpecialCleaningDialog(int price, int hour, int diffPrice, String desc, StatusListener statusListener) {
        this.price = price;
        this.hour = hour;
        this.desc = desc;
        this.diffPrice = diffPrice;
        this.statusListener = statusListener;
    }

    @Override
    public View getView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.dialog_pay_point, null);
    }

    @Override
    public void onViewCreated() {
        ButterKnife.bind(this, getDecorView());

        tvNeedPoint.setText(Util.getMoneyString(price, '.') + " 포인트");
        //현재 포인트
        int creditNow = Integer.parseInt(CurrentUserInfo.get(getContext()).credit);
        tvCurrentPoint.setText(Util.getMoneyString(creditNow, '.') + " 포인트");
        tvDesc.setText(desc);

        if(price > creditNow){
            tvNotEnough.setVisibility(View.VISIBLE);
        }else{
            tvNotEnough.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.dialogPayPoint_vbtnCharge)
    public void onBtnCharge(View v){
        DialogController.showCenterDialog(getContext(), new ChargePointDialog());

    }

    @OnClick(R.id.dialogCommon_vbtnCancel)
    public void onCancel(View v){
        dismiss();
    }

    @OnClick(R.id.dialogCommon_vbtnConfirm)
    public void onBuy(View v){

        statusListener.onClickBuy();
        dismiss();
        //결제함니다
//        dismiss();
    }

    public interface StatusListener{
        void onClickBuy();

    }

}
