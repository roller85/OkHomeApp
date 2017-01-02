package id.co.okhome.okhomeapp.view.dialog;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.okhome.okhomeapp.R;
import id.co.okhome.okhomeapp.lib.dialog.ViewDialog;
import id.co.okhome.okhomeapp.view.activity.FindEmailChangePasswordActivity;
import id.co.okhome.okhomeapp.view.activity.FindPasswordByEmailActivity;

/**
 * Created by josongmin on 2016-08-09.
 */

public class ForgotEmailOrPassDialog extends ViewDialog{

    public ForgotEmailOrPassDialog() {

    }

    @Override
    public View getView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.dialog_forgot_email_or_password, null);
    }

    @Override
    public void onViewCreated() {
        ButterKnife.bind(this, getDecorView());

    }


    @OnClick(R.id.dialogForgotEmailOrPassword_vbtnFindEmailByPhone)
    public void onFindEmailByPhone(View v){
        getContext().startActivity(new Intent(getContext(), FindEmailChangePasswordActivity.class));
        dismiss();

    }

    @OnClick(R.id.dialogForgotEmailOrPassword_vbtnFindPasswordByPhone)
    public void onFindPasswordByPhone(View v){
        //이메일입려갗

        getContext().startActivity(new Intent(getContext(), FindEmailChangePasswordActivity.class));
        dismiss();

    }

    @OnClick(R.id.dialogForgotEmailOrPassword_vbtnFindPasswordByEmail)
    public void onFindPasswordByEmail(View v){
        getContext().startActivity(new Intent(getContext(), FindPasswordByEmailActivity.class));

        dismiss();
    }



}
