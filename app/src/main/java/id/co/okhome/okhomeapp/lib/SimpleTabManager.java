package id.co.okhome.okhomeapp.lib;

import android.app.Activity;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by josong on 2016-12-16.
 */

public class SimpleTabManager {
    Activity activity;
    List<View> listViews = new ArrayList<>();
    Map<Integer, View> mapItems = new HashMap<>();
    Map<View, Integer> mapItemsReverse = new HashMap<>();
    SimpleTabListener tabListener;

    int pos = 0;
    int size = 0;
    public SimpleTabManager(Activity activity){
        this.activity = activity;
    }

    public void setTabListener(SimpleTabListener tabListener) {
        this.tabListener = tabListener;
    }

    public void addTab(View vItem){

        mapItems.put(size, vItem);
        mapItemsReverse.put(vItem, size);
        listViews.add(vItem);

        size++;
        vItem.setOnClickListener(onViewClickListener);

    }

    //클릭됨
    public void onItemClick(View vItem){
        int pos = mapItemsReverse.get(vItem);
        SimpleTabManager.this.pos = pos;

        for(View v : listViews){
            if(v == vItem){
                tabListener.onTabOn(mapItemsReverse.get(vItem), vItem);
            }else{
                tabListener.onTabOff(mapItemsReverse.get(vItem), vItem);
            }
        }
    }

    public int getPos() {
        return pos;
    }

    //아이템클릭리스너
    View.OnClickListener onViewClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onItemClick(v);
        }
    };

    public interface SimpleTabListener{
        void onTabOn(int pos, View v);
        void onTabOff(int pos, View v);
    }


}
