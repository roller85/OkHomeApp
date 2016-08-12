package id.co.okhome.okhomeapp.view.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import id.co.okhome.okhomeapp.R;

/**
 * Created by josongmin on 2016-08-08.
 */

public class ProgressDotsView extends LinearLayout {

    ViewGroup vgDots;
    int maxCount;

    public ProgressDotsView(Context context) {
        super(context);
        init();
    }

    public ProgressDotsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ProgressDotsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        if(!isInEditMode()){
            setGravity(Gravity.CENTER);
            vgDots = (ViewGroup)LayoutInflater.from(getContext()).inflate(R.layout.layer_progress_dot, null);
            this.addView(vgDots);
        }
    }

    public ProgressDotsView setMaxCount(int count){
        this.maxCount = count;
        for(int i = 0; i < vgDots.getChildCount(); i++){
            if(i < count){
                Log.d("JO", "VISIBLE");
                vgDots.getChildAt(i).setVisibility(View.VISIBLE);
                vgDots.getChildAt(i).setBackgroundResource(R.drawable.circle_gray_whitegray);
            }else{
                vgDots.getChildAt(i).setVisibility(View.GONE);
            }
        }

        return this;
    }

    public ProgressDotsView setCurrentPos(int pos){
        for(int i = 0; i < maxCount; i++){
            if(i == pos){
                vgDots.getChildAt(i).setBackgroundResource(R.drawable.circle_appcolor);
            }else{
                vgDots.getChildAt(i).setBackgroundResource(R.drawable.circle_gray_whitegray);
            }
        }
        return this;
    }
}
