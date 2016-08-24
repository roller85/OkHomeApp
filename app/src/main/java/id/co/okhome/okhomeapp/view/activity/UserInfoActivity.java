package id.co.okhome.okhomeapp.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.okhome.okhomeapp.R;
import id.co.okhome.okhomeapp.lib.OkHomeActivityParent;
import id.co.okhome.okhomeapp.lib.OkHomeUtil;
import id.co.okhome.okhomeapp.lib.dialog.DialogController;
import id.co.okhome.okhomeapp.lib.dialog.ViewDialog;
import id.co.okhome.okhomeapp.view.dialog.ChangePhoneNumberDialog;
import id.co.okhome.okhomeapp.view.dialog.CommonInputDialog;

public class UserInfoActivity extends OkHomeActivityParent {

    @BindView(R.id.actUserInfo_tvPhone)
    TextView tvPhone;

    @BindView(R.id.actUserInfo_tvEmail)
    TextView tvEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfo);

        ButterKnife.bind(this);
        init();
    }

    private void init(){
        OkHomeUtil.setBackbtnListener(this);
    }


    @OnClick(R.id.actUserInfo_vbtnEmail)
    public void onEmailClick(View v){
        DialogController.showCenterDialog(this, new CommonInputDialog(CommonInputDialog.TITLE_EMAIL, CommonInputDialog.SUBTITLE_EMAIL, "", new ViewDialog.DialogCommonCallback() {
            @Override
            public void onCallback(Object dialog, Map<String, Object> params) {

            }
        }));
    }

    @OnClick(R.id.actUserInfo_vbtnPhone)
    public void onPhoneClick(View v){
        DialogController.showCenterDialog(this, new ChangePhoneNumberDialog(new ViewDialog.DialogCommonCallback() {
            @Override
            public void onCallback(Object dialog, Map<String, Object> params) {

            }
        }));
    }

}
