package id.co.okhome.okhomeapp.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.okhome.okhomeapp.R;
import id.co.okhome.okhomeapp.config.IndoBank;
import id.co.okhome.okhomeapp.lib.OkHomeActivityParent;
import id.co.okhome.okhomeapp.lib.Util;
import id.co.okhome.okhomeapp.model.VirtualAccountResultModel;

public class PaymentVAConfirmActivity extends OkHomeActivityParent {

    @BindView(R.id.actPayVaConfirm_tvPrice)                 TextView tvPrice;
    @BindView(R.id.actPayVaConfirm_tvOrderName)             TextView tvItemName;
    @BindView(R.id.actPayVaConfirm_tvVA)                    TextView tvVA;
    @BindView(R.id.actPayVaConfirm_tvVA2)                   TextView tvVA2;
    @BindView(R.id.actPayVaConfirm_tvExpiryDateTime)        TextView tvExpiryDateTime;
    @BindView(R.id.actPayVaConfirm_ivLogoBank)              ImageView ivBankLogo;
    @BindView(R.id.actPayVaConfirm_vgHowtoType1Contents)    View vHowtoType1Contents;
    @BindView(R.id.actPayVaConfirm_vgHowtoType2Contents)    View vHowtoType2Contents;
    @BindView(R.id.actPayVaConfirm_vgHowtoType3Contents)    View vHowtoType3Contents;
    @BindView(R.id.actPayVaConfirm_ivArrowHowtoType1)      ImageView ivArrowType1;
    @BindView(R.id.actPayVaConfirm_ivArrowHowtoType2)      ImageView ivArrowType2;
    @BindView(R.id.actPayVaConfirm_ivArrowHowtoType3)      ImageView ivArrowType3;

    VirtualAccountResultModel vaModel;
    int paramTotalPrice = 0;
    String paramOrderName = "";
    String paramOrderNo = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_va_confirm);
        ButterKnife.bind(this);

        vaModel = (VirtualAccountResultModel)getIntent().getSerializableExtra("virtualAccountResultModel");
        if(vaModel == null){
            finish();
            return;
        }

        paramOrderNo = getIntent().getStringExtra("orderNo");
        paramOrderName = getIntent().getStringExtra("orderName");
        paramTotalPrice = getIntent().getIntExtra("totalPrice", 0);

        vHowtoType1Contents.setVisibility(View.GONE);
        vHowtoType2Contents.setVisibility(View.GONE);
        vHowtoType3Contents.setVisibility(View.GONE);


        adaptViewAndData();
        toggleViewVisibility(vHowtoType1Contents, ivArrowType1);
    }

    //뷰 토글
    private void toggleViewVisibility(View vTarget, ImageView ivArrow){
        Boolean on = (Boolean)vTarget.getTag();
        if(on == null){
            on = false;
        }

        on = !on;

        if(on){
            vTarget.setVisibility(View.VISIBLE);
            ivArrowType1.setRotation(180);
        }else{
            vTarget.setVisibility(View.GONE);
            ivArrowType1.setRotation(0);
        }

        vTarget.setTag(on);

    }

    private void adaptViewAndData(){
        tvPrice.setText(Util.getMoneyString(paramTotalPrice, '.') + "Rp");
        tvItemName.setText(paramOrderName);
        tvVA.setText(vaModel.bankVacctNo);
        tvVA2.setText(vaModel.bankVacctNo);

        int logoId= 0;
        if(vaModel.bankCd.equals(IndoBank.BCA.bankCode)){
            logoId = IndoBank.BCA.logoId;
        }else if(vaModel.bankCd.equals(IndoBank.PERMATA.bankCode)){
            logoId = IndoBank.PERMATA.logoId;
        }else if(vaModel.bankCd.equals(IndoBank.MANDIRI.bankCode)){
            logoId = IndoBank.MANDIRI.logoId;
        }else if(vaModel.bankCd.equals(IndoBank.HANA.bankCode)){
            logoId = IndoBank.HANA.logoId;
        }else if(vaModel.bankCd.equals(IndoBank.BNI.bankCode)){
            logoId = IndoBank.BNI.logoId;
        }else if(vaModel.bankCd.equals(IndoBank.MAY.bankCode)){
            logoId = IndoBank.MAY.logoId;
        }
        ivBankLogo.setImageResource(logoId);

        //국가별 분기..
        String locale = Util.getCurrentLang(this);
        if(locale.equals("ko")){
            String datetimeExpiry = Util.changeDatetimeFormat(vaModel.expiryDateTime, "yyyyMMddHHmmss", "MM월 dd일 HH시 mm분", Locale.KOREA);
            tvExpiryDateTime.setText(datetimeExpiry + "까지 ");
        }else if(locale.equals("en")){
            String datetimeExpiry = Util.changeDatetimeFormat(vaModel.expiryDateTime, "yyyyMMddHHmmss", "MM/dd, HH:mm", Locale.KOREA);
            tvExpiryDateTime.setText(datetimeExpiry);
        }else if(locale.equals("id")){
            String datetimeExpiry = Util.changeDatetimeFormat(vaModel.expiryDateTime, "yyyyMMddHHmmss", "MM/dd, HH:mm", Locale.KOREA);
            tvExpiryDateTime.setText(datetimeExpiry);
        }
    }

    @OnClick(R.id.actPayVaConfirm_vgHowtoType1)
    public void onHowtoType1(View v){
        toggleViewVisibility(vHowtoType1Contents, ivArrowType1);
    }

    @OnClick(R.id.actPayVaConfirm_vgHowtoType2)
    public void onHowtoType2(View v){
        toggleViewVisibility(vHowtoType2Contents, ivArrowType2);
    }

    @OnClick(R.id.actPayVaConfirm_vgHowtoType3)
    public void onHowtoType3(View v){
        toggleViewVisibility(vHowtoType3Contents, ivArrowType3);
    }

    @OnClick(R.id.common_vbtnBack)
    public void onBtnBack(View v){
        finish();
    }
}
