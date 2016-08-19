package id.co.okhome.okhomeapp.lib;

import android.app.Activity;
import android.view.View;

import id.co.okhome.okhomeapp.R;

/**
 * Created by josongmin on 2016-08-17.
 */

public class OkHomeUtil {
    public static final  void setBackbtnListener(final Activity act){
        act.findViewById(R.id.common_vbtnBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                act.finish();;
            }
        });

    }
}
