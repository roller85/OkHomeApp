package id.co.okhome.okhomeapp.view.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.mrjodev.jorecyclermanager.JoRecyclerAdapter;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.okhome.okhomeapp.R;
import id.co.okhome.okhomeapp.config.CurrentUserInfo;
import id.co.okhome.okhomeapp.lib.OkHomeActivityParent;
import id.co.okhome.okhomeapp.lib.retrofit.RetrofitCallback;
import id.co.okhome.okhomeapp.model.CleaningTicketModel;
import id.co.okhome.okhomeapp.restclient.RestClient;
import id.co.okhome.okhomeapp.view.viewholder.BlankHolder;
import id.co.okhome.okhomeapp.view.viewholder.CleaningTicketHeaderHolder;
import id.co.okhome.okhomeapp.view.viewholder.CleaningTicketHolder;

public class CleaningTicketActivity extends OkHomeActivityParent {

    @BindView(R.id.actPoint_rcv)
    RecyclerView rcv;

    @BindView(R.id.actPoint_vLoading)
    View vLoading;

    JoRecyclerAdapter adapter;
    Handler handlerChargeCredit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point);

        ButterKnife.bind(this);
        initHandler();

        adapter = new JoRecyclerAdapter(
                new JoRecyclerAdapter.Params()
                        .addParam("handlerChargeCredit", handlerChargeCredit)
                        .setRecyclerView(rcv)
                        .setItemViewHolderCls(CleaningTicketHolder.class)
                        .setHeaderViewHolderCls(CleaningTicketHeaderHolder.class)
                        .setFooterViewHolderCls(BlankHolder.class)
        );

        adapter.addFooterItem(new String());
        adapter.addFooterItem(new String());
        adapter.addFooterItem(new String());
        adapter.addFooterItem(new String());
        getCleaningTicketSummary();
    }


    private void initHandler(){
        handlerChargeCredit = new Handler(){
            @Override
            public void dispatchMessage(Message msg) {
            }
        };
    }

    private void getCleaningTicketSummary(){
        vLoading.setVisibility(View.VISIBLE);
        RestClient.getCleaningClient().getCleaningCountSummary(CurrentUserInfo.getId(this)).enqueue(new RetrofitCallback<Map<String, String>>() {
            @Override
            public void onFinish() {
                vLoading.setVisibility(View.GONE);
                super.onFinish();
            }

            @Override
            public void onSuccess(Map<String, String> result) {
                adapter.addHeaderItem(result);
                loadList();
            }
        });

    }

    private void loadList(){
        vLoading.setVisibility(View.VISIBLE);


        RestClient.getCleaningClient().getCleaningTicketList(CurrentUserInfo.getId(this)).enqueue(new RetrofitCallback<List<CleaningTicketModel>>() {
            @Override
            public void onFinish() {
                super.onFinish();
                vLoading.setVisibility(View.GONE);
            }

            @Override
            public void onSuccess(List<CleaningTicketModel> result) {
                adapter.setListItems(result);
            }
        });

    }

    @OnClick(R.id.actPoint_llbtnBack)
    public void onBack(View v){
        finish();
    }
}
