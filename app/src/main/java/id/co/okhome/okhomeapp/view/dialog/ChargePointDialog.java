package id.co.okhome.okhomeapp.view.dialog;

import android.content.Intent;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.okhome.okhomeapp.R;
import id.co.okhome.okhomeapp.lib.Util;
import id.co.okhome.okhomeapp.lib.dialog.ViewDialog;
import id.co.okhome.okhomeapp.view.activity.PaymentActivity;
import id.co.okhome.okhomeapp.view.fragment.makereservation.flow.MakeCleaningReservationParam;

/**
 * Created by josongmin on 2016-08-09.
 */

public class ChargePointDialog extends ViewDialog{


    @BindView(R.id.dialogChargeCredit_ivChk1)           ImageView ivChk1;
    @BindView(R.id.dialogChargeCredit_ivChk2)           ImageView ivChk2;
    @BindView(R.id.dialogChargeCredit_ivChk3)           ImageView ivChk3;
    @BindView(R.id.dialogChargeCredit_ivChk4)           ImageView ivChk4;

    @BindView(R.id.dialogChargeCredit_etPoint)          EditText etPoint;
    @BindView(R.id.dialogChargeCredit_tvInputRp)        TextView tvInputRp;

    int pos = -1;
    int inputPoint = 0;
    int choosedPoint = 0;
    @Override
    public View getView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.dialog_charge_credit, null);
    }

    @Override
    public void onViewCreated() {
        ButterKnife.bind(this, getDecorView());

        etPoint.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                onChkClick(3);
                return false;
            }
        });
        etPoint.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                onChkClick(3);

                if(event.getAction() == KeyEvent.ACTION_UP){
                    //계산..)
                    String inputText = etPoint.getText().toString();
                    if(inputText.length() <= 0){
                        return false;
                    }

                    int point = Integer.parseInt(etPoint.getText().toString());
                    inputPoint = point;
                    choosedPoint = point;
                    String sPoint = point >= 1000 ? Util.getMoneyString(point / 1000, '.') + " Ribu" :  Util.getMoneyString(point, '.') + " Rp";
                    tvInputRp.setText(sPoint);

                }
                return false;
            }
        });
    }

    //체크클릭
    private void onChkClick(int pos){
        ivChk1.setImageResource(R.drawable.ic_done_circle);
        ivChk2.setImageResource(R.drawable.ic_done_circle);
        ivChk3.setImageResource(R.drawable.ic_done_circle);
        ivChk4.setImageResource(R.drawable.ic_done_circle);

        switch(pos){
            case 0:
                choosedPoint = 100000;
                ivChk1.setImageResource(R.drawable.ic_done_deepblue);
                break;
            case 1:
                choosedPoint = 200000;
                ivChk2.setImageResource(R.drawable.ic_done_deepblue);
                break;
            case 2:
                choosedPoint = 300000;
                ivChk3.setImageResource(R.drawable.ic_done_deepblue);
                break;
            case 3:
                ivChk4.setImageResource(R.drawable.ic_done_deepblue);
                choosedPoint = inputPoint;
                break;
        }
        this.pos = pos;
    }

    @OnClick({R.id.dialogChargeCredit_vbtnChk1, R.id.dialogChargeCredit_vbtnChk2, R.id.dialogChargeCredit_vbtnChk3, R.id.dialogChargeCredit_vbtnChk4,})
    public void onChkClick(View v){
        switch(v.getId()){
            case R.id.dialogChargeCredit_vbtnChk1:
                onChkClick(0);
                break;
            case R.id.dialogChargeCredit_vbtnChk2:
                onChkClick(1);
                break;
            case R.id.dialogChargeCredit_vbtnChk3:
                onChkClick(2);
                break;
            case R.id.dialogChargeCredit_vbtnChk4:
                onChkClick(3);
                break;
        }
    }

    @OnClick(R.id.dialogCommon_vbtnCancel)
    public void onCancel(View v){
        dismiss();
    }

    @OnClick(R.id.dialogCommon_vbtnConfirm)
    public void onConfirm(View v){
//        paramOrderNo = getIntent().getStringExtra("orderNo");
//        paramOrderName = getIntent().getStringExtra("orderName");
//        paramTotalPrice = getIntent().getIntExtra("totalPrice", 0);

        String orderNo = MakeCleaningReservationParam.makeOrderNo();
        String orderName = "포인트 " + Util.getMoneyString(choosedPoint, '.');
        int totalPrice = choosedPoint;


        if(totalPrice < 100000){
            Util.showToast(getContext(), "최소 결제금액은 100.000 Rp 입니다.");
            return;
        }
        getContext().startActivity(new Intent(getContext(), PaymentActivity.class)
                .putExtra("orderNo", orderNo)
                .putExtra("orderName", orderName)
                .putExtra("totalPrice", totalPrice));
    }

}
