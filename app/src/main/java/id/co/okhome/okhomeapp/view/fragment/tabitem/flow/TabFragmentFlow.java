package id.co.okhome.okhomeapp.view.fragment.tabitem.flow;

import android.view.View;
import android.widget.ImageView;

/**
 * Created by josongmin on 2016-10-16.
 */

public interface TabFragmentFlow {
    public String getTitle();
    public View.OnClickListener onTabSettingClick(ImageView ivIcon);
}
