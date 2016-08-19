package id.co.okhome.okhomeapp.lib.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.orhanobut.dialogplus.DialogPlus;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by josongmin on 2016-08-09.
 */

public abstract class ViewDialog {
    Context context;
    View view;
    Map<String, Object> params = new HashMap<>();
    DialogCommonCallback commonCallback;
    Object dialog = null;

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

    public ViewDialog setParams(Map<String, Object> params) {
        this.params = params;
        return this;
    }

    public ViewDialog addParam(String key, Object value) {
        params.put(key, value);
        return this;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setContext(Context context){
        this.context = context;
    }

    public void init(){
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = getView(inflater);
        this.view = v;

    }

    public void setDialog(Object dialog) {
        this.dialog = dialog;
    }

    public Object getDialog() {
        return dialog;
    }

    public View getDecorView(){
        return view;
    }

    public abstract View getView(LayoutInflater inflater);
    public abstract void onViewCreated();


    public interface DialogCommonCallback{
        public void onCallback(Object dialog, Map<String, Object> params);
    }

    public void dismiss(){
        if(getDialog() instanceof Dialog){
            ((Dialog) getDialog()).dismiss();
        }else if(getDialog() instanceof DialogPlus){
            ((DialogPlus) getDialog()).dismiss();
        }

    }
}
