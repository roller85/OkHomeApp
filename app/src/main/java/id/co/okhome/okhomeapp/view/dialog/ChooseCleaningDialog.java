package id.co.okhome.okhomeapp.view.dialog;

import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.okhome.okhomeapp.R;
import id.co.okhome.okhomeapp.lib.dialog.ViewDialog;
import id.co.okhome.okhomeapp.lib.joviewpager.JoViewPagerController;
import id.co.okhome.okhomeapp.model.SpcCleaningModel;
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
                .setViewPageItemClass(this, SpcCleaningModel.class)
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
//            list.add(new CleaningItemModel(R.drawable.ic_household_1, "Super-Super restroom cleaning", "We improve your performance.\nThat's why you need us", "450000", "2"));
//            list.add(new CleaningItemModel(R.drawable.ic_household_2, "Extream dapur cleaning", "Lorem Ipsum is simply dummy text of the printing and s", "150000", "2"));
//            list.add(new CleaningItemModel(R.drawable.ic_household_3, "Mega kamar tidurcleaning", "d it to make a type specimen book. It has survived not only five centuries, but also the", "150000", "2"));
//            list.add(new CleaningItemModel(R.drawable.ic_household_4, "Super-Super restroom cleaning", "pular belief, Lorem Ipsum is not simply random text.", "150000", "2"));
//            list.add(new CleaningItemModel(R.drawable.ic_household_5, "Super-Super A cleaning", "pular belief, Lorem Ipsum is not simply random text. It has", "150000", "2"));
//            list.add(new CleaningItemModel(R.drawable.ic_household_1, "Super-Super restroom cleaning", "We improve your performance.\nThat's why you need us", "450000", "2"));
//            list.add(new CleaningItemModel(R.drawable.ic_household_2, "Extream dapur cleaning", "Lorem Ipsum is simply dummy text of the printing and typesetting i's", "150000", "2"));
//            list.add(new CleaningItemModel(R.drawable.ic_household_3, "Mega kamar tidurcleaning", "d it to make a type specimen book. It has survived not", "150000", "2"));
        }

        viewPagerController.setListModel(list).build();

    }

//    //클리닝 아이템
//    public class CleaningPageItem extends JoViewPagerItem<CleaningItemModel>{
//
//        @BindView(R.id.itemCleaning_vbtnRequest)    View vbtnReq;
//        @BindView(R.id.itemCleaning_tvDesc)         TextView tvDesc;
//        @BindView(R.id.itemCleaning_tvPrice)        TextView tvPrice;
//        @BindView(R.id.itemCleaning_tvTitle)        TextView tvTitle;
//        @BindView(R.id.itemCleaning_tvRequest)      TextView tvRequest;
//        @BindView(R.id.itemCleaning_ivChk)          ImageView ivChk;
//
//        @Override
//        public View getView(LayoutInflater inflater) {
//            return inflater.inflate(R.layout.item_cleaning, null);
//        }
//
//        @Override
//        public void onViewCreated() {
//            ButterKnife.bind(this, getDecorView());
//        }
//
//        @Override
//        public void onViewSelected(SpcCleaningModel model, int position) {
//            tvDesc.setText(model.desc);
//            tvPrice.setText(model.hour + "Hours / " + Util.getMoneyString(model.price, '.')+"Rp");
//            tvTitle.setText(model.title);
//
////            if(model.isChecked){
////                vbtnReq.setBackgroundColor(getContext().getResources().getColor(R.color.colorLightGray));
////                ivChk.setImageResource(R.drawable.ic_cross_white);
////                tvRequest.setText("Cancel");
////            }else{
////                vbtnReq.setBackgroundColor(getContext().getResources().getColor(R.color.colorAppPrimary2));
////                ivChk.setImageResource(R.drawable.ic_check_type2);
////                tvRequest.setText("Request");
////            }
//        }
//
//        @OnClick({R.id.itemCleaning_vbtnRequest})
//        public void onBtnRequest(){
////            getModel().isChecked = !getModel().isChecked;
//            refresh();
//
//            ChooseCleaningDialog dialog = getGlobalParam(ChooseCleaningDialog.class.getName());
//            dialog.dismiss();
//
//
//            //콜백 던지기
////            getCommonCallback().onCallback(dialog.getDialog(),
////                    Util.makeMap("pos", getPosition(), "isChked", getModel().isChecked, "CleaningItemModel", getModel()));
//        }
//
//    }

    //모델


}
