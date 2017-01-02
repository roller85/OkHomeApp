package id.co.okhome.okhomeapp.view.dialog;

import android.view.LayoutInflater;
import android.view.View;
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

public class CommonTextDialog extends ViewDialog{

    @BindView(R.id.dialogCommonText_tvContent)
    TextView tvContent;

    @BindView(R.id.dialogCommonText_tvTitle)
    TextView tvTitle;

    @BindView(R.id.dialogCommon_vbtnCancel)
    TextView tvBtnCancel;

    @BindView(R.id.dialogCommon_vbtnConfirm)
    TextView tvBtnOk;

    DialogCommonCallback callback;
    String title, content;
    boolean showJustOkBtn = false;

    public CommonTextDialog(String title, String content, boolean showJustOkBtn, DialogCommonCallback callback) {
        this.callback = callback;
        this.title = title;
        this.content = content;
        this.showJustOkBtn = showJustOkBtn;
    }

    public CommonTextDialog(String title, String content, DialogCommonCallback callback) {
        this.callback = callback;
        this.title = title;
        this.content = content;
    }

    public CommonTextDialog(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void setButtonText(String confirm, String cancel){
        tvBtnOk.setText(confirm);
        tvBtnCancel.setText(cancel);
    }

    @Override
    public View getView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.dialog_common_text, null);
    }

    @Override
    public void onViewCreated() {
        ButterKnife.bind(this, getDecorView());

        tvTitle.setText(title);
        tvContent.setText(content);

        if(showJustOkBtn){
            tvBtnCancel.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.dialogCommon_vbtnConfirm)
    public void onConfirmClick(View v){
        if(callback!= null)
            callback.onCallback(getDialog(), Util.makeMap("ONCLICK", "OK"));
        dismiss();
    }

    @OnClick(R.id.dialogCommon_vbtnCancel)
    public void onCancelClick(View v){
        if(callback != null)
            callback.onCallback(getDialog(), Util.makeMap("ONCLICK", "CANCEL"));
        dismiss();
    }

}
