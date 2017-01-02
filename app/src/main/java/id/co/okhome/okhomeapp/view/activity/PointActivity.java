package id.co.okhome.okhomeapp.view.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.mrjodev.jorecyclermanager.JoRecyclerAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.okhome.okhomeapp.R;
import id.co.okhome.okhomeapp.config.CurrentUserInfo;
import id.co.okhome.okhomeapp.lib.OkHomeActivityParent;
import id.co.okhome.okhomeapp.lib.dialog.DialogController;
import id.co.okhome.okhomeapp.lib.retrofit.RetrofitCallback;
import id.co.okhome.okhomeapp.lib.retrofit.restmodel.ErrorModel;
import id.co.okhome.okhomeapp.model.CreditLogModel;
import id.co.okhome.okhomeapp.restclient.RestClient;
import id.co.okhome.okhomeapp.view.dialog.ChargePointDialog;
import id.co.okhome.okhomeapp.view.viewholder.CreditHistoryHolder;
import id.co.okhome.okhomeapp.view.viewholder.PointUsageHeaderHolder;

public class PointActivity extends OkHomeActivityParent {

    @BindView(R.id.actPoint_tvTitle)
    TextView tvTitle;

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

        tvTitle.setText("포인트");
        initHandler();

        adapter = new JoRecyclerAdapter(
                new JoRecyclerAdapter.Params()
                        .addParam("handlerChargeCredit", handlerChargeCredit)
                        .setRecyclerView(rcv)
                        .setItemViewHolderCls(CreditHistoryHolder.class)
                        .setHeaderViewHolderCls(PointUsageHeaderHolder.class)
        );

        adapter.addHeaderItem(CurrentUserInfo.get(this).credit);
        loadList();

    }


    private void initHandler(){
        handlerChargeCredit = new Handler(){
            @Override
            public void dispatchMessage(Message msg) {
                showChargeCreditDialog();
            }
        };
    }

    private void showChargeCreditDialog(){
        DialogController.showCenterDialog(this, new ChargePointDialog());
    }


    private void loadList(){
        vLoading.setVisibility(View.VISIBLE);
        RestClient.getCreditClient().getCreditList(CurrentUserInfo.getId(this), "ALL", "0", "20").enqueue(new RetrofitCallback<List<CreditLogModel>>() {
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

    @OnClick(R.id.actPoint_llbtnBack)
    public void onBack(View v){
        finish();
    }
}
