package id.co.okhome.okhomeapp.lib;

import android.util.SparseArray;
import android.view.View;

/**
 * Created by josongmin on 2016-09-28.
 */

public class ViewHolderUtil {
    /**parentView의 태그는 비워둬야한다.*/
    public static <T extends View> T getView(View parentView, int id) {
        SparseArray<View> viewHolder = (SparseArray<View>) parentView.getTag();
        if (viewHolder == null) {
            viewHolder = new SparseArray<View>();
            parentView.setTag(viewHolder);
        }
        View childView = viewHolder.get(id);
        if (childView == null) {
            childView = parentView.findViewById(id);
            viewHolder.put(id, childView);
        }
        return (T) childView;
    }

    public static boolean hasView(View parentView, int id) {
        SparseArray<View> viewHolder = (SparseArray<View>) parentView.getTag();
        if (viewHolder == null) {
            viewHolder = new SparseArray<View>();
            parentView.setTag(viewHolder);
        }
        View childView = viewHolder.get(id);
        if (childView == null) {
            return false;
        }else{
            return true;
        }
    }
}
