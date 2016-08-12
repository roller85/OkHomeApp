package id.co.okhome.okhomeapp.lib;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by josongmin on 2016-08-10.
 */

public class JoViewRepeater<E> {

    //instance
    List<E> list = new ArrayList<E>();
    Context context;
    ViewGroup vg;
    int itemLayoutId;
    JoRepeaterCallBack callBack;


    //constructor
    public JoViewRepeater(Context context) {
        this.context = context;
    }

    //동작
    public void notifyDataSetChanged(){
        vg.removeAllViews();
        //헤더

        if(callBack != null){
            View vHeader = callBack.getHeaderView();
            if(vHeader != null) vg.addView(vHeader);
        }
        //아이템
        for(E model : list){
            View vItem = getView(model);
            vg.addView(vItem);
        }

        //푸터
        if(callBack != null){
            View vFooter = callBack.getFooterView();
            if(vFooter!= null) vg.addView(vFooter);
        }
    }

    private View getView(E model){
        View vItem = LayoutInflater.from(context).inflate(itemLayoutId, null);
        if(callBack != null){
            callBack.onBind(vItem, model);
        }
        return vItem;
    }

    public void notifyDataInserted(int pos){
        E model = getList().get(pos);
        View v = getView(model);
        vg.addView(v, pos);
    }

    public void notifyDataInsertedAtLast(){
        notifyDataInserted(getList().size()-1);
    }

    public void notifyDataRemoved(int pos){
        vg.removeView(vg.getChildAt(pos));
    }

    //컨테이너 설정
    public JoViewRepeater<E> setContainer(ViewGroup vg){
        this.vg = vg;
        return this;
    }

    public JoViewRepeater<E>setContainer(int viewGroupId){
        vg = (ViewGroup) LayoutInflater.from(context).inflate(viewGroupId, null);
        return this;
    }

    //반복뷰설정
    public JoViewRepeater<E>setItemLayoutId(int itemLayoutId){
        this.itemLayoutId = itemLayoutId;
        return this;
    }

    //setter


    public List<E> getList() {
        return list;
    }

    public JoViewRepeater<E>setCallBack(JoRepeaterCallBack callBack) {
        this.callBack = callBack;
        return this;
    }

    public void setList(List<E> list) {
        this.list = list;
    }

    //추가
    public void insertItem(E model){
        list.add(model);
    }

    public void insertItem(E model, int pos){
        list.add(pos, model);
    }

    public void insertItemAtLast(E model){
        list.add(list.size(), model);
    }

    //제거
    public void deleteItem(int pos){
        list.remove(pos);
    }

    public int deleteItem(E model){
        for(int i = 0; i < list.size(); i++){
            if(list.get(i) == model){
                list.remove(model);
                return i;
            }
        }

        return -1;
    }


    public interface JoRepeaterCallBack<E>{
        public View getHeaderView();
        public View getFooterView();
        public void onBind(View v, E model);
    }



}
