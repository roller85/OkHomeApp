package id.co.okhome.okhomeapp.view.dialog;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.okhome.okhomeapp.R;
import id.co.okhome.okhomeapp.lib.OkhomeException;
import id.co.okhome.okhomeapp.lib.ProgressDialogController;
import id.co.okhome.okhomeapp.lib.Util;
import id.co.okhome.okhomeapp.lib.dialog.DialogController;
import id.co.okhome.okhomeapp.lib.dialog.ViewDialog;
import id.co.okhome.okhomeapp.lib.retrofit.RetrofitCallback;
import id.co.okhome.okhomeapp.lib.retrofit.restmodel.ErrorModel;
import id.co.okhome.okhomeapp.model.AddressItemModel;
import id.co.okhome.okhomeapp.restclient.RestClient;

/**
 * Created by josongmin on 2016-08-09.
 */

public class HouseAddressDialog extends ViewDialog{

    @BindView(R.id.dialogChangeAddress_tvAddress1)      TextView tvAddress1;
    @BindView(R.id.dialogChangeAddress_tvAddress2)      TextView tvAddress2;
    @BindView(R.id.dialogChangeAddress_tvAddress3)      TextView tvAddress3;
    @BindView(R.id.dialogChangeAddress_etAddressLine)   EditText etAddress;

    DialogCommonCallback callback;
    String homeId;
    boolean autoUpdate = true;

    public HouseAddressDialog(String homeId, boolean autoUpdate, DialogCommonCallback callback) {
        this.homeId = homeId;
        this.callback = callback;
        this.autoUpdate = autoUpdate;
    }

    @Override
    public View getView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.dialog_change_address, null);
    }

    @Override
    public void onViewCreated() {
        ButterKnife.bind(this, getDecorView());
    }

    //조서세부 선택창 오픈
    private void showAddressItemDialog(String parentId, final TextView targetTextView){
        final int pId = ProgressDialogController.show(getContext());
        RestClient.getCommonClient().getAddress(parentId).enqueue(new RetrofitCallback<List<AddressItemModel>>() {

            @Override
            public void onFinish() {
                ProgressDialogController.dismiss(pId);
            }

            @Override
            public void onSuccess(List<AddressItemModel> result) {

                DialogController.showCenterDialog(getContext(), new CommonAddressItemDialog("City", result, new CommonAddressItemDialog.OnAddressClick() {
                    @Override
                    public void onAddressClick(AddressItemModel addressItemModel) {

                        if(targetTextView == tvAddress1 && !addressItemModel.value.equals(tvAddress1.getText().toString())){
                            tvAddress2.setText("");
                            tvAddress2.setTag("");

                            tvAddress3.setText("");
                            tvAddress3.setTag("");

                        }else if(targetTextView == tvAddress2){
                            if(addressItemModel.value.equals(tvAddress2.getText().toString())){
                                ;
                                Util.Log("옴");
                            }else{
                                Util.Log("왜안옴");
                                tvAddress3.setText("");
                                tvAddress3.setTag("");
                            }
                        }

                        targetTextView.setText(addressItemModel.value);
                        targetTextView.setTag(addressItemModel.id);
                    }
                }));

            }

            @Override
            public void onJodevError(ErrorModel jodevErrorModel) {
                Util.showToast(getContext(), jodevErrorModel.message);
            }
        });
    }

    @OnClick(R.id.dialogChangeAddress_vbtnAddress1)
    public void onAddress1Click(View v){
        showAddressItemDialog("0", tvAddress1);
    }

    @OnClick(R.id.dialogChangeAddress_vbtnAddress2)
    public void onAddress2Click(View v){
        String parentId = tvAddress1.getTag() != null ? (String)tvAddress1.getTag() : "";
        if(parentId.equals("")) return;

        showAddressItemDialog(parentId, tvAddress2);
    }

    @OnClick(R.id.dialogChangeAddress_vbtnAddress3)
    public void onAddress3Click(View v){
        String parentId = tvAddress2.getTag() != null ? (String)tvAddress2.getTag() : "";
        if(parentId.equals("")) return;

        showAddressItemDialog(parentId, tvAddress3);
    }

    //서버로 업데이트
    private void update(){
        final String address1 = tvAddress1.getText().toString();
        final String address2 = tvAddress2.getText().toString();
        final String address3 = tvAddress3.getText().toString();
        final String address = etAddress.getText().toString();


        final int pId = ProgressDialogController.show(getContext());
        RestClient.getHomeClient().updateHomeAddress(homeId, address1, address2, address3, address).enqueue(new RetrofitCallback<String>() {

            @Override
            public void onFinish() {
                ProgressDialogController.dismiss(pId);
            }

            @Override
            public void onSuccess(String result) {
                finish(address1, address2, address3, address);
            }

            @Override
            public void onJodevError(ErrorModel jodevErrorModel) {
                Util.showToast(getContext(), jodevErrorModel.message);
            }
        });

    }

    public void finish(String address1, String address2, String address3, String address4){
        callback.onCallback(getDialog(), Util.makeMap("SUCCESS", true
                , "address1", address1
                , "address2", address2
                , "address3", address3
                , "address4", address4
        ));

        dismiss();
    }

    @OnClick(R.id.dialogCommon_vbtnConfirm)
    public void onConfirmClick(View v){

        String address1 = tvAddress1.getText().toString();
        String address2 = tvAddress2.getText().toString();
        String address3 = tvAddress3.getText().toString();
        String address = etAddress.getText().toString();

        try{
            if(address1.equals("") || address2.equals("") || address3.equals("") || address.equals("")) throw new OkhomeException("Check address");
        }catch(OkhomeException e){
            Util.showToast(getContext(), e.message);
            return;
        }

        if(autoUpdate){
            update();
        }else{
            finish(address1, address2, address3, address);
        }
    }

    @OnClick(R.id.dialogCommon_vbtnCancel)
    public void onCancelClick(View v){
        dismiss();
    }
}
