package id.co.okhome.okhomeapp.view.etc;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.okhome.okhomeapp.R;
import io.codetail.animation.ViewAnimationUtils;

/**
 * Created by josong on 2016-12-23.
 */

public class FullpopupSpecialCleaningPromotion {

    @BindView(R.id.fullpopupSpecialCleaningPromo_vContents)     ViewGroup vgContents;
    @BindView(R.id.fullpopupSpecialCleaningPromo_vbtnReq)       View vbtnReq;

    ViewGroup vg;
    LayoutInflater inflater;
    View vParent;
    List<AnimModel> list;
    Handler handler = null;
    boolean on;
    ClickListener clickListener;

    public FullpopupSpecialCleaningPromotion(Activity act) {
        vg = (ViewGroup)act.getWindow().getDecorView();
        inflater = act.getLayoutInflater();
        vParent = inflater.inflate(R.layout.fullpopup_specialcleaning_promo, null);

        ButterKnife.bind(this, vParent);

        ((ViewGroup) act.getWindow().getDecorView()).addView(vParent);
        vgContents.setVisibility(View.INVISIBLE);

        initHandler();
    }

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    private void initHandler(){
        list = new ArrayList<>();
        list.add(new AnimModel(900, vg.findViewById(R.id.fullpopupSpecialCleaningPromo_tvText1)));
        list.add(new AnimModel(900, vg.findViewById(R.id.fullpopupSpecialCleaningPromo_tvText2)));
        list.add(new AnimModel(900, vg.findViewById(R.id.fullpopupSpecialCleaningPromo_vCleaingItem1), 1));
        list.add(new AnimModel(80, vg.findViewById(R.id.fullpopupSpecialCleaningPromo_vCleaingItem2), 1));
        list.add(new AnimModel(80, vg.findViewById(R.id.fullpopupSpecialCleaningPromo_vCleaingItem3), 1));
        list.add(new AnimModel(80, vg.findViewById(R.id.fullpopupSpecialCleaningPromo_vCleaingItem4), 1));
        list.add(new AnimModel(80, vg.findViewById(R.id.fullpopupSpecialCleaningPromo_vCleaingItem5), 1));
        list.add(new AnimModel(80, vg.findViewById(R.id.fullpopupSpecialCleaningPromo_vCleaingItem6), 1));
        list.add(new AnimModel(80, vg.findViewById(R.id.fullpopupSpecialCleaningPromo_vCleaingItem7), 1));
        list.add(new AnimModel(80, vg.findViewById(R.id.fullpopupSpecialCleaningPromo_vCleaingItem8), 1));
        list.add(new AnimModel(80, vg.findViewById(R.id.fullpopupSpecialCleaningPromo_vCleaingItem9), 1));
        list.add(new AnimModel(80, vg.findViewById(R.id.fullpopupSpecialCleaningPromo_vCleaingItem10), 1));
        list.add(new AnimModel(900, vg.findViewById(R.id.fullpopupSpecialCleaningPromo_tvText3)));
        list.add(new AnimModel(900, vg.findViewById(R.id.fullpopupSpecialCleaningPromo_vgButtons)));

        handler = new Handler(){

            @Override
            public void dispatchMessage(Message msg) {
                int pos = (int)msg.obj;


                if(list.size() <= pos){
                    return;
                }

                AnimModel m = list.get(pos);
                AnimModel mNext = null;
                if(list.size() <= pos +1){
                    ;
                }else{
                    mNext = list.get(pos+1);
                }

                m.vItem.setVisibility(View.VISIBLE);
                if(m.anim == 1){
                    m.vItem.setTranslationX(m.vItem.getX() * 2);
                    m.vItem.animate().translationX(0).setDuration(500).start();

                }

                if(mNext != null){
                    Message message = new Message();
                    message.obj = pos+1;
                    message.what = 0;
                    handler.sendMessageDelayed(message, mNext.delay);
                }else{
                    ;
                }

            }
        };
    }

    private void showAnimation(){
        for(AnimModel m : list){
            m.vItem.setVisibility(View.GONE);
        }

        handler.removeMessages(0);

        Message message = new Message();
        message.what = 0;
        message.obj = 0;
        handler.sendMessage(message);
    }

    @OnClick(R.id.fullpopupSpecialCleaningPromo_vbtnReq)
    public void onOk(View view){
        hide();
        clickListener.onOkClick();
    }


    public void show(){
        on = true;
        showMain();
        showAnimation();
    }

    public void hide(){
        on = false;
        handler.removeMessages(0);
        hideMain();
//        vbtnReq.clearAnimation();
    }

    void showMain(){
        showReveal(vgContents);
    }

    void hideMain(){
        hideReveal(vgContents);
    }

    void showReveal(View vTarget) {

        View myView = vTarget;
        myView.setVisibility(View.VISIBLE);
        // get the center for the clipping circle
        int cx = vTarget.getMeasuredWidth() / 2;
        int cy = vTarget.getMeasuredHeight() / 2;

        // get the final radius for the clipping circle
        int dx = Math.max(cx, vTarget.getMeasuredWidth() - cx);
        int dy = Math.max(cy, vTarget.getMeasuredHeight() - cy);
        float finalRadius = (float) Math.hypot(dx, dy);

        // Android native animator
        Animator animator =
                ViewAnimationUtils.createCircularReveal(myView, cx, cy, 0, finalRadius);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.setDuration(400);

        animator.start();
    }

    void hideReveal(View vTarget) {
        View myView = vTarget;

        // get the center for the clipping circle
        int cx = vTarget.getMeasuredWidth() / 2;
        int cy = vTarget.getMeasuredHeight() / 2;

        // get the final radius for the clipping circle
        int dx = Math.max(cx, vTarget.getMeasuredWidth() - cx);
        int dy = Math.max(cy, vTarget.getMeasuredHeight() - cy);
        float finalRadius = (float) Math.hypot(dx, dy);

        // Android native animator
        Animator animator =
                ViewAnimationUtils.createCircularReveal(myView, cx, cy, finalRadius, 0);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.setDuration(400);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                vgContents.setVisibility(View.INVISIBLE);
            }
        });
        animator.start();
    }

    class AnimModel{
        int delay;
        View vItem;
        int anim = 0;

        public AnimModel(int delay, View vItem) {
            this.delay = delay;
            this.vItem = vItem;
        }

        public AnimModel(int delay, View vItem, int anim) {
            this.delay = delay;
            this.vItem = vItem;
            this.anim = anim;
        }
    }

    public interface ClickListener{
        void onOkClick();
    }
}
