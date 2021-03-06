package id.co.okhome.okhomeapp.view.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.okhome.okhomeapp.R;
import id.co.okhome.okhomeapp.lib.JoChoiceViewController;
import id.co.okhome.okhomeapp.lib.Util;
import id.co.okhome.okhomeapp.lib.dialog.ViewDialog;
import id.co.okhome.okhomeapp.model.SpcCleaningModel;

import static java.lang.Integer.parseInt;

/**
 * Created by josongmin on 2016-08-09.
 */

public class ChooseCleaningGridDialog extends ViewDialog{


    @BindView(R.id.dialogCleaningGridList_llContent)
    ViewGroup vgContent;

    List<SpcCleaningModel> listCleaningItem = null;
    List<SpcCleaningModel> listDefCleaningSet = null;
    Map<String, String> mapChkedCleaningIdTemp = null;

    OnExtraCleaningChoosedListener onExtraCleaningChoosedListener;
    ExtraCleaningItemAdapter adapter;
    int defaultDuration = 0;
    Map<Integer, SpcCleaningModel> mapSpcItemChecked;

    public ChooseCleaningGridDialog(int defaultDuration, List<SpcCleaningModel> listCleaningItem, List<SpcCleaningModel> listDefCleaningSet, OnExtraCleaningChoosedListener onExtraCleaningChoosedListener) {
        this.defaultDuration = defaultDuration;
        this.listCleaningItem = listCleaningItem;
        this.onExtraCleaningChoosedListener = onExtraCleaningChoosedListener;

        if(listDefCleaningSet != null){
            mapSpcItemChecked = new HashMap<>();
            for(SpcCleaningModel m : listDefCleaningSet){
                mapSpcItemChecked.put(parseInt(m.id), m);
            }
        }
    }

    @Override
    public View getView(LayoutInflater inflater) {
        View v = inflater.inflate(R.layout.dialog_cleaning_grid_list, null);
        return v;
    }

    @Override
    public void onViewCreated() {

        ButterKnife.bind(this, getDecorView());

        adapter = new ExtraCleaningItemAdapter(getContext(), vgContent, true, 2);
        adapter.setList(listCleaningItem);
        adapter.build();

        if(mapSpcItemChecked != null){
            for(int i = 0; i < listCleaningItem.size(); i++){
                SpcCleaningModel m = listCleaningItem.get(i);
                int spcId = Integer.parseInt(m.id);
                if(mapSpcItemChecked.get(spcId) != null){
                    adapter.setChecked(i, true);
                }
            }
        }
    }

    @Override
    public void show() {
        mapChkedCleaningIdTemp = new HashMap<>();
        for(SpcCleaningModel m : adapter.getCheckedItemList()){
            mapChkedCleaningIdTemp.put(m.id, m.id);

            Util.Log("show() m.id :  " + m.id);
        }
    }

    @OnClick(R.id.dialogCommon_vbtnCancel)
    public void onCancelClick(View v){

        for(SpcCleaningModel m : listCleaningItem){
            if(mapChkedCleaningIdTemp.get(m.id) == null){
                setCheck(m.id, false);
            }else{
                setCheck(m.id, true);
            }
        }

        dismiss();
    }

    @OnClick(R.id.dialogCommon_vbtnConfirm)
    public void onConfirmClick(View v){

        int totalDuration = defaultDuration;
        for(SpcCleaningModel m : adapter.getCheckedItemList()){
            totalDuration += m.hour;
        }

        onExtraCleaningChoosedListener.onChoosed(adapter.getCheckedItemList());
        dismiss();

    }

    public void setCheck(String specialCleaningId, boolean chk){

        int i = 0;
        for(SpcCleaningModel m : adapter.getListItems()){
            if(m.id.equals(specialCleaningId)){
                adapter.setChecked(i, chk);
                break;
            }
            i++;
        }
    }



    class ExtraCleaningItemAdapter extends JoChoiceViewController<SpcCleaningModel> {
        public ExtraCleaningItemAdapter(Context context, ViewGroup vgContent, boolean multiChoice, int spanSize) {
            super(context, vgContent, multiChoice, spanSize);
        }

        class ViewHolder{
            @BindView(R.id.itemExtraCleaningItem_tvName)        TextView tvName;
            @BindView(R.id.itemExtraCleaningItem_tvPrice)       TextView tvPrice;
            @BindView(R.id.itemExtraCleaningItem_tvHour)        TextView tvHour;
            @BindView(R.id.itemExtraCleaningItem_ivIcon)        ImageView ivIcon;
            @BindView(R.id.itemExtraCleaningItem_ivChk)         ImageView ivChk;


            public ViewHolder(View v) {
                ButterKnife.bind(this, v);
                v.setTag(this);
            }
        }

        @Override
        public View getItemView(LayoutInflater inflater, SpcCleaningModel model, int pos) {
            View vItem = inflater.inflate(R.layout.item_extra_cleaning_item, null);
            ViewHolder vh = (ViewHolder)vItem.getTag();
            if(vh == null){
                vh = new ViewHolder(vItem);
            }

            vh.tvName.setText(model.name);
            vh.tvHour.setText(model.hour < 0 ? (model.hour * 60) + "m" : (int)model.hour + "h");
            vh.tvPrice.setText(Util.getMoneyString(model.price, '.') + "Rp");

            Glide.with(getContext())
                    .load(model.imgUrl)
                    .dontAnimate()
                    .into(vh.ivIcon);

            onItemCheckChanged(vItem, model, false, pos);
            return vItem;
        }

        @Override
        public void onItemCheckChanged(View vItem, SpcCleaningModel model, boolean checked, int pos) {
            ViewHolder vh = (ViewHolder)vItem.getTag();
            if(checked){
                vh.ivChk.setImageResource(R.drawable.ic_checked);
            }else{
                vh.ivChk.setImageResource(R.drawable.ic_check_not_deep);
            }
            model.isChecked = checked;
        }

    }

    public interface OnExtraCleaningChoosedListener{
        public void onChoosed(List<SpcCleaningModel> list);
    }
}
