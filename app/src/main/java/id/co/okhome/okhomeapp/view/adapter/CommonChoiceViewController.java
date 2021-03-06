package id.co.okhome.okhomeapp.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.okhome.okhomeapp.R;
import id.co.okhome.okhomeapp.lib.JoChoiceViewController;
import id.co.okhome.okhomeapp.model.CommonCheckitemModel;

/**
 * Created by josongmin on 2016-09-07.
 */

public class CommonChoiceViewController extends JoChoiceViewController<CommonCheckitemModel> {

    List<ItemListener> listItemListener = new ArrayList<>();

    public CommonChoiceViewController(Context context, ViewGroup vgContent, boolean multiChoice, int spanSize) {
        super(context, vgContent, multiChoice, spanSize);
    }

    public void addItemListener(ItemListener itemListener) {
        listItemListener.add(itemListener);
    }

    class ViewHolder{
        @BindView(R.id.itemCommonToggle_tvItem)
        TextView tvItem;
        @BindView(R.id.itemCommonToggle_vItemBg)
        View vItemBg;

        public ViewHolder(View v) {
            ButterKnife.bind(this, v);
        }
    }

    @Override
    public View getItemView(LayoutInflater inflater, CommonCheckitemModel model, int pos) {
        View vItem = inflater.inflate(R.layout.item_common_toggle, null);
        ViewHolder h = (ViewHolder)vItem.getTag();
        if(h == null){
            h = new ViewHolder(vItem);
            vItem.setTag(h);
        }

        h.tvItem.setText(model.text);

        onItemCheckChanged(vItem, model, false, pos);
        return vItem;
    }

    @Override
    public void onItemCheckChanged(View vItem, CommonCheckitemModel model, boolean checked, int pos) {
        ViewHolder h = (ViewHolder)vItem.getTag();
        if(checked){

            h.vItemBg.setBackgroundColor(getContext().getResources().getColor(R.color.colorAppPrimary2));
            h.vItemBg.setBackgroundResource(R.drawable.bg_inputbox_selected);

            for(ItemListener l : listItemListener){
                l.onItemChecked(model, pos);
            }
//            h.tvItem.setTextColor(Color.parseColor("#096491"));
        }else{
//                h.vItemBg.setBackgroundColor(Color.parseColor("#e7eced"));
            h.vItemBg.setBackgroundResource(R.drawable.bg_inputbox);
            h.tvItem.setTextColor(getContext().getResources().getColor(R.color.colorGray));
        }

        for(ItemListener l : listItemListener){
            l.onItemChanged(model, pos);
        }
    }


    public interface ItemListener{
        void onItemChecked(CommonCheckitemModel model, int pos);
        void onItemChanged(CommonCheckitemModel model, int pos);
    }
}