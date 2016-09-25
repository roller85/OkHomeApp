package id.co.okhome.okhomeapp.lib;

import android.app.ProgressDialog;
import android.content.Context;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by josongmin on 2016-08-28.
 */

public class ProgressDialogController {
    private static final Map<Integer, ProgressDialog> mapInstance = new HashMap<>();

    public static int show(Context context){
        int id = show(context, "Loading");
        return id;
    }

    public static int show(Context context, String message){
        int id = new Random().nextInt(Integer.MAX_VALUE);
        ProgressDialog pd = ProgressDialog.show(context, null, message);
        pd.setCancelable(true);
        mapInstance.put(id, pd);

        pd.show();
        return id;
    }

    public static final void dismiss(int id){
        mapInstance.get(id).dismiss();
        mapInstance.remove(id);
    }
}
