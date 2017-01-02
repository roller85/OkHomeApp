package id.co.okhome.okhomeapp.view.fragment.tabitem;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.okhome.okhomeapp.R;
import id.co.okhome.okhomeapp.config.CurrentUserInfo;
import id.co.okhome.okhomeapp.lib.ProgressDialogController;
import id.co.okhome.okhomeapp.lib.Util;
import id.co.okhome.okhomeapp.lib.ViewHolderUtil;
import id.co.okhome.okhomeapp.lib.dialog.DialogController;
import id.co.okhome.okhomeapp.lib.dialog.ViewDialog;
import id.co.okhome.okhomeapp.lib.joviewrepeator.JoRepeatorCallback;
import id.co.okhome.okhomeapp.lib.joviewrepeator.JoViewRepeator;
import id.co.okhome.okhomeapp.lib.retrofit.RetrofitCallback;
import id.co.okhome.okhomeapp.model.CreditPromotionModel;
import id.co.okhome.okhomeapp.restclient.RestClient;
import id.co.okhome.okhomeapp.view.activity.PollActivity;
import id.co.okhome.okhomeapp.view.dialog.CommonInputDialog;
import id.co.okhome.okhomeapp.view.dialog.CommonTextDialog;
import id.co.okhome.okhomeapp.view.fragment.tabitem.flow.TabFragmentFlow;

/**
 * Created by josongmin on 2016-07-28.
 */

public class PromotionFragment extends Fragment implements TabFragmentFlow {

    @BindView(R.id.fragmentChargePoint_vLoading)            View vLoading;
    @BindView(R.id.fragmentChargePoint_vgPromotionItems)    ViewGroup vgPromotionItems;

    JoViewRepeator<CreditPromotionModel> creditPromotionItemAdapter;
    CreditPromotionItemCallback creditPromotionItemCallback;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_charge_point, null);
    }

    @Override
    public void onStart() {
        super.onStart();
        ButterKnife.bind(this, getView());

        creditPromotionItemCallback = new CreditPromotionItemCallback();
        creditPromotionItemAdapter =  new JoViewRepeator<CreditPromotionModel>(getContext())
                .setContainer(vgPromotionItems)
                .setItemLayoutId(R.layout.item_credit_promotionitem)
                .setCallBack(creditPromotionItemCallback);

        loadPromotionList();

    }

    @Override
    public String getTitle() {
        return "프로모션";
    }

    private void loadPromotionList(){
        vLoading.setVisibility(View.VISIBLE);
        String userId = CurrentUserInfo.getId(getContext());
        RestClient.getCreditPromotionClient().getCreditPromotionList(userId).enqueue(new RetrofitCallback<List<CreditPromotionModel>>() {

            @Override
            public void onFinish() {
                super.onFinish();
                vLoading.setVisibility(View.GONE);
            }

            @Override
            public void onSuccess(List<CreditPromotionModel> result) {
                creditPromotionItemAdapter.setList(result);
                creditPromotionItemAdapter.notifyDataSetChanged();
            }
        });

    }

    @Override
    public View.OnClickListener onTabSettingClick(ImageView ivIcon) {
        return null;
    }


    private void showFriendPromotionInputDialog(final CreditPromotionModel creditPromotionModel){
        CommonInputDialog commonInputDialog = new CommonInputDialog("Friend promotion code", "Get free 100.000 Credit!", "", new ViewDialog.DialogCommonCallback() {
            @Override
            public void onCallback(Object dialog, Map<String, Object> params) {
                String onClick = (String)params.get("ONCLICK");
                if(onClick.equals("OK")){
                    String text = (String)params.get("TEXT");
                    //텍스트를 넘기좌..

                    final int pid = ProgressDialogController.show(getContext());
                    RestClient.getCreditPromotionClient().applyFriendPromotionCode(CurrentUserInfo.getId(getContext()), creditPromotionModel.id, text).enqueue(new RetrofitCallback<String>() {

                        @Override
                        public void onFinish() {
                            ProgressDialogController.dismiss(pid);
                        }

                        @Override
                        public void onSuccess(String result) {
                            DialogController.showCenterDialog(getContext(), new CommonTextDialog("Complete", "100.000Rp will be charged", true, null));
                            loadPromotionList();
                        }

                    });
                }
            }
        });
        DialogController.showCenterDialog(getContext(), commonInputDialog);
        commonInputDialog.getEtInput().setHint("Input friend code");

    }

    private void showPromotionInputDialog(final CreditPromotionModel creditPromotionModel){
        CommonInputDialog commonInputDialog = new CommonInputDialog("Promotion code", "Get free 100.000 Credit!", "", new ViewDialog.DialogCommonCallback() {
            @Override
            public void onCallback(Object dialog, Map<String, Object> params) {
                String onClick = (String)params.get("ONCLICK");
                if(onClick.equals("OK")){
                    String text = (String)params.get("TEXT");
                    //텍스트를 넘기좌..

                    final int pid = ProgressDialogController.show(getContext());
                    RestClient.getCreditPromotionClient().applyPromotionCode(CurrentUserInfo.getId(getContext()), creditPromotionModel.id, text).enqueue(new RetrofitCallback<String>() {

                        @Override
                        public void onFinish() {
                            ProgressDialogController.dismiss(pid);
                        }

                        @Override
                        public void onSuccess(String result) {
                            DialogController.showCenterDialog(getContext(), new CommonTextDialog("Complete", "150.000Rp will be charged", true, null));
                            loadPromotionList();
                        }

                    });
                }
            }
        });
        DialogController.showCenterDialog(getContext(), commonInputDialog);
        commonInputDialog.getEtInput().setHint("Input promotion code");
        commonInputDialog.getEtInput().setInputType(InputType.TYPE_CLASS_NUMBER);
    }

    class CreditPromotionItemCallback extends JoRepeatorCallback<CreditPromotionModel>{

        @Override
        public void onBind(View v, final CreditPromotionModel model) {
            TextView tvSubTitle = ViewHolderUtil.getView(v, R.id.itemCreditPromotionItem_tvSubTitle);
            TextView tvTitle = ViewHolderUtil.getView(v, R.id.itemCreditPromotionItem_tvTitle);
            TextView tvValue = ViewHolderUtil.getView(v, R.id.itemCreditPromotionItem_tvValue);

            int reward = Integer.parseInt(model.reward);

            tvSubTitle.setText(model.subtitle);
            tvTitle.setText(model.title);
            tvValue.setText("+ " + Util.getMoneyString(reward, '.') + " Point" );

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(model.type.equals("POLL")){
                        startActivity(new Intent(getContext(), PollActivity.class)
                                .putExtra("reqType", PollActivity.REQTYPE_PROMOTION_POLL)
                                .putExtra("reqTypeValue", model.id)
                                .putExtra("jsonPollData", model.extra));
                    }
                    else if(model.type.equals("FRIEND")){
                        showFriendPromotionInputDialog(model);
                    }
                    else if(model.type.equals("PROMOTION")){
                        showPromotionInputDialog(model);
                    }
                }
            });
        }

    }


}
