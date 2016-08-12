package id.co.okhome.okhomeapp.lib;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by josongmin on 2016-08-11.
 */

public abstract class JoChoiceViewController <T> {

    Context context;

    //보관용 컨테이너들. 나중에 모델하나로 수정해야겠음. 편하게 할려고한게 오히려 더 복잡해짐.
    List<View> listViews = new ArrayList<>();
    List<T> listItems = new ArrayList<>();
    Map<View, T> mapModel = new HashMap<>();
    Map<View, Boolean> mapCheck = new HashMap<>();

    LayoutInflater inflater;
    boolean multiChoice = false;

    ViewGroup vgContent;
    int spanSize = 3;


    public JoChoiceViewController(Context context, ViewGroup vgContent, boolean multiChoice, int spanSize) {
        this.context = context;
        this.vgContent = vgContent;
        this.spanSize = spanSize;
        this.inflater = LayoutInflater.from(context);
        this.multiChoice = multiChoice;
    }

    public JoChoiceViewController addItem(T model){
        listItems.add(model);
        return this;
    }

    public JoChoiceViewController setChecked(int pos, boolean checked){
        chk(listViews.get(pos));
        return this;
    }

    /**최종 만들기*/
    public JoChoiceViewController build(){
        vgContent.removeAllViews();

        int size = listItems.size();

        ViewGroup vgVertical = makeVerticalViewContainer();
        ViewGroup vgHorizontal = null;
        for(int i = 0; i < size; i++){
            T model = listItems.get(i);
            if(i % spanSize == 0){
                Log.d("JO", "NewVertical");
                vgHorizontal = makeHorizontalViewContainer();
                vgVertical.addView(vgHorizontal);
            }

            final ViewGroup vItemContainer = makeItemViewContainer();
            View vItem = getItemView(inflater, model, i);
            vItemContainer.addView(vItem);

            //정리
            mapCheck.put(vItem, false);
            mapModel.put(vItem, model);
            listViews.add(vItem);

            //상태 변함onItemClick
            vItem.setOnClickListener(onItemClick);

            //컨테이너에 넣는다
            vgHorizontal.addView(vItemContainer);
        }

        Log.d("JO", vgVertical.getChildCount() + "개");
        vgContent.addView(vgVertical);

        return this;
    }

    //버티컬 컨테이너
    private ViewGroup makeVerticalViewContainer(){
        LinearLayout llContainer = new LinearLayout(context);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        llContainer.setLayoutParams(lp);
        llContainer.setOrientation(LinearLayout.VERTICAL);


        return llContainer;
    }

    //한줄 컨테이너 만들기
    private ViewGroup makeHorizontalViewContainer(){
        LinearLayout llContainer = new LinearLayout(context);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        llContainer.setWeightSum(spanSize);
        llContainer.setLayoutParams(lp);

        return llContainer;
    }

    //아이템뷰컨테이너 만들어지는곳
    private ViewGroup makeItemViewContainer(){
        LinearLayout llItem = new LinearLayout(context);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        lp.weight = 1;
        llItem.setLayoutParams(lp);
        llItem.setGravity(Gravity.CENTER);

        return llItem;
    }

    private void chk(View vTarget){
        if(multiChoice){
            T modelTarget = mapModel.get(vTarget);
            mapCheck.put(vTarget, !mapCheck.get(vTarget));
            onItemCheckChanged(vTarget, modelTarget, mapCheck.get(vTarget));
        }else{
            Set key = mapModel.keySet();
            for (Iterator<View> iterator = key.iterator(); iterator.hasNext();) {
                View vItem = iterator.next();
                T modelCurrent = mapModel.get(vItem);

                if(vTarget == vItem){
                    mapCheck.put(vTarget, true);
                    onItemCheckChanged(vItem, modelCurrent, true); //켜졋다고알림
                }else{
                    mapCheck.put(vItem, false);
                    onItemCheckChanged(vItem, modelCurrent, false); //꺼졋다고알림
                }
            }
        }
    }


    View.OnClickListener onItemClick = new View.OnClickListener() {
        @Override
        public void onClick(View vTarget) {
            chk(vTarget);
        }
    };

    public abstract View getItemView(LayoutInflater inflater,  T model, int pos);
    public abstract void onItemCheckChanged(View vItem, T model, boolean checked);






}
