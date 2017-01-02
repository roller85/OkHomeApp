package id.co.okhome.okhomeapp.view.dialog;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.okhome.okhomeapp.R;
import id.co.okhome.okhomeapp.config.CurrentUserInfo;
import id.co.okhome.okhomeapp.lib.ProgressDialogController;
import id.co.okhome.okhomeapp.lib.SmsReceiver;
import id.co.okhome.okhomeapp.lib.Util;
import id.co.okhome.okhomeapp.lib.dialog.ViewDialog;
import id.co.okhome.okhomeapp.lib.retrofit.RetrofitCallback;
import id.co.okhome.okhomeapp.lib.retrofit.restmodel.ErrorModel;
import id.co.okhome.okhomeapp.restclient.RestClient;

/**
 * Created by josongmin on 2016-08-09.
 */

public class ChangePhoneNumberDialog extends ViewDialog{


    @BindView(R.id.dialogChangePhoneNumber_etInputVerificationCode)         EditText etInputVerificationCode;
    @BindView(R.id.dialogChangePhoneNumber_etInputPhoneNumber)              EditText etInputPhoneNumber;
    @BindView(R.id.dialogChangePhoneNumber_vgCertCode)                      ViewGroup vgCertCode;
    @BindView(R.id.dialogChangePhoneNumber_vCertDisable)                    View vCertDisable;
    @BindView(R.id.dialogCommon_vbtnConfirm)                                TextView tvBtnConfirm;

    DialogCommonCallback callback;
    String phoneNumberProgressed = "";
    String codeProgressed = "";
    String phone;
    Activity activity;
    BroadcastReceiver smsReceiver;

    public ChangePhoneNumberDialog(Activity activity, String phone, DialogCommonCallback callback) {
        this.activity = activity;
        this.callback = callback;
        this.phone = phone;
    }

    @Override
    public View getView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.dialog_change_phonenumber, null);
    }

    @Override
    public void show() {
        super.show();

        registerSmsReceiver();
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }


    //sms인증 리시버 등록
    private void registerSmsReceiver(){
        new SmsReceiver(activity).init(new SmsReceiver.OnSmsReceivedListener() {
            @Override
            public void onSmsReceive(String message) {
                //원본
            }

            @Override
            public void onOkhomeVerificationSmsReceive(String message) {
                etInputVerificationCode.setText(message);
            }
        });
    }

    @Override
    public void onViewCreated() {
        ButterKnife.bind(this, getDecorView());

        tvBtnConfirm.setVisibility(View.GONE);
        vgCertCode.setVisibility(View.GONE);
    }

    @OnClick(R.id.dialogChangePhoneNumber_vbtnSendSms)
    public void onBtnSendSms(View v){
        String phone = etInputPhoneNumber.getText().toString();
        if(phone.length() < 5){
            Util.showToast(getContext(), "Check your phone number");
            return;
        }
        phoneNumberProgressed = phone;
        final int pId =ProgressDialogController.show(getContext());
        RestClient.getCertificationClient().sendCertCode(phone, "Y").enqueue(new RetrofitCallback<String>() {

            @Override
            public void onFinish() {
                ProgressDialogController.dismiss(pId);
            }

            @Override
            public void onSuccess(String result) {
                Util.showToast(getContext(), "잠시 후 인증 문자가 도착합니다.");
                tvBtnConfirm.setVisibility(View.VISIBLE);
                vgCertCode.setVisibility(View.VISIBLE);
            }

            @Override
            public void onJodevError(ErrorModel jodevErrorModel) {
                Util.showToast(getContext(), jodevErrorModel.message);
            }
        });
    }

    private void onConfirm(){
        final int pId = ProgressDialogController.show(getContext());

        //업데이트 한다
        RestClient.getUserRestClient().updatePhone(CurrentUserInfo.getId(getContext()), phoneNumberProgressed).enqueue(new RetrofitCallback<String>() {

            @Override
            public void onFinish() {
                ProgressDialogController.dismiss(pId);
            }

            @Override
            public void onSuccess(String result) {
                callback.onCallback(getDialog(), Util.makeMap("ONCLICK", "OK"));
                dismiss();
            }

            @Override
            public void onJodevError(ErrorModel jodevErrorModel) {
                Util.showToast(getContext(), jodevErrorModel.message);
            }
        });
    }

    @OnClick(R.id.dialogCommon_vbtnConfirm)
    public void onConfirmClick(View v){

        final String code = etInputVerificationCode.getText().toString();
        if(code.length() < 4 ) return;

        final int pId = ProgressDialogController.show(getContext());

        RestClient.getCertificationClient().chkValidCode(phoneNumberProgressed, code).enqueue(new RetrofitCallback<Boolean>() {
            @Override
            public void onFinish() {
                ProgressDialogController.dismiss(pId);
            }

            @Override
            public void onSuccess(Boolean result) {
                //성공
                vCertDisable.setVisibility(View.VISIBLE);
                vCertDisable.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ;
                    }
                });

                Util.hideKeyboard((Activity)getContext());
                onConfirm();
            }

            @Override
            public void onJodevError(ErrorModel jodevErrorModel) {
                Util.showToast(getContext(), jodevErrorModel.message);
            }
        });
    }

    @OnClick(R.id.dialogCommon_vbtnCancel)
    public void onCancelClick(View v){
        callback.onCallback(getDialog(), Util.makeMap("ONCLICK", "CANCEL"));
        dismiss();
    }

}
