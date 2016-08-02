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
import id.co.okhome.okhomeapp.model.QnaModel;
import id.co.okhome.okhomeapp.viewholder.QnAHolder;

/**
 * Created by josongmin on 2016-07-28.
 */

public class QnAFragment extends Fragment {

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
                        .setItemViewHolderCls(QnAHolder.class)
        );

        //서버에서 데이터 불러오기
        loadQnaList();
    }


    //서버를 통해서 가져올것이다
    private void loadQnaList(){
        vLoading.setVisibility(View.VISIBLE);
        new Handler(){
            @Override
            public void dispatchMessage(Message msg) {
                List<QnaModel> listQna = new ArrayList<QnaModel>();
                for(int i = 0; i < 100; i++){
                    QnaModel m = new QnaModel();
                    m.answer = "답변입니다 " + i;
                    m.question = "질문입니다" + i;
                    listQna.add(m);
                }

                adapter.setListItems(listQna);
                vLoading.setVisibility(View.GONE);

            }
        }.sendEmptyMessageDelayed(0, 2000);

    }

}
