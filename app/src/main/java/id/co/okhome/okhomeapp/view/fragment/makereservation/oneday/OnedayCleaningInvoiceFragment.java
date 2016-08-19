package id.co.okhome.okhomeapp.view.fragment.makereservation.oneday;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.orhanobut.dialogplus.DialogPlus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.okhome.okhomeapp.R;
import id.co.okhome.okhomeapp.lib.JoViewRepeater;
import id.co.okhome.okhomeapp.lib.Util;
import id.co.okhome.okhomeapp.lib.dialog.DialogController;
import id.co.okhome.okhomeapp.lib.dialog.ViewDialog;
import id.co.okhome.okhomeapp.model.CleaningItemModel;
import id.co.okhome.okhomeapp.view.dialog.ChooseCleaningDialog;

/**
 * Created by josongmin on 2016-07-28.
 */

public class OnedayCleaningInvoiceFragment extends Fragment {

    @BindView(R.id.fragmentMakeReservationDayCleaningInvoice_vgCleaningItem)    ViewGroup vgCleaningItem;
    TextView tvTotalDuration;
    TextView tvTotalPrice;


    DialogPlus dialogChooseCleaning;

    JoViewRepeater<CleaningItemModel> viewRepeater;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_m_r_daycleaning_invoice, null);
    }

    @Override
    public void onStart() {
        super.onStart();
        ButterKnife.bind(this, getView());


        viewRepeater = new JoViewRepeater<CleaningItemModel>(getContext())
                .setContainer(vgCleaningItem)
                .setItemLayoutId(R.layout.item_cleaning_simple)
                .setCallBack(new JoViewRepeater.JoRepeaterCallBack<CleaningItemModel>() {

                    @Override
                    public View getHeaderView() {
                        return null;
                    }

                    @Override
                    public View getFooterView() {
                        View v = LayoutInflater.from(getContext()).inflate(R.layout.item_cleaning_simple_footer, null);
                        tvTotalDuration = (TextView)v.findViewById(R.id.itemCleaningSimpleFooter_tvTotalDuration);
                        tvTotalPrice = (TextView)v.findViewById(R.id.itemCleaningSimpleFooter_tvTotalPrice);

                        refreshTotalPrice();
                        return v;
                    }

                    @Override
                    public void onBind(View v, CleaningItemModel model) {
                        TextView tvTitle = (TextView) v.findViewById(R.id.itemCleaningSimple_tvTitle);
                        TextView tvPrice = (TextView) v.findViewById(R.id.itemCleaningSimple_tvPrice);
                        TextView tvDuration = (TextView) v.findViewById(R.id.itemCleaningSimple_tvDuration);

                        //matching data
                        tvTitle.setText(model.title);
                        tvPrice.setText(Util.getMoneyString(model.price, '.') + "Rp");
                        tvDuration.setText(model.hour + "hr");
                    }
                });
        loadList();
    }

    private void loadList(){
        List<CleaningItemModel> listCleaningItems = new ArrayList<>();
        listCleaningItems.add(new CleaningItemModel(R.drawable.img_noti_team, "Basic cleaning", "simple", "100000", "4"));
        listCleaningItems.add(new CleaningItemModel(R.drawable.img_noti_team, "Consulting", "simple", "0", "1"));

        viewRepeater.setList(listCleaningItems);
        viewRepeater.notifyDataSetChanged();
    }

    private void refreshTotalPrice(){
        int price = 0;
        int hour = 0;
        for(CleaningItemModel m : viewRepeater.getList()){
            price += Integer.parseInt(m.price);
            hour += Integer.parseInt(m.hour);

            tvTotalPrice.setText(Util.getMoneyString(price+"", '.') + "Rp");
            tvTotalDuration.setText(hour +"hr");

        }
    }

    @OnClick(R.id.fragmentMakeReservationDayCleaningInvoice_vbtnServices)
    public void onBtnServicesClick(){

        if(dialogChooseCleaning == null){
            dialogChooseCleaning =
                    DialogController.showBottomDialog(
                            getContext()

                            //콜백 처리
                            , new ChooseCleaningDialog().setCommonCallback(new ViewDialog.DialogCommonCallback() {
                                @Override
                                public void onCallback(Object dialog, Map<String, Object> params) {
                                    boolean isChked = (boolean)params.get("isChked");
                                    int pos = (int)params.get("pos");
                                    CleaningItemModel model = (CleaningItemModel)params.get("CleaningItemModel");

                                    if(isChked){
                                        //리스트에 추가
                                        viewRepeater.insertItemAtLast(model);
                                        viewRepeater.notifyDataInsertedAtLast();
                                    }else{
                                        int deletePos = viewRepeater.deleteItem(model);
                                        viewRepeater.notifyDataRemoved(deletePos);
                                        if(pos != -1){

                                        }
                                    }

                                    //합계
                                    refreshTotalPrice();
                                }
                            }));
        }
        dialogChooseCleaning.show();
    }


}
