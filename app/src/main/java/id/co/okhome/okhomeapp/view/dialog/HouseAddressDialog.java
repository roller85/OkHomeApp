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
import id.co.okhome.okhomeapp.model.HomeModel;
import id.co.okhome.okhomeapp.restclient.RestClient;


public class HouseAddressDialog extends ViewDialog{

    @BindView(R.id.dialogChangeAddress_tvAddress1)          TextView tvAddress1;
    @BindView(R.id.dialogChangeAddress_tvAddress2)          TextView tvAddress2;
    @BindView(R.id.dialogChangeAddress_tvAddress3)          TextView tvAddress3;
    @BindView(R.id.dialogChangeAddress_etAddressLine)       EditText etAddress;

    DialogCommonCallback callback;
    String homeId;
    boolean autoUpdate = true;
    String[] arrAddressSeqs;
    String[] arrAddress;
    HomeModel homeModel;

    public HouseAddressDialog(HomeModel homeModel, boolean autoUpdate, DialogCommonCallback callback) {
        this.homeId = homeModel.id;
        this.homeModel = homeModel;
        this.callback = callback;
        this.autoUpdate = autoUpdate;
        if(homeModel.addressSeqs != null){
            arrAddressSeqs = homeModel.addressSeqs.split(",");
        }

        if(homeModel.address1 != null){
            arrAddress = new String[3];
            arrAddress[0] = homeModel.address1;
            arrAddress[1] = homeModel.address2;
            arrAddress[2] = homeModel.address3;
        }
    }



    @Override
    public View getView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.dialog_change_address, null);
    }

    @Override
    public void show() {
        super.show();

        if(arrAddressSeqs != null){
            initDefaultAddress();
        }else{
            showCityDialog();
        }

    }

    private void initDefaultAddress(){
        tvAddress1.setText(arrAddress[0]);
        tvAddress1.setTag(arrAddressSeqs[0]);

        tvAddress2.setText(arrAddress[1]);
        tvAddress2.setTag(arrAddressSeqs[1]);

        tvAddress3.setText(arrAddress[2]);
        tvAddress3.setTag(arrAddressSeqs[2]);

        etAddress.setText(homeModel.address4);
    }

    @Override
    public void onViewCreated() {
        ButterKnife.bind(this, getDecorView());
    }

    //주소세부 선택창 오픈
    private void showAddressItemDialog(String parentId, final TextView targetTextView, final String addressType, final String title){
        final int pId = ProgressDialogController.show(getContext());
        RestClient.getCommonClient().getAddress(parentId).enqueue(new RetrofitCallback<List<AddressItemModel>>() {

            @Override
            public void onFinish() {
                ProgressDialogController.dismiss(pId);
            }

            @Override
            public void onSuccess(List<AddressItemModel> result) {

                DialogController.showCenterDialog(getContext(), new CommonAddressItemDialog(title, result, new CommonAddressItemDialog.OnAddressClick() {

                    @Override
                    public void onAddressClick(AddressItemModel addressItemModel) {

                        if(targetTextView == tvAddress1 && !addressItemModel.value.equals(tvAddress1.getText().toString())){
                            tvAddress2.setText("");
                            tvAddress2.setTag("");
                            tvAddress3.setText("");
                            tvAddress3.setTag("");

                        }else if(targetTextView == tvAddress2){
                            if(addressItemModel.value.equals(tvAddress2.getText().toString())){
                                Util.Log("옴");

                            }else{
                                Util.Log("왜안옴");
                                tvAddress3.setText("");
                                tvAddress3.setTag("");

                            }

                        }

                        targetTextView.setText(addressItemModel.value);
                        targetTextView.setTag(addressItemModel.id);

                        if(addressType.equals("CITY")){
                            showTownDialog();
                        }else if(addressType.equals("TOWN")){
                            showLocalityDialog();
                        }else{

                        }
                    }
                }));
            }

            @Override
            public void onJodevError(ErrorModel jodevErrorModel) {
                Util.showToast(getContext(), jodevErrorModel.message);
            }

        });
    }

    //첫번째 주소 팝업
    private void showCityDialog(){
        showAddressItemDialog("0", tvAddress1, "CITY", "City");
    }

    //두번째 주소 팝업
    private void showTownDialog(){
        String parentId = tvAddress1.getTag() != null ? (String)tvAddress1.getTag() : "";
        if(parentId.equals("")) return;

        showAddressItemDialog(parentId, tvAddress2, "TOWN", "Town");
    }

    //마지막주소 팝업
    private void showLocalityDialog(){

        String parentId = tvAddress2.getTag() != null ? (String)tvAddress2.getTag() : "";
        if(parentId.equals("")) return;

        showAddressItemDialog(parentId, tvAddress3, "LOCALITY", "Locality");
    }

    @OnClick(R.id.dialogChangeAddress_vbtnAddress1)
    public void onAddress1Click(View v){
        showCityDialog();
    }

    @OnClick(R.id.dialogChangeAddress_vbtnAddress2)
    public void onAddress2Click(View v){
        showTownDialog();
    }

    @OnClick(R.id.dialogChangeAddress_vbtnAddress3)
    public void onAddress3Click(View v){
        showLocalityDialog();
    }

    //서버로 업데이트
    private void update(){
        final String address1 = tvAddress1.getText().toString();
        final String address2 = tvAddress2.getText().toString();
        final String address3 = tvAddress3.getText().toString();
        final String address = etAddress.getText().toString();
        final String addressSeqs = tvAddress1.getTag().toString() + "," + tvAddress2.getTag().toString() + "," + tvAddress3.getTag().toString();

        final int pId = ProgressDialogController.show(getContext());
        RestClient.getHomeClient().updateHomeAddress(homeId, address1, address2, address3, address, addressSeqs).enqueue(new RetrofitCallback<String>() {

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
