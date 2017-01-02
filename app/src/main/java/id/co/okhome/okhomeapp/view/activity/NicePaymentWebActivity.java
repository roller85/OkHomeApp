package id.co.okhome.okhomeapp.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.okhome.okhomeapp.R;
import id.co.okhome.okhomeapp.config.CurrentUserInfo;
import id.co.okhome.okhomeapp.config.Variables;
import id.co.okhome.okhomeapp.lib.AnimatedTooltipImageController;
import id.co.okhome.okhomeapp.lib.OkHomeActivityParent;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NicePaymentWebActivity extends OkHomeActivityParent {

    public final static String RESULTKEY_RESULTCODE = "resultCd";
    public final static String RESULTKEY_AMOUNT = "amount";
    public final static String RESULTKEY_AUTHNO = "authNo";
    public final static String RESULTKEY_REFERENCENO = "referenceNo";
    public final static String RESULTKEY_PAYMETHOD = "payMethod";
    public final static String RESULTKEY_TXID = "tXid";
    public final static String RESULTKEY_DESCRIPTION = "description";
    public final static String RESULTKEY_CALLBACKURL = "callbackUrl";
    public final static String RESULTKEY_RESULTMSG = "resultMsg";
    public final static String RESULTKEY_TRANSDT = "transDt";

    @BindView(R.id.actNicePayementWeb_wv)           WebView wv;
    @BindView(R.id.actNicePayementWeb_tvResult)     TextView tvResult;
    @BindView(R.id.actNicePayementWeb_vLoading)     View vLoading;
    String secure3dUrl = "";

    AnimatedTooltipImageController loadingImageController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nice_payment_web);

        ButterKnife.bind(this);

        int totalPrice = getIntent().getIntExtra("totalPrice", 0);
        String orderNo = getIntent().getStringExtra("orderNo");
        String orderName = getIntent().getStringExtra("orderName");
        String cardNo = getIntent().getStringExtra("cardNumber");
        String expMonth = getIntent().getStringExtra("expiryMonth");
        String expYear = getIntent().getStringExtra("expiryYear");
        String cvv = getIntent().getStringExtra("cvv");
        String expYymm = expYear + expMonth;
        //http://localhost:3111/niceplay/pay/3dsecure?userId=1&productId=5&cardNo=5272890539151153&expYymm=0622&cvv=146
        //샘플데이터
//        cardNo = "4726470023638919";
//        expYymm = "1911";
//        cvv = "763";

        if(cardNo == null || expYymm == null || cvv == null){
            return;
        }

        secure3dUrl = Variables.APP_BASE_URL + "/nicepay/credit_card/3dsecure"
                + "?orderNo=" + orderNo
                + "&userId=" + CurrentUserInfo.getId(this)
                + "&price=" + totalPrice
                + "&cardNo=" + cardNo
                + "&expYymm=" + expYymm
                + "&cvv=" + cvv;

        initWebView();
        loadWeb();


    }

    //최종결과를 intent로 내 뱉자
    private void doResult(String json){
//        tvResult.setText(json);
        Type tt = new TypeToken<Map<String, String>>(){}.getType();
        Map<String, String> mapResult = new Gson().fromJson(json, tt);

        Intent i = new Intent();
        for (Map.Entry<String, String> entry : mapResult.entrySet()) {
            i.putExtra(entry.getKey(), entry.getValue());
        }

        setResult(RESULT_OK, i);
        finish();
    }

    private void onNotAcceptableCard(){
        Intent i = new Intent();
        i.putExtra(RESULTKEY_RESULTCODE, "-1000");
        i.putExtra(RESULTKEY_RESULTMSG, "신용카드가 아니거나 지원되지 않는 카드입니다.");

        setResult(RESULT_OK, i);
        finish();
    }

    private void openUrlDirect(final String url){
        //자체적으로 열고, 파싱, 리턴
        final Handler handler = new Handler(){
            @Override
            public void dispatchMessage(Message msg) {
                if(msg.what == -1){
                    //error
                }else{
                    //성공. 파싱해서 모델링하고 뱉어내자
                    doResult(msg.obj.toString());
                }
            }
        };
        new Thread(){
            @Override
            public void run() {
                try{
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(url)
                            .build();

                    Response response = client.newCall(request).execute();
                    String result = response.body().string();

                    Message m = new Message();
                    m.what = 0;
                    m.obj = result;
                    handler.sendMessage(m);

                }catch(Exception e){
                    handler.sendEmptyMessage(-1);
                }
            }
        }.start();






    }

    private void initWebView(){
        WebSettings webSettings =  wv.getSettings();
        webSettings.setUseWideViewPort(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true);
        wv.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if(url.contains("credit_card/3dsecure/callback/") && !url.contains("&callbackUrl=")){
                    openUrlDirect(url);
                }
                else if(url.equals("https://www.nicepay.co.id/nicepay/api/return.do")){
                    onNotAcceptableCard();
                    return false;
                }
                else{
                    view.loadUrl(url);
                }
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

            }
        });
        wv.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if(newProgress == 100){
                    vLoading.setVisibility(View.GONE);
                }else{
                    vLoading.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void loadWeb(){

        wv.loadUrl(secure3dUrl);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(RESULT_CANCELED);
    }
}
