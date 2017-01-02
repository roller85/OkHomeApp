package id.co.okhome.okhomeapp.view.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.mrjodev.jorecyclermanager.JoRecyclerAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.okhome.okhomeapp.R;
import id.co.okhome.okhomeapp.lib.JoSharedPreference;
import id.co.okhome.okhomeapp.lib.OkHomeActivityParent;
import id.co.okhome.okhomeapp.model.CleaningReservationModel;
import id.co.okhome.okhomeapp.view.viewholder.BlankHolder;
import id.co.okhome.okhomeapp.view.viewholder.CollisionCleaningHolder;

public class CollisionCleaningManagerActivity extends OkHomeActivityParent {

    @BindView(R.id.actCCM_rcv)
    RecyclerView rcv;

    @BindView(R.id.actCCM_vLoading)
    View vLoading;

    JoRecyclerAdapter adapter;
    Handler handlerChargeCredit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collision_cleaning_manager);

        ButterKnife.bind(this);
        initHandler();

        adapter = new JoRecyclerAdapter(
                new JoRecyclerAdapter.Params()
                        .setRecyclerView(rcv)
                        .setItemViewHolderCls(CollisionCleaningHolder.class)
                        .setFooterViewHolderCls(BlankHolder.class)
        );

        adapter.addFooterItem(new String());
        adapter.addFooterItem(new String());
        adapter.addFooterItem(new String());
        adapter.addFooterItem(new String());
        loadList();
    }


    private void initHandler(){
        handlerChargeCredit = new Handler(){
            @Override
            public void dispatchMessage(Message msg) {
            }
        };
    }

    private void loadList(){

        vLoading.setVisibility(View.GONE);
        List<CleaningReservationModel> listResult = JoSharedPreference.with().get("READY_CLEANING_QUEUE");
        adapter.setListItems(listResult);

    }

    @OnClick(R.id.actCCM_llbtnBack)
    public void onBack(View v){
        finish();
    }
}
