package id.co.okhome.okhomeapp.view.fragment.makereservation;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.okhome.okhomeapp.R;
import id.co.okhome.okhomeapp.lib.JoChoiceViewController;

/**
 * Created by josongmin on 2016-07-28.
 */

public class HouseInfoFragment extends Fragment {

    @BindView(R.id.fragmentMakeReservationHouseInfo_vgChkItemHomeType)          ViewGroup vgChkItemHomeType;
    @BindView(R.id.fragmentMakeReservationHouseInfo_vgChkItemHomeSize)          ViewGroup vgChkItemHomeSize;
    @BindView(R.id.fragmentMakeReservationHouseInfo_vgChkItemRestroomCount)     ViewGroup vgChkItemRestroomCount;
    @BindView(R.id.fragmentMakeReservationHouseInfo_vgChkItemRoomCount)         ViewGroup vgChkItemRoomCount;
    @BindView(R.id.fragmentMakeReservationHouseInfo_vgChkItemPet)               ViewGroup vgChkItemPet;


    Map<String, JoChoiceViewController> mapChoiceView = new HashMap<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_m_r_houseinfo, null);
    }

    @Override
    public void onStart() {
        super.onStart();
        ButterKnife.bind(this, getView());

        mapChoiceView.put("HOMETYPE"
                , new CommonChoiceViewController(getContext(), vgChkItemHomeType, false, 3)
                        .addItem("Apartment").addItem("House").addItem("Studio")
                        .build()
                        .setChecked(0, true));

        mapChoiceView.put("HOMESIZE"
                , new CommonChoiceViewController(getContext(), vgChkItemHomeSize, false, 3)
                        .addItem("100 or less").addItem("200 or less").addItem("200 or above")
                        .build());

        mapChoiceView.put("ROOMCOUNT"
                , new CommonChoiceViewController(getContext(), vgChkItemRoomCount, false, 4)
                        .addItem("Satu").addItem("Dua").addItem("Tiga").addItem("Empat +")
                        .build());

        mapChoiceView.put("TOILETCOUNT"
                , new CommonChoiceViewController(getContext(), vgChkItemRestroomCount, false, 4)
                        .addItem("Satu").addItem("Dua").addItem("Tiga").addItem("Empat +")
                        .build());

        mapChoiceView.put("PET"
                , new CommonChoiceViewController(getContext(), vgChkItemPet, false, 4)
                        .addItem("Dog").addItem("Cat").addItem("Etc").addItem("Nope")
                        .build());

    }

    class CommonChoiceViewController extends JoChoiceViewController<String> {
        public CommonChoiceViewController(Context context, ViewGroup vgContent, boolean multiChoice, int spanSize) {
            super(context, vgContent, multiChoice, spanSize);
        }

        class ViewHolder{
            @BindView(R.id.itemCommonToggle_tvItem) TextView tvItem;
            @BindView(R.id.itemCommonToggle_vItemBg) View vItemBg;

            public ViewHolder(View v) {
                ButterKnife.bind(this, v);
            }
        }

        @Override
        public View getItemView(LayoutInflater inflater, String model,int pos) {
            View vItem = inflater.inflate(R.layout.item_common_toggle, null);
            ViewHolder h = (ViewHolder)vItem.getTag();
            if(h == null){
                h = new ViewHolder(vItem);
                vItem.setTag(h);
            }

            h.tvItem.setText(model);

            onItemCheckChanged(vItem, model, false);
            return vItem;
        }

        @Override
        public void onItemCheckChanged(View vItem, String model, boolean checked) {
            ViewHolder h = (ViewHolder)vItem.getTag();
            if(checked){
                h.vItemBg.setBackgroundColor(getContext().getResources().getColor(R.color.colorAppPrimary2));
                h.tvItem.setTextColor(Color.parseColor("#ffffff"));
            }else{
                h.vItemBg.setBackgroundColor(Color.parseColor("#e7eced"));
                h.tvItem.setTextColor(getContext().getResources().getColor(R.color.colorGray));
            }
        }
    }

}
