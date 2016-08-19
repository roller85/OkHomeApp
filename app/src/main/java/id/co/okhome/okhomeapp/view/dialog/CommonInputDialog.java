package id.co.okhome.okhomeapp.view.dialog;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.okhome.okhomeapp.R;
import id.co.okhome.okhomeapp.lib.Util;
import id.co.okhome.okhomeapp.lib.dialog.ViewDialog;

/**
 * Created by josongmin on 2016-08-09.
 */

public class CommonInputDialog extends ViewDialog{

    public final static String TITLE_EMAIL = "Input your phone email";
    public final static String SUBTITLE_EMAIL = "What is your email";


    @BindView(R.id.dialogCommonInputbox_etInput)        EditText etInput;
    @BindView(R.id.dialogCommonInputbox_tvSubTitle)     TextView tvSubTitle;
    @BindView(R.id.dialogCommonInputbox_tvTitle)        TextView tvTitle;

    String title, subTitle, content;
    DialogCommonCallback callback;
    public CommonInputDialog(String title, String subTitle, String content, DialogCommonCallback callback) {
        this.title = title; this.subTitle = subTitle; this.content = content; this.callback = callback;
    }

    @Override
    public View getView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.dialog_common_inputbox, null);
    }

    @Override
    public void onViewCreated() {
        ButterKnife.bind(this, getDecorView());

        tvSubTitle.setText(subTitle);
        tvTitle.setText(title);
        etInput.setText(content);

        Util.setSoftKeyboardVisiblity(etInput, true);
    }


    @OnClick(R.id.dialogCommon_vbtnConfirm)
    public void onConfirmClick(View v){
        callback.onCallback(getDialog(), Util.makeMap("ONCLICK", "OK", "TEXT", etInput.getText().toString()));
        Util.setSoftKeyboardVisiblity(etInput, false);
        dismiss();
    }

    @OnClick(R.id.dialogCommon_vbtnCancel)
    public void onCancelClick(View v){
        callback.onCallback(getDialog(), Util.makeMap("ONCLICK", "CANCEL"));
        Util.setSoftKeyboardVisiblity(etInput, false);
        dismiss();
    }
}
