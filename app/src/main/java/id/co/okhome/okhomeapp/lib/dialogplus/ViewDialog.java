package id.co.okhome.okhomeapp.lib.dialogplus;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.orhanobut.dialogplus.DialogPlus;

import java.util.Map;

/**
 * Created by josongmin on 2016-08-09.
 */

public abstract class ViewDialog {
    Context context;
    View view;
    Map<String, Object> params = null;
    DialogPlus dialogPlus;
    DialogCommonCallback commonCallback;

    public ViewDialog(Context context) {
        this.context = context;
    }
    public ViewDialog() {

    }

    public ViewDialog setCommonCallback(DialogCommonCallback commonCallback) {
        this.commonCallback = commonCallback;
        return this;
    }

    public DialogCommonCallback getCommonCallback() {
        return commonCallback;
    }

    public Context getContext() {
        return context;
    }

    public void setDialogPlus(DialogPlus dialogPlus) {
        this.dialogPlus = dialogPlus;
    }

    public DialogPlus getDialogPlus() {
        return dialogPlus;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    public void setContext(Context context){
        this.context = context;
    }

    public void init(){
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = getView(inflater);
        this.view = v;

    }

    public View getDecorView(){
        return view;
    }

    public abstract View getView(LayoutInflater inflater);
    public abstract void onViewCreated();


    public interface DialogCommonCallback{
        public void onCallback(DialogPlus dialogPlus, Map<String, Object> params);
    }
}
