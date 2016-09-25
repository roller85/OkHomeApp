package id.co.okhome.okhomeapp.model;

/**
 * Created by josongmin on 2016-09-08.
 */

public class CleaningModel {
    public String imgUrl, name, id;
    public int hour, price;
    public boolean isStatic = false;
    public boolean isChecked = false;

    public CleaningModel() {
    }

    public CleaningModel(String id, String name, int hour, int price, String imgUrl, boolean isStatic) {
        this.imgUrl = imgUrl;
        this.hour = hour;
        this.price = price;
        this.name = name;
        this.id = id;
        this.isStatic = isStatic;
    }
}
