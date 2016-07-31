package id.co.okhome.okhomeapp.view.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
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
import id.co.okhome.okhomeapp.model.NoticeModel;
import id.co.okhome.okhomeapp.viewholder.BlankHolder;
import id.co.okhome.okhomeapp.viewholder.NoticeHolder;

/**
 * Created by josongmin on 2016-07-28.
 */

public class NoticeFragment extends Fragment {

    @BindView(R.id.fragCommonRecyclerView_rcv)
    RecyclerView rcv;

    @BindView(R.id.fragCommonRecyclerView_pbLoading)
    View vLoading;

    JoRecyclerAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_common_recyclerview, null);
    }

    @Override
    public void onStart() {
        super.onStart();
        ButterKnife.bind(this, getView());

        adapter = new JoRecyclerAdapter(
                new JoRecyclerAdapter.Params()
                        .setRecyclerView(rcv)
                        .setItemViewHolderCls(NoticeHolder.class)
                        .setHeaderViewHolderCls(BlankHolder.class)
        );

        adapter.addHeaderItem("");
        loadList();
    }

    private void loadList(){
        vLoading.setVisibility(View.VISIBLE);
        new Handler(){
            @Override
            public void dispatchMessage(Message msg) {
                List<NoticeModel> list = new ArrayList<NoticeModel>();

                for(int i = 0; i < 40; i++){
                    NoticeModel m = new NoticeModel();
                    m.title = "Hello Notice title test " + (i+1);
                    list.add(m);
                }

                adapter.setListItems(list);
                vLoading.setVisibility(View.GONE);
            }
        }.sendEmptyMessageDelayed(0, 1000);

    }

}
