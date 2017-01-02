package id.co.okhome.okhomeapp.view.activity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.okhome.okhomeapp.R;
import id.co.okhome.okhomeapp.config.Variables;
import id.co.okhome.okhomeapp.lib.OkHomeActivityParent;
import id.co.okhome.okhomeapp.lib.Util;

public class WebpageActivity extends OkHomeActivityParent {

    @BindView(R.id.actWebPage_wv)
    WebView webView;

    @BindView(R.id.actWebPage_vLoading)
    View vLoading;

    @BindView(R.id.actWebPage_tvTitle)
    TextView tvTitle;


    public final static int WEBTYPE_TERMSOFSERVICE      = 1;
    public final static int WEBTYPE_PRIVACYPOLICY       = 2;
    public final static String TAG_WEBTYPE              = "WEBTYPE";
    private int webType = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webpage);
        ButterKnife.bind(this);
        webType = getIntent().getIntExtra("WEBTYPE", WEBTYPE_TERMSOFSERVICE);

        if (webType == 0){
            Util.showToast(this, "webType param null");
            finish();
            return;
        }
        loadWeb();
    }

    private void loadWeb(){
        vLoading.setVisibility(View.VISIBLE);
        String url = "";
        switch(webType){
            case WEBTYPE_PRIVACYPOLICY:
                tvTitle.setText("Pivacy policy");
                url = Variables.APP_BASE_URL + "/w/etc/termsofservice";
                break;

            case WEBTYPE_TERMSOFSERVICE:
                tvTitle.setText("Terms of service");
                url = Variables.APP_BASE_URL + "/w/etc/privacypolicy";
                break;

        }
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                vLoading.setVisibility(View.GONE);
            }
        });
        webView.loadUrl(url);
    }

    @OnClick(R.id.actWebPage_vbtnBack)
    public void onBack(View v){
        finish();

    }
}
