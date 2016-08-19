package id.co.okhome.okhomeapp.view.dialog;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.okhome.okhomeapp.R;
import id.co.okhome.okhomeapp.lib.dialog.DialogController;
import id.co.okhome.okhomeapp.lib.dialog.ViewDialog;

/**
 * Created by josongmin on 2016-08-09.
 */

public class HouseAddressDialog extends ViewDialog{


    @BindView(R.id.dialogChangeAddress_tvState)
    TextView tvState;

    DialogCommonCallback callback;

    public HouseAddressDialog(DialogCommonCallback callback) {
        this.callback = callback;
    }

    @Override
    public View getView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.dialog_change_address, null);
    }

    @Override
    public void onViewCreated() {
        ButterKnife.bind(this, getDecorView());

    }

    @OnClick(R.id.dialogChangeAddress_vbtnState)
    public void onStateClick(View v){
        List list = new ArrayList();
        Collections.addAll(list, new String[]{"Jakarta", "Bekasi", "Montong", "Bothon", "Mereyho", "Etc"});
        DialogController.showCenterDialog(getContext(), new CommonListDialog("Province", list, new DialogCommonCallback() {
            @Override
            public void onCallback(Object dialog, Map<String, Object> params) {
                tvState.setText((String)params.get("ITEM"));
            }
        }));
    }

    @OnClick(R.id.dialogCommon_vbtnConfirm)
    public void onConfirmClick(View v){
        dismiss();
    }

    @OnClick(R.id.dialogCommon_vbtnCancel)
    public void onCancelClick(View v){
        dismiss();
    }
}
