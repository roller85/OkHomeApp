package id.co.okhome.okhomeapp.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.okhome.okhomeapp.R;
import id.co.okhome.okhomeapp.config.CurrentUserInfo;
import id.co.okhome.okhomeapp.config.IndoBank;
import id.co.okhome.okhomeapp.lib.CommonCallback;
import id.co.okhome.okhomeapp.lib.OkHomeActivityParent;
import id.co.okhome.okhomeapp.lib.OkhomeException;
import id.co.okhome.okhomeapp.lib.ProgressDialogController;
import id.co.okhome.okhomeapp.lib.SimpleTabManager;
import id.co.okhome.okhomeapp.lib.Util;
import id.co.okhome.okhomeapp.lib.dialog.DialogController;
import id.co.okhome.okhomeapp.lib.dialog.ViewDialog;
import id.co.okhome.okhomeapp.lib.retrofit.RetrofitCallback;
import id.co.okhome.okhomeapp.model.VirtualAccountResultModel;
import id.co.okhome.okhomeapp.restclient.RestClient;
import id.co.okhome.okhomeapp.view.dialog.CommonListDialog;

public class PaymentActivity extends OkHomeActivityParent {

    @BindView(R.id.actPayment_etCardNo1)        EditText etCardNo1;
    @BindView(R.id.actPayment_etCardNo2)        EditText etCardNo2;
    @BindView(R.id.actPayment_etCardNo3)        EditText etCardNo3;
    @BindView(R.id.actPayment_etCardNo4)        EditText etCardNo4;

    @BindView(R.id.actPayment_vTabCreditCard)   View vTabCreditCard;
    @BindView(R.id.actPayment_vTabBankTransfer) View vTabTransfer;

    @BindView(R.id.actPayment_vgBank)           View vgBank;
    @BindView(R.id.actPayment_vgCreditCard)     View vgCreditCard;

    @BindView(R.id.actPayment_ivChkBank)        ImageView tvChkBank;
    @BindView(R.id.actPayment_ivChkCredit)      ImageView tvChkCreditCard;

    @BindView(R.id.actPayment_tvBank)           TextView tvBank;
    @BindView(R.id.actPayment_ivCardTypeMaster) ImageView ivCardTypeMaster;
    @BindView(R.id.actPayment_ivCardTypeVisa)   ImageView ivCardTypeVisa;

    @BindView(R.id.actPayment_tvExpiryMM)       TextView tvExpiryMonth;
    @BindView(R.id.actPayment_tvExpiryYYYY)     TextView tvExpiryYear;
    @BindView(R.id.actPayment_etCVV)            EditText etCvv;

    @BindView(R.id.actPayment_tvOrder)          TextView tvOrder;
    @BindView(R.id.actPayment_vCardLoading)     View vCardLoading;

    @BindView(R.id.actPayment_tvOrderName)      TextView tvOrderName;
    @BindView(R.id.actPayment_tvPrice)        TextView tvPrice;
    SimpleTabManager tabManager;

    int countChkKoreaCard = 0;
    int isKoreaCard = -1;


    int paramTotalPrice = 0;
    String paramOrderName = "";
    String paramOrderNo = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        ButterKnife.bind(this);

        paramOrderNo = getIntent().getStringExtra("orderNo");
        paramOrderName = getIntent().getStringExtra("orderName");
        paramTotalPrice = getIntent().getIntExtra("totalPrice", 0);

        if(paramOrderName == null){
            Util.showToast(this, "다시 시도해주세요! 필수인자가 없음!");
            finish();
            return;
        }

        tvPrice.setText(Util.getMoneyString(paramTotalPrice, '.') + " Rp");
        tvOrderName.setText(paramOrderName);

        new CreditCardTextWatcher(etCardNo1, etCardNo1, etCardNo2, CreditCardTextWatcher.TYPE_FIRST).match();
        new CreditCardTextWatcher(etCardNo2, etCardNo1, etCardNo3, CreditCardTextWatcher.TYPE_NORMAL).match();
        new CreditCardTextWatcher(etCardNo3, etCardNo2, etCardNo4, CreditCardTextWatcher.TYPE_NORMAL).match();
        new CreditCardTextWatcher(etCardNo4, etCardNo3, etCardNo4, CreditCardTextWatcher.TYPE_LAST).match();

        tabManager = new SimpleTabManager(this);
        tabManager.setTabListener(new SimpleTabManager.SimpleTabListener() {
            @Override
            public void onTabOn(int pos, View v) {
                vgBank.setVisibility(View.GONE);
                vgCreditCard.setVisibility(View.GONE);
                tvChkCreditCard.setImageResource(R.drawable.ic_check_not_deep);
                tvChkBank.setImageResource(R.drawable.ic_check_not);
                //탭처리
                if(v == vTabCreditCard){
                    vgCreditCard.setVisibility(View.VISIBLE);
                    tvChkCreditCard.setImageResource(R.drawable.ic_checked);
                }else{
                    vgBank.setVisibility(View.VISIBLE);
                    tvChkBank.setImageResource(R.drawable.ic_checked);
                }
            }

            @Override
            public void onTabOff(int pos, View v) {
                ;
            }
        });

        vCardLoading.setVisibility(View.INVISIBLE);
        tabManager.addTab(vTabCreditCard);
        tabManager.addTab(vTabTransfer);
        tabManager.onItemClick(vTabTransfer);


        setCardType(null);
    }

    //이미지 카드타입설정
    private void setCardType(Boolean isVisa){
        ivCardTypeMaster.setImageResource(R.drawable.ic_master2_gray);
        ivCardTypeVisa.setImageResource(R.drawable.ic_visa2_gray);

        ivCardTypeMaster.setAlpha(0.8f);
        ivCardTypeVisa.setAlpha(0.8f);
        if(isVisa == null){
            ;
        }
        else if(isVisa){
            ivCardTypeVisa.setImageResource(R.drawable.ic_visa2);
            ivCardTypeVisa.setAlpha(1f);
        }else{
            ivCardTypeMaster.setImageResource(R.drawable.ic_master2);
            ivCardTypeMaster.setAlpha(1f);
        }
    }

    /**카드 유효성 검사*/
    private boolean isValidCardNumber(String cardNumber){


        String firstChar = cardNumber.substring(0, 1);
        if(firstChar.equals("4")){
            return Pattern.matches("4\\d{12}(\\d{3})?", cardNumber);
        }else if(firstChar.equals("5")){
            return Pattern.matches("5[1-5]\\d{14}", cardNumber);
        }else{
            return false;
        }
    }

    private void chkIsKoreaCard(String cardNumber, final CommonCallback commonCallback){
        vCardLoading.setVisibility(View.VISIBLE);

        String bin = cardNumber.substring(0, 6);
        countChkKoreaCard++;
        final int pos = countChkKoreaCard;
        isKoreaCard = -1;
        RestClient.getCommonClient().isKoreaCard(bin).enqueue(new RetrofitCallback<Boolean>() {

            @Override
            public void onFinish() {
                super.onFinish();
                if(pos == countChkKoreaCard){
                    vCardLoading.setVisibility(View.INVISIBLE);
                }

            }

            @Override
            public void onSuccess(Boolean result) {

                if(pos == countChkKoreaCard){
                    if(result){
                        //같으면 한국카드안된다고 표시
                        isKoreaCard = 1; //맞음
                    }else{
                        isKoreaCard = 0; //아님
                    }

                    if(commonCallback != null){
                        commonCallback.onWorkDone(Util.makeMap("isKoreaCard", isKoreaCard == 1 ? true : false));
                    }
                }else{
                    //버린다.
                }

            }
        });
    }

    @OnClick(R.id.actPayment_vbtnExpiryMM)
    public void onBtnExpiryMM(View v){
        List listMonth = new ArrayList();
        for(int i = 1; i <= 31; i++){
            listMonth.add(i+"");
        }


        DialogController.showBottomDialog(this, Util.getScreenHeight(this) / 2, new CommonListDialog("신용카드 유효기간 월을 입력하세요.", listMonth, new ViewDialog.DialogCommonCallback() {
            @Override
            public void onCallback(Object dialog, Map<String, Object> params) {
                String item = (String)params.get(CommonListDialog.TAG_ITEM);
                String var = Util.fillupWith2Zero(item);

                tvExpiryMonth.setText(var);
                tvExpiryMonth.setTag(var);

            }
        }));
    }

    @OnClick(R.id.actPayment_vbtnExpiryYYYY)
    public void onBtnExpiryYYYY(View v){
        List listYear = new ArrayList();

        for(int i = Util.getCurrentYear(); i <= Util.getCurrentYear() + 20; i++){
            listYear.add(i+"");
        }
        DialogController.showBottomDialog(this, Util.getScreenHeight(this) / 2, new CommonListDialog("신용카드 유효기간 년도를 입력하세요.", listYear, new ViewDialog.DialogCommonCallback() {
            @Override
            public void onCallback(Object dialog, Map<String, Object> params) {
                String item = (String)params.get(CommonListDialog.TAG_ITEM);
                String var = item.substring(2);
                tvExpiryYear.setText(item);
                tvExpiryYear.setTag(var);
            }
        }));
    }

    @OnClick(R.id.actPayment_vbtnOrder)
    public void onBtnOrder(View v){
        if(tabManager.getPos() == 0){
            orderWithCreditCard();
        }else{
            orderWithBankTrasfer();
        }

    }

    //신용카드결제
    private void orderWithCreditCard(){

        String cardNumber = getCardNumber();
        String expiryMonth = tvExpiryMonth.getTag().toString();
        String expiryYear = tvExpiryYear.getTag().toString();
        String cvv = etCvv.getText().toString();

        try{
            if(!isValidCardNumber(cardNumber)){
                throw new OkhomeException("올바르지 않은 카드번호입니다.");
            }

            if(isKoreaCard == -1){
                throw new OkhomeException("카드번호 유효성 체크중입니다");
            }

            if(isKoreaCard == 1){
                throw new OkhomeException("죄송합니다. 한국 카드는 은행규정에 따라 모바일에서 결제되지 않습니다.");
            }

            if(expiryMonth.length() <= 0){
                throw new OkhomeException("카드 유효기간(월)을 입력하세요.");
            }

            if(expiryYear.length() <= 0){
                throw new OkhomeException("카드 유효기간(년)을 입력하세요.");
            }

            if(cvv.length() != 3){
                throw new OkhomeException("CVV 코드를 입력하세요.");
            }

            //카드번호 검증..


        }catch(OkhomeException e){
            DialogController.showAlertDialog(this, "Okhome", e.getMessage());
            return;
        }

        startActivityForResult(new Intent(this, NicePaymentWebActivity.class)
                .putExtra("expiryMonth", expiryMonth)
                .putExtra("expiryYear", expiryYear)
                .putExtra("cardNumber", cardNumber)
                .putExtra("cvv", cvv)
                , 1);
    }

    //현금결제
    private void orderWithBankTrasfer(){
        final String bankCode = (String)tvBank.getTag();
        final int price = paramTotalPrice;
        if(bankCode == null || bankCode.length() <= 0){
            Util.showToast(this, "이체할 은행을 선택하세요");
            return;
        }

        final int id = ProgressDialogController.show(this, "잠시만 기다리세요!");
        RestClient.getNicepayClient().issueVirtualAccount(paramOrderNo, CurrentUserInfo.getId(this), paramTotalPrice, bankCode).enqueue(new RetrofitCallback<VirtualAccountResultModel>() {
            @Override
            public void onFinish() {
                super.onFinish();
                ProgressDialogController.dismiss(id);
            }

            @Override
            public void onSuccess(VirtualAccountResultModel result) {
//                paramOrderNo = getIntent().getStringExtra("orderNo");
//                paramOrderName = getIntent().getStringExtra("orderName");
//                paramTotalPrice = getIntent().getIntExtra("totalPrice", 0);

                OkHomeActivityParent.finishAllActivitiesWithout(PaymentActivity.this);
                startActivity(new Intent(PaymentActivity.this, MainActivity.class));
                startActivity(new Intent(PaymentActivity.this, PaymentVAConfirmActivity.class)
                        .putExtra("orderNo", paramOrderNo)
                        .putExtra("orderName", paramOrderName)
                        .putExtra("totalPrice", paramTotalPrice)
                        .putExtra("virtualAccountResultModel", result));
                finish();
            }
        });
    }

    private String getCardNumber(){
        String cardNumber = etCardNo1.getText().toString() + etCardNo2.getText().toString() + etCardNo3.getText().toString() + etCardNo4.getText().toString();
        return cardNumber;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //카드
        if(requestCode == 1 && resultCode == RESULT_OK){
            String result = data.getStringExtra(NicePaymentWebActivity.RESULTKEY_RESULTCODE);

            if(result.equals("0000")){
                //아래는 성공결과임. 체크하자.
                //{"resultCd":"0000","amount":"1000","authNo":"326200","referenceNo":"OKHOME_1482096329986","transTm":"042559","payMethod":"01","tXid":"OKHOME201601201612190425593354","description":"any contents will be here","callbackUrl":"http://192.168.0.104:3111/nicepay/credit_card/callback/OKHOME_1482096329986_callbackurl_card","transDt":"20161219","resultMsg":"SUCCESS"}
                //결제성공!
                DialogController.showAlertDialog(this, "결제성공", "결제가 완료되었습니다.", true);
                setResult(RESULT_OK);
                finish();
            }

            else{
                String msg = data.getStringExtra(NicePaymentWebActivity.RESULTKEY_RESULTMSG);
                DialogController.showAlertDialog(this, "결제실패", msg, true);
            }

        }
        //계좌이체
        else if(requestCode == 2){

        }
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////
    //여기서 부터 은행처리
    /////////////////////////////////////////////////////////////////////////////////////////////////////

    @OnClick(R.id.actPayment_vbtnBank)
    public void onBtnBank(View v){
        final String[] arrBank = new String[]{"Mandiri", "BNI", "Maybank", "Permata", "Hana Bank"};
        final String[] arrBankCode = new String[]{
                IndoBank.MANDIRI.bankCode, IndoBank.BNI.bankCode
                , IndoBank.MAY.bankCode, IndoBank.PERMATA.bankCode, IndoBank.HANA.bankCode};
        final Map<String, String> mapBanks = new HashMap<>();

        for(int i = 0 ; i < arrBank.length; i++){
            mapBanks.put(arrBank[i], arrBankCode[i]);
        }


        DialogController.showBottomDialog(this, Util.getScreenHeight(this) / 2, new CommonListDialog("Bank", arrBank, new ViewDialog.DialogCommonCallback() {
            @Override
            public void onCallback(Object dialog, Map<String, Object> params) {
                String item = (String)params.get("ITEM");
                String bankCode = mapBanks.get(item);

                tvBank.setText(item);
                tvBank.setTag(bankCode);
            }
        }));
    }


    @OnClick(R.id.common_vbtnBack)
    public void onBack(View v){
        finish();
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////
    //기타 클래스
    /////////////////////////////////////////////////////////////////////////////////////////////////////

    class CreditCardTextWatcher implements TextWatcher, View.OnKeyListener{

        static final int TYPE_NORMAL = 2;
        static final int TYPE_FIRST = 1;
        static final int TYPE_LAST = 3;

        int type;
        EditText etTarget, etPrev, etNext;
        String currentText = "";
        int beforeLength, currentLength;
        public CreditCardTextWatcher(EditText etTarget, EditText etPrev, EditText etNext, int type) {
            this.etTarget = etTarget;
            this.etPrev = etPrev;
            this.etNext = etNext;
            this.type = type;
        }

        public void match(){
            etTarget.setOnKeyListener(this);
            etTarget.addTextChangedListener(this);
        }

        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {

            //같으면 끝
            if(etPrev == etTarget){
                return false;
            }


            if(keyCode == KeyEvent.KEYCODE_DEL){
                //텍스트 바뀌고 난다음에 up호출됨
                if(event.getAction() == KeyEvent.ACTION_UP){
                    Util.Log(beforeLength + " " + currentLength);
                    currentLength = etTarget.getText().length();

                    if(beforeLength == currentLength && currentLength == 0){
                        //앞단으로 포커스 이동
                        //앞단 텍스트 맨끝삭제
                        etPrev.setText(etPrev.getText().subSequence(0, 3));
                        etPrev.requestFocus();
                        etPrev.setSelection(etPrev.getText().length());

                    }
                }
                //텍스트 바뀌기 전down호출
                else if(event.getAction() == KeyEvent.ACTION_DOWN){
                    beforeLength = etTarget.getText().length();
                }
            }else{
                //입력 완료되었는지 체크
                if(event.getAction() == KeyEvent.ACTION_UP){
                    //16자리 입력완료됫니
                    String cardNumber = getCardNumber();
                    if(cardNumber.length() == 16){
                        Util.setSoftKeyboardVisiblity(etTarget, false);
                        if(!isValidCardNumber(cardNumber)){
                            DialogController.showAlertDialog(PaymentActivity.this, "Okhome", "잘못된 카드 번호입니다. 다시 확인해주세요", true);
                        }

                        chkIsKoreaCard(cardNumber, new CommonCallback() {
                            @Override
                            public void onWorkDone(Map<String, Object> mapResult) {
                                boolean isKoreaCard = (boolean)mapResult.get("isKoreaCard");
                                if(isKoreaCard){
                                    DialogController.showAlertDialog(PaymentActivity.this, "Okhome", "죄송합니다. 한국에서 발행한 카드는 은행규정에 따라 모바일에서 결제되지 않습니다.");
                                }
                            }
                        });
                    }
                }
            }


            return false;
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            currentText = s.toString();
            if(s.length() == 4){
                if(type == TYPE_FIRST){
                    //첫번째꺼
                    String firstChar = s.toString().substring(0, 1);
                    if(firstChar.equals("4")){  //비자
                        setCardType(true);
                        etNext.requestFocus();
                    }else if(firstChar.equals("5")){    //마스터
                        setCardType(false);
                        etNext.requestFocus();
                    }else{
                        DialogController.showAlertDialog(PaymentActivity.this, "Okhome", "죄송합니다! Visa, MasterCard만 가능합니다!", true);
                        etCardNo1.setText("");
                        //can not..
                    }

                }else if(type == TYPE_LAST){
                    //
                }else{
                    etNext.requestFocus();
                }
            }else{
                if(type == TYPE_FIRST){
                    setCardType(null);
                }
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void afterTextChanged(Editable s) {}
    }
}
