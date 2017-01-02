package id.co.okhome.okhomeapp.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.okhome.okhomeapp.R;
import id.co.okhome.okhomeapp.lib.OkHomeActivityParent;
import id.co.okhome.okhomeapp.lib.ProgressDialogController;
import id.co.okhome.okhomeapp.lib.dialog.DialogController;
import id.co.okhome.okhomeapp.lib.dialog.ViewDialog;
import id.co.okhome.okhomeapp.lib.retrofit.RetrofitCallback;
import id.co.okhome.okhomeapp.restclient.RestClient;
import id.co.okhome.okhomeapp.view.dialog.CommonTextDialog;

public class FindPasswordByEmailActivity extends OkHomeActivityParent {

    @BindView(R.id.actFPBE_etEmail)     EditText etEmail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_findpassword_by_email);
        ButterKnife.bind(this);

    }

    @OnClick(R.id.actFPBE_vbtnSendEmail)
    public void onSendEmail(View v){
        String email = etEmail.getText().toString();

        final int pid = ProgressDialogController.show(this);
        RestClient.getUserRestClient().requestResettingPassword(email).enqueue(new RetrofitCallback<String>() {
            @Override
            public void onFinish() {
                ProgressDialogController.dismiss(pid);
            }

            @Override
            public void onSuccess(String result) {
                //성공하면 메세지.
                DialogController.showCenterDialog(FindPasswordByEmailActivity.this, new CommonTextDialog("Complete", "To continue changing your password, Check your email", true, new ViewDialog.DialogCommonCallback() {
                    @Override
                    public void onCallback(Object dialog, Map<String, Object> params) {
                        if(params.get("ONCLICK").equals("OK")){
                            finish();
                        }
                    }
                }));
            }
        });
    }


}
