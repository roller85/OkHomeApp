package id.co.okhome.okhomeapp.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.google.gson.Gson;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.okhome.okhomeapp.R;
import id.co.okhome.okhomeapp.config.CurrentUserInfo;
import id.co.okhome.okhomeapp.lib.OkHomeActivityParent;
import id.co.okhome.okhomeapp.lib.ProgressDialogController;
import id.co.okhome.okhomeapp.lib.Util;
import id.co.okhome.okhomeapp.lib.dialog.DialogController;
import id.co.okhome.okhomeapp.lib.dialog.ViewDialog;
import id.co.okhome.okhomeapp.lib.pollmanager.PollManager;
import id.co.okhome.okhomeapp.lib.pollmanager.model.PollItemSet;
import id.co.okhome.okhomeapp.lib.retrofit.RetrofitCallback;
import id.co.okhome.okhomeapp.restclient.RestClient;

public class PollActivity extends OkHomeActivityParent {

    public static final int REQTYPE_PROMOTION_POLL = 1;

    @BindView(R.id.actPoll_llPollItems)         LinearLayout llPollItems;

    String jsonPollData = "";
    PollItemSet pollItemSet = null;
    PollManager pollManager;
    int reqType = REQTYPE_PROMOTION_POLL;
    String reqTypeValue = "";
    String promotionId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poll);

        reqType = getIntent().getIntExtra("reqType", REQTYPE_PROMOTION_POLL);
        reqTypeValue = getIntent().getStringExtra("reqTypeValue"); //promotionId
        promotionId = reqTypeValue;

        ButterKnife.bind(this);

        jsonPollData = getIntent().getStringExtra("jsonPollData");
        if(jsonPollData == null){
            finish();
            return;

        }

        adaptViewAndData();
    }

    private void adaptViewAndData(){
        pollManager = new PollManager(this, jsonPollData);
        pollManager.adaptView(llPollItems);

    }

    @OnClick(R.id.actPoll_vbtnOk)
    public void onbtnOk(View v){
        Map<Integer, String> mapResult = pollManager.ok();
        if(mapResult == null){
            return;
        }else{
            for(int  i = 0; i < pollManager.getCount(); i++){
                Util.Log(mapResult.get(i));
            }

        }

        if(reqType == REQTYPE_PROMOTION_POLL){
            //엑스트라에 업데이트하기

            String jsonResult = new Gson().toJson(mapResult);
            final int pId = ProgressDialogController.show(this);
            RestClient.getCreditPromotionClient().applyPromotionPoll(CurrentUserInfo.getId(this), promotionId, jsonResult).enqueue(new RetrofitCallback<String>() {

                @Override
                public void onFinish() {
                    super.onFinish();
                    ProgressDialogController.dismiss(pId);
                }

                @Override
                public void onSuccess(String result) {
                    DialogController.showAlertDialog(PollActivity.this, "Okhome", "포인트가 적립되었습니다.\n참여해주셔서 감사합니다!", true, new ViewDialog.DialogCommonCallback() {
                        @Override
                        public void onCallback(Object dialog, Map<String, Object> params) {
                            finish();
                        }
                    });

                }
            });
        }
    }

    @OnClick(R.id.actPoll_llbtnBack)
    public void onBack(View v){
        finish();
    }


}
