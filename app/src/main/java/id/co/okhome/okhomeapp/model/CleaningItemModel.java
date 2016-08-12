package id.co.okhome.okhomeapp.model;

/**
 * Created by josongmin on 2016-08-10.
 */

public class CleaningItemModel{
    public String title, desc, price, hour;
    public int imgResId;
    public boolean isChecked = false;

    public CleaningItemModel() {
    }

    public CleaningItemModel(int imgResId, String title, String desc, String price, String hour){
        this.imgResId = imgResId;
        this.title = title;
        this.desc = desc;
        this.hour = hour;
        this.price = price;
    }
}