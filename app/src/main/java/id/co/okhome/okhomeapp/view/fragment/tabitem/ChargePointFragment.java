package id.co.okhome.okhomeapp.view.fragment.tabitem;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.okhome.okhomeapp.R;
import id.co.okhome.okhomeapp.lib.JoChoiceViewController;
import id.co.okhome.okhomeapp.lib.Util;
import id.co.okhome.okhomeapp.lib.retrofit.RetrofitCallback;
import id.co.okhome.okhomeapp.lib.retrofit.restmodel.ErrorModel;
import id.co.okhome.okhomeapp.model.CreditProductModel;
import id.co.okhome.okhomeapp.restclient.RestClient;

/**
 * Created by josongmin on 2016-07-28.
 */

public class ChargePointFragment extends Fragment {

    @BindView(R.id.fragmentChargePoint_vLoading)        View vLoading;
    @BindView(R.id.fragmentChargePoint_vgProduct)       ViewGroup vgProduct;


    CreditProductChooser creditProductChooser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_charge_point, null);
    }

    @Override
    public void onStart() {
        super.onStart();
        ButterKnife.bind(this, getView());

        creditProductChooser = new CreditProductChooser(getContext(), vgProduct, false, 1);

        getProductList();
    }

    //상품갖고오기
    private void getProductList(){
        vLoading.setVisibility(View.VISIBLE);
        RestClient.getCreditClient().getCreditProductList().enqueue(new RetrofitCallback<List<CreditProductModel>>() {

            @Override
            public void onFinish() {
                vLoading.setVisibility(View.GONE);
            }

            @Override
            public void onSuccess(List<CreditProductModel> result) {
                creditProductChooser.setList(result);
                creditProductChooser.build();
            }

            @Override
            public void onJodevError(ErrorModel jodevErrorModel) {

            }
        });
    }

    class CreditProductChooser extends JoChoiceViewController<CreditProductModel> {
        public CreditProductChooser(Context context, ViewGroup vgContent, boolean multiChoice, int spanSize) {
            super(context, vgContent, multiChoice, spanSize);
        }

        class ViewHolder{
            @BindView(R.id.itemChargeCredit_ivChk)      ImageView ivChk;
            @BindView(R.id.itemChargeCredit_tvRupiah)   TextView tvRupiah;
            @BindView(R.id.itemChargeCredit_tvSummary)  TextView tvSummary;
            public ViewHolder(View v) {
                ButterKnife.bind(this, v);
            }
        }

        @Override
        public View getItemView(LayoutInflater inflater, CreditProductModel model, int pos) {
            View vItem = inflater.inflate(R.layout.item_charge_credit, null);
            ViewHolder h = (ViewHolder)vItem.getTag();
            if(h == null){
                h = new ViewHolder(vItem);
                vItem.setTag(h);
            }


            h.tvRupiah.setText(Util.getMoneyString(model.price, '.') + " Rp");
            h.tvSummary.setText("+ Bonus " + Util.getMoneyString(model.bonusCredit, '.') + " Gift");
            //체인지 변경
            onItemCheckChanged(vItem, model, false, pos);
            return vItem;
        }

        @Override
        public void onItemCheckChanged(View vItem, CreditProductModel model, boolean checked, int pos) {
            ViewHolder h = (ViewHolder)vItem.getTag();

            if(checked){
                h.ivChk.setImageResource(R.drawable.ic_checked);
            }else{
                h.ivChk.setImageResource(R.drawable.ic_check_not);
            }

        }
    }

}
