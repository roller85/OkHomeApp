package id.co.okhome.okhomeapp.lib;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.view.View;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by josongmin on 2016-07-28.
 */

public class DrawerLayoutController {

    private static Map<FragmentActivity, DrawerLayoutController> mapInstance = new HashMap<FragmentActivity, DrawerLayoutController>();

    //편의용
    public final static DrawerLayoutController with(FragmentActivity activity){
        DrawerLayoutController drawerLayoutController = mapInstance.get(activity);
        return drawerLayoutController;
    }

    public final static DrawerLayoutController with(FragmentActivity activity, int contentId, DrawerLayout drawerLayout){
        DrawerLayoutController drawerLayoutController = mapInstance.get(activity);
        if(drawerLayoutController == null){
            drawerLayoutController = new DrawerLayoutController(activity, contentId, drawerLayout);
            mapInstance.put(activity, drawerLayoutController);
        }

        return drawerLayoutController;
    }


    private FragmentActivity activity;
    private int contentId;
    private DrawerLayout drawerLayout;
    private FragmentManager fm;

    private ViewComponentInitiator viewComponentInitiator = null;

    public DrawerLayoutController(FragmentActivity activity, int contentId, DrawerLayout drawerLayout){
        this.activity = activity;
        this.drawerLayout = drawerLayout;
        this.contentId = contentId;
        fm = activity.getSupportFragmentManager();
    }

    public DrawerLayoutController setViewComponentInitiator(ViewComponentInitiator viewComponentInitiator){
        this.viewComponentInitiator = viewComponentInitiator;
        return this;
    }

    public ViewComponentInitiator getViewComponentInitiator(){
        return viewComponentInitiator;
    }

    //레이아웃 내용 변경된 후
    public DrawerLayoutController commit(){
        viewComponentInitiator.initDrawerContent(activity, this, drawerLayout, (View)drawerLayout.getChildAt(1));
        return this;
    }

    public void show(Fragment fragment){
        show(fragment, true);
    }

    public void show(final Fragment fragment, final boolean showContents){


        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                fm.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                final FragmentTransaction tr = fm.beginTransaction();
                tr.replace(contentId, fragment);
//        tr.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
//                tr.addToBackStack(null);
                tr.commit();
            }
        }, 280);

        if(showContents){
            drawerLayout.closeDrawers();
        }

    }

    public interface ViewComponentInitiator{
        public void initDrawerContent(FragmentActivity activity, DrawerLayoutController drawerLayoutController, DrawerLayout drawerLayout, View vParent);
    }

}
