package id.co.okhome.okhomeapp.lib;

import android.view.View;
import android.view.ViewGroup;

/**
 * Created by josongmin on 2016-08-18.
 */

public class AutoSlideViewController {
    View[] views;
    ViewGroup vgParent;
    public AutoSlideViewController(ViewGroup vgParent, View... views) {
        this.views = views;
        this.vgParent = vgParent;
    }


}
