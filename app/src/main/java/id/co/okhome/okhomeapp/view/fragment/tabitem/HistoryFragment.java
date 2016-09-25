package id.co.okhome.okhomeapp.view.fragment.tabitem;

import android.os.Bundle;
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
import id.co.okhome.okhomeapp.config.CurrentUserInfo;
import id.co.okhome.okhomeapp.lib.joviewpager.JoViewPagerController;
import id.co.okhome.okhomeapp.lib.joviewpager.JoViewPagerItem;
import id.co.okhome.okhomeapp.lib.retrofit.RetrofitCallback;
import id.co.okhome.okhomeapp.lib.retrofit.restmodel.ErrorModel;
import id.co.okhome.okhomeapp.model.CreditLogModel;
import id.co.okhome.okhomeapp.restclient.RestClient;
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
                loadList("ALL");
            }else if(type.equals("SPEND")){
                loadList("USE");
            }else if(type.equals("CHARGE")){
                loadList("CHARGE");
            }
        }

        private void loadList(String type){
            vLoading.setVisibility(View.VISIBLE);
            RestClient.getCreditClient().getCreditList(CurrentUserInfo.getId(getContext()), type, "0", "20").enqueue(new RetrofitCallback<List<CreditLogModel>>() {
                @Override
                public void onSuccess(List<CreditLogModel> result) {
                    adapter.setListItems(result);
                    vLoading.setVisibility(View.GONE);
                }

                @Override
                public void onJodevError(ErrorModel jodevErrorModel) {

                }
            });

        }

    }
}
