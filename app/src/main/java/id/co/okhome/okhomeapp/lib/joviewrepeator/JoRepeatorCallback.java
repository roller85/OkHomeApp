package id.co.okhome.okhomeapp.lib.joviewrepeator;

import android.view.View;

/**
 * Created by josongmin on 2016-09-06.
 */

public abstract class JoRepeatorCallback<E> {
    JoViewRepeator viewRepeator;
    public void setViewRepeator(JoViewRepeator viewRepeator){
        this.viewRepeator = viewRepeator;
    }

    public JoViewRepeator getViewRepeator() {
        return viewRepeator;
    }

    public  View getHeaderView(){return null;}
    public  View getFooterView(){return null;}
    public abstract void onBind(View v, E model);
}
