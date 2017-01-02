package id.co.okhome.okhomeapp.lib.pollmanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

import id.co.okhome.okhomeapp.R;
import id.co.okhome.okhomeapp.lib.JoChoiceViewController;
import id.co.okhome.okhomeapp.lib.OkhomeException;
import id.co.okhome.okhomeapp.lib.Util;
import id.co.okhome.okhomeapp.lib.ViewHolderUtil;
import id.co.okhome.okhomeapp.lib.pollmanager.model.PollItemCommon;
import id.co.okhome.okhomeapp.lib.pollmanager.model.PollItemSet;
import id.co.okhome.okhomeapp.lib.pollmanager.model.SubCheckItem;

import static id.co.okhome.okhomeapp.lib.ViewHolderUtil.getView;

/**
 * Created by josong on 2016-12-30.
 */

public class PollManager {

    PollItemSet pollItemSet = null;
    LayoutInflater inflater;
    Map<Integer, Object> mapComponent = new HashMap<>();
    Context context;

    public PollManager(Context context, String jsonSrc) {
        this.context  = context;
        inflater = LayoutInflater.from(context);
        pollItemSet = null;
        ObjectMapper mapper = new ObjectMapper();
        try{
            pollItemSet = mapper.readValue(jsonSrc, PollItemSet.class);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public int getCount(){
        return pollItemSet.listPollItemSet.size();
    }

    public Map<Integer, String> ok(){
        Map<Integer, String> mapResult = new HashMap<>();
        try{
            for(int i = 0; i < pollItemSet.listPollItemSet.size(); i++){
                Object obj = mapComponent.get(i);
                if(obj instanceof RadioboxAdapter){
                    RadioboxAdapter radioboxAdapter = (RadioboxAdapter)obj;
                    if(radioboxAdapter.getCheckedItemList().size() <= 0){
                        throw new OkhomeException("질문에 답을 해주세요 : " + pollItemSet.listPollItemSet.get(i).title);
                    }

                    mapResult.put(i, radioboxAdapter.getCheckItemStringType());
                } else if (obj instanceof ChkboxAdapter) {
                    ChkboxAdapter chkboxAdapter = (ChkboxAdapter)obj;
                    if(chkboxAdapter.getCheckedItemList().size() <= 0){
                        throw new OkhomeException("질문에 답을 해주세요 : " + pollItemSet.listPollItemSet.get(i).title);
                    }
                    mapResult.put(i, chkboxAdapter.getCheckItemStringType());
                } else if (obj instanceof EditText) {
                    EditText etContent = (EditText)obj;
                    String value = etContent.getText().toString();

                    if(value.length() <= 0){
                        throw new OkhomeException("질문에 답을 해주세요 : " + pollItemSet.listPollItemSet.get(i).title);
                    }
                    mapResult.put(i, value);
                }
            }
        }catch(OkhomeException e){
            Util.showToast(context, e.getMessage());
            return null;
        }


        return mapResult;
    }

    public void adaptView(ViewGroup vgParent){
        ViewGroup vgPollContainer = (ViewGroup)inflater.inflate(R.layout.item_poll_container, null);

        //투표 그룹정보 부터 설정
        TextView tvSubTitle = ViewHolderUtil.getView(vgPollContainer, R.id.itemPollContainer_tvSubTitle);
        TextView tvTitle = ViewHolderUtil.getView(vgPollContainer, R.id.itemPollContainer_tvTitle);
        tvSubTitle.setText(pollItemSet.subTitle);
        tvTitle.setText(pollItemSet.title);

        //투표 들어갈내용
        ViewGroup vgPollItemContainer = getView(vgPollContainer, R.id.itemPollContainer_vgPollItems);
        int i = 0;
        for(PollItemCommon pollItemCommon : pollItemSet.listPollItemSet){
            //아이템위에 덧씌울거.여기에 투표 소제목이 있음.
            ViewGroup vgItemContainer = (ViewGroup)inflater.inflate(R.layout.item_pollitem_container, null);
            vgPollItemContainer.addView(vgItemContainer);

            TextView tvPollTitle = ViewHolderUtil.getView(vgItemContainer, R.id.itemPollItemContainer_tvTitle);
            ViewGroup vgPollItem = ViewHolderUtil.getView(vgItemContainer, R.id.itemPollItemContainer_vgPollItem);
            tvPollTitle.setText((i + 1) + ". " + pollItemCommon.title);

            if(pollItemCommon.type.equals(PollItemCommon.TYPE_INPUT)){
                //인풋아이템일 경우
                ViewGroup vgInputItem = (ViewGroup)inflater.inflate(R.layout.item_pollitem_input, null);
                vgPollItem.addView(vgInputItem);

                EditText etInput = (EditText)vgInputItem.findViewById(R.id.itemPollItemInput_etInput);
                etInput.setHint(pollItemCommon.hint);

                mapComponent.put(i, etInput);
            }else{
                //체크시리즈
                if(pollItemCommon.type.equals(PollItemCommon.TYPE_RADIO)){
                    ChkboxAdapter chkboxAdapter = new ChkboxAdapter(context, vgPollItem);
                    chkboxAdapter.setList(pollItemCommon.listChkItems);
                    chkboxAdapter.build();

                    mapComponent.put(i, chkboxAdapter);
                }else{
                    RadioboxAdapter radioboxAdapter = new RadioboxAdapter(context, vgPollItem);
                    radioboxAdapter.setList(pollItemCommon.listChkItems);
                    radioboxAdapter.build();

                    mapComponent.put(i, radioboxAdapter);
                }
            }
            i++;
        }

        vgParent.addView(vgPollContainer);
        vgParent.addView(getPadding());

    }


    class ChkboxAdapter extends JoChoiceViewController<SubCheckItem> {

        public ChkboxAdapter(Context context, ViewGroup vgContent) {
            super(context, vgContent, false, 1);
        }

        @Override
        public void onItemCheckChanged(View vItem, SubCheckItem model, boolean checked, int pos) {
            TextView tvText = ViewHolderUtil.getView(vItem, R.id.itemPollItemCheckBox_tvText);
            ImageView ivChk = ViewHolderUtil.getView(vItem, R.id.itemPollItemCheckBox_ivChk);

            if(checked){
                ivChk.setImageResource(R.drawable.ic_checked);
            }else{
                ivChk.setImageResource(R.drawable.ic_check_not_deep);
            }
        }

        @Override
        public View getItemView(LayoutInflater inflater, SubCheckItem model, int pos) {
            View v = inflater.inflate(R.layout.item_pollitem_checkbox, null);

            TextView tvText = ViewHolderUtil.getView(v, R.id.itemPollItemCheckBox_tvText);
            tvText.setText(model.item);
            return v;
        }

        public String getCheckItemStringType(){
            String result  = "";
            for(SubCheckItem m : getCheckedItemList()){
                result = result + "," + m.item;
            }

            return result.substring(1);
        }
    }

    View getPadding(){
        View v = new View(context);
        v.setLayoutParams(new LinearLayout.LayoutParams(50, Util.getPixelByDp(context, 20)));
        return v;
    }

    class RadioboxAdapter extends JoChoiceViewController<SubCheckItem> {

        public RadioboxAdapter(Context context, ViewGroup vgContent) {
            super(context, vgContent, true, 1);
        }

        @Override
        public void onItemCheckChanged(View vItem, SubCheckItem model, boolean checked, int pos) {
            TextView tvText = ViewHolderUtil.getView(vItem, R.id.itemPollItemCheckBox_tvText);
            ImageView ivChk = ViewHolderUtil.getView(vItem, R.id.itemPollItemCheckBox_ivChk);

            if(checked){
                ivChk.setImageResource(R.drawable.ic_checked);
            }else{
                ivChk.setImageResource(R.drawable.ic_check_not_deep);
            }
        }

        @Override
        public View getItemView(LayoutInflater inflater, SubCheckItem model, int pos) {
            View v = inflater.inflate(R.layout.item_pollitem_checkbox, null);

            TextView tvText = ViewHolderUtil.getView(v, R.id.itemPollItemCheckBox_tvText);
            tvText.setText(model.item);
            return v;
        }

        public String getCheckItemStringType(){
            String result  = "";
            for(SubCheckItem m : getCheckedItemList()){
                result = result + "," + m.item;
            }

            return result.substring(1);
        }
    }

}

