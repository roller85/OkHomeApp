package id.co.okhome.okhomeapp.view.dialog;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.okhome.okhomeapp.R;
import id.co.okhome.okhomeapp.lib.dialog.ViewDialog;

/**
 * Created by josongmin on 2016-08-09.
 */

public class ChangePhoneNumberDialog extends ViewDialog{


    @BindView(R.id.dialogChangePhoneNumber_etInputVerificationCode)        EditText etInputVerificationCode;
    @BindView(R.id.dialogChangePhoneNumber_etInputPhoneNumber)             EditText etInputPhoneNumber;

    DialogCommonCallback callback;

    public ChangePhoneNumberDialog(DialogCommonCallback callback) {
        this.callback = callback;
    }

    @Override
    public View getView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.dialog_change_phonenumber, null);
    }

    @Override
    public void onViewCreated() {
        ButterKnife.bind(this, getDecorView());

    }

}
