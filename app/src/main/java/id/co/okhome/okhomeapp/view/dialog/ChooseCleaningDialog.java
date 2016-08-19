package id.co.okhome.okhomeapp.view.dialog;

import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.okhome.okhomeapp.R;
import id.co.okhome.okhomeapp.lib.Util;
import id.co.okhome.okhomeapp.lib.dialog.ViewDialog;
import id.co.okhome.okhomeapp.lib.joviewpager.JoViewPagerController;
import id.co.okhome.okhomeapp.lib.joviewpager.JoViewPagerItem;
import id.co.okhome.okhomeapp.model.CleaningItemModel;
import id.co.okhome.okhomeapp.view.customview.ProgressDotsView;

/**
 * Created by josongmin on 2016-08-09.
 */

public class ChooseCleaningDialog extends ViewDialog{

    @BindView(R.id.dialogCleaningList_vp)
    ViewPager vp;

    @BindView(R.id.dialogCleaningList_pdv)
    ProgressDotsView pdv;

    @BindView(R.id.dialogCleaningList_vTabBar)
    View vTabBar;

    JoViewPagerController viewPagerController;

    int vTabBarWidth = 0, vTabWidth = 0;

    @Override
    public View getView(LayoutInflater inflater) {
        View v = inflater.inflate(R.layout.dialog_cleaning_list, null);
//        if(getParams().get("GRAVITY") != null && getParams().get("GRAVITY").equals("CENTER")){
//            View vContent = ((ViewGroup)v).getChildAt(0);
//            ((LinearLayout.LayoutParams)vContent.getLayoutParams())
//                    .setMargins(
//                            Util.getPixelByDp(getContext(), 1)
//                            , Util.getPixelByDp(getContext(), 5)
//                            , Util.getPixelByDp(getContext(), 1)
//                            , Util.getPixelByDp(getContext(), 5));
//        }
        return v;

    }

    @Override
    public void onViewCreated() {
        ButterKnife.bind(this, getDecorView());

        viewPagerController = JoViewPagerController
                .with(getContext(), vp)
                .setProgressDotsView(pdv)
                .setViewPageItemClass(this, CleaningPageItem.class)
                .putParam(ChooseCleaningDialog.class.getName(), this);

        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.d("JO", vTabBarWidth + "");
                float factor = (position + positionOffset) * ((vTabBarWidth) );
                vTabBar.setTranslationX(factor);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        loadList();

        calcViewSize();
    }

    private void calcViewSize(){
        vTabBar.post(new Runnable() {
            @Override
            public void run() {
                vTabBarWidth = vTabBar.getWidth();
                vTabWidth = ((View)vTabBar.getParent()).getWidth();
            }
        });
    }


    //리스트불러옴. 서버에서도 불러올수있게
    private void loadList(){

        List list = viewPagerController.getListModel();
        if(list == null){
            list = new ArrayList();
            list.add(new CleaningItemModel(R.drawable.img_noti_team, "Super-Super restroom cleaning", "We improve your performance.\nThat's why you need us", "450000", "2"));
            list.add(new CleaningItemModel(R.drawable.img_noti_team, "Extream dapur cleaning", "Lorem upsum desccaa\ncanal", "150000", "2"));
            list.add(new CleaningItemModel(R.drawable.img_noti_files, "Mega kamar tidurcleaning", "Lorem upsum desccaa\ncanal", "150000", "2"));
            list.add(new CleaningItemModel(R.drawable.img_noti_team, "Super-Super restroom cleaning", "Lorem upsum desccaa\ncanal", "150000", "2"));
            list.add(new CleaningItemModel(R.drawable.img_noti_files, "Super-Super A cleaning", "Lorem upsum desccaa\ncanal", "150000", "2"));
            list.add(new CleaningItemModel(R.drawable.img_noti_files, "Super-Super B cleaning", "Lorem upsum desccaa\ncanal", "150000", "2"));
        }

        viewPagerController.setListModel(list).build();

    }

    //클리닝 아이템
    public class CleaningPageItem extends JoViewPagerItem<CleaningItemModel>{

        @BindView(R.id.itemCleaning_ivPhoto)        ImageView ivPhoto;
        @BindView(R.id.itemCleaning_tvDesc)         TextView tvDesc;
        @BindView(R.id.itemCleaning_tvPrice)        TextView tvPrice;
        @BindView(R.id.itemCleaning_tvTitle)        TextView tvTitle;
        @BindView(R.id.itemCleaning_tvRequest)      TextView tvRequest;
        @BindView(R.id.itemCleaning_vChk)           View vChk;

        @Override
        public View getView(LayoutInflater inflater) {
            return inflater.inflate(R.layout.item_cleaning, null);
        }

        @Override
        public void onViewCreated() {
            ButterKnife.bind(this, getDecorView());
        }

        @Override
        public void onViewSelected(CleaningItemModel model, int position) {
            ivPhoto.setImageResource(model.imgResId);
            tvDesc.setText(model.desc);
            tvPrice.setText(model.hour + "Hours / " + Util.getMoneyString(model.price)+"Rp");
            tvTitle.setText(model.title);

            if(model.isChecked){
                vChk.setVisibility(View.VISIBLE);
                tvRequest.setText("Cancel");
            }else{
                vChk.setVisibility(View.INVISIBLE);
                tvRequest.setText("Request");
            }
        }

        @OnClick({R.id.itemCleaning_vbtnRequest})
        public void onBtnRequest(){
            getModel().isChecked = !getModel().isChecked;
            refresh();

            ChooseCleaningDialog dialog = getGlobalParam(ChooseCleaningDialog.class.getName());
            dialog.dismiss();


            //콜백 던지기
            getCommonCallback().onCallback(dialog.getDialog(),
                    Util.makeMap("pos", getPosition(), "isChked", getModel().isChecked, "CleaningItemModel", getModel()));
        }

    }

    //모델


}
