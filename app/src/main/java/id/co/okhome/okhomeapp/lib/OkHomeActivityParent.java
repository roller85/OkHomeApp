package id.co.okhome.okhomeapp.lib;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by josongmin on 2016-08-19.
 */

public class OkHomeActivityParent extends AppCompatActivity{

    public static List<OkHomeActivityParent> listActivities = new ArrayList();

    public static void inputNewActivity(OkHomeActivityParent activity){
        listActivities.add(activity);
    }

    public static void destroyActivity(OkHomeActivityParent activity){
        listActivities.remove(activity);
    }

    public static void finishAllActivities(){
        finishAllActivitiesWithout(null);
    }

    public static void finishAllActivitiesWithout(OkHomeActivityParent activity){
        List<OkHomeActivityParent> listRemoved = new ArrayList<>();

        for(OkHomeActivityParent act : listActivities){
            if(activity == null){
                listRemoved.add(act);
            }else{
                if(act != activity){
                    listRemoved.add(act);
                }
            }
        }

        for(OkHomeActivityParent act : listRemoved){
            act.finish();
        }
    }

//
//    @Override
//    protected void attachBaseContext(Context newBase) {
//        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
//    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        inputNewActivity(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        inputNewActivity(this);
        super.onCreate(savedInstanceState, persistentState);

    }

    @Override
    protected void onDestroy() {
        destroyActivity(this);
        super.onDestroy();
    }
}
