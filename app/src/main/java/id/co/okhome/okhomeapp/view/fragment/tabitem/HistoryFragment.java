package id.co.okhome.okhomeapp.view.fragment.tabitem;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mrjodev.jorecyclermanager.JoRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.okhome.okhomeapp.R;
import id.co.okhome.okhomeapp.lib.joviewpager.JoViewPagerController;
import id.co.okhome.okhomeapp.lib.joviewpager.JoViewPagerItem;
import id.co.okhome.okhomeapp.model.CreditHistoryModel;
import id.co.okhome.okhomeapp.view.viewholder.CreditHistoryHolder;

/**
 * Created by josongmin on 2016-07-28.
 */

public class HistoryFragment extends Fragment {

    @BindView(R.id.fragmentHistory_vp)
    ViewPager vp;

    @BindView(R.id.fragmentHistory_vTabBar)
    View vTabBar;

    JoViewPagerController viewPagerController;
    int tabBarWidth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_history, null);
    }

    @Override
    public void onStart() {
        super.onStart();
        ButterKnife.bind(this, getView());

        viewPagerController = JoViewPagerController
                .with(getContext(), vp)
                .setViewPageItemClass(this, HistoryPageItem.class);

        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                float factor = (position + positionOffset) * ((tabBarWidth) );
                vTabBar.setTranslationX(factor);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        vp.setOffscreenPageLimit(3);
        calcViewSize();
        loadList();
    }

    //뷰 크기 계싼하기
    private void calcViewSize(){
        getView().post(new Runnable() {
            @Override
            public void run() {
                tabBarWidth = vTabBar.getWidth();
            }
        });
    }

    //내역 불러오기
    private void loadList(){

        List list = viewPagerController.getListModel();
        if(list == null){
            list = new ArrayList();
            list.add("ALL");
            list.add("SPEND");
            list.add("CHARGE");

        }

        viewPagerController.setListModel(list).build();

    }

    //탭아이템 처리
    public class HistoryPageItem extends JoViewPagerItem<String> {

        @BindView(R.id.fragCommonRecyclerView_rcv)          RecyclerView rcv;
        @BindView(R.id.fragCommonRecyclerView_pbLoading)    View vLoading;
        @BindView(R.id.fragCommonRecyclerView_vLine)        View vLine;

        JoRecyclerAdapter adapter;

        @Override
        public View getView(LayoutInflater inflater) {
            return inflater.inflate(R.layout.fragment_common_recyclerview, null);
        }

        @Override
        public void onViewCreated() {
            ButterKnife.bind(this, getDecorView());
            vLine.setVisibility(View.GONE);

            adapter = new JoRecyclerAdapter(
                    new JoRecyclerAdapter.Params()
                            .setRecyclerView(rcv)
                            .setItemViewHolderCls(CreditHistoryHolder.class)
            );
        }

        @Override
        public void onViewSelected(String type, int position) {
            if(type.equals("ALL")){
                loadList();
            }else if(type.equals("SPEND")){
                loadList();
            }else if(type.equals("CHARGE")){
                loadList();
            }
        }

        final int[] arrCredit = new int[]{-100000, 50000, 100000, 70000, 100000, 30000, 900000, 150000};
        final String[] type = new String[]{"Card", "Credit", "Cash", "Bonus"};
        private void loadList(){
            vLoading.setVisibility(View.VISIBLE);
            new Handler(){
                @Override
                public void dispatchMessage(Message msg) {
                    List<CreditHistoryModel> list = new ArrayList();

                    for(int i = 0; i < 40; i++){
                        CreditHistoryModel m = new CreditHistoryModel();

                        m.credit = arrCredit[i % arrCredit.length] + "";
                        m.type = type[i % type.length];

                        list.add(m);
                    }

                    adapter.setListItems(list);
                    vLoading.setVisibility(View.GONE);
                }
            }.sendEmptyMessageDelayed(0, 1000);

        }

    }
}
