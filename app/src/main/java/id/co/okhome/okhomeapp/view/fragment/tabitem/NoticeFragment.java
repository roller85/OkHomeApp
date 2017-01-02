package id.co.okhome.okhomeapp.view.fragment.tabitem;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mrjodev.jorecyclermanager.JoRecyclerAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.okhome.okhomeapp.R;
import id.co.okhome.okhomeapp.lib.retrofit.RetrofitCallback;
import id.co.okhome.okhomeapp.lib.retrofit.restmodel.ErrorModel;
import id.co.okhome.okhomeapp.model.NoticeModel;
import id.co.okhome.okhomeapp.restclient.RestClient;
import id.co.okhome.okhomeapp.view.fragment.tabitem.flow.TabFragmentFlow;
import id.co.okhome.okhomeapp.view.viewholder.BlankHolder;
import id.co.okhome.okhomeapp.view.viewholder.NoticeHolder;

/**
 * Created by josongmin on 2016-07-28.
 */

public class NoticeFragment extends Fragment implements TabFragmentFlow {

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

    @Override
    public String getTitle() {
        return "공지사항";
    }

    @Override
    public View.OnClickListener onTabSettingClick(ImageView ivIcon) {
        return null;
    }

    private void loadList(){
        vLoading.setVisibility(View.VISIBLE);

        RestClient.getNoticeClient().getNoticeList().enqueue(new RetrofitCallback<List<NoticeModel>>() {

            @Override
            public void onFinish() {
                vLoading.setVisibility(View.GONE);
            }

            @Override
            public void onSuccess(List<NoticeModel> result) {
                adapter.setListItems(result);
                vLoading.setVisibility(View.GONE);
            }

            @Override
            public void onJodevError(ErrorModel jodevErrorModel) {

            }
        });


    }

}
