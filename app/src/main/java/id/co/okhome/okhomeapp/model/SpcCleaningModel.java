package id.co.okhome.okhomeapp.model;

/**
 * Created by josongmin on 2016-09-08.
 */

public class SpcCleaningModel {

    public static final int ID_CELLING = 1;
    public static final int ID_WINDOW = 2;
    public static final int ID_BALCONY = 3;
    public static final int ID_PILL = 4;
    public static final int ID_REFRIGERATOR = 5;
    public static final int ID_TIDYUP = 6;
    public static final int ID_BATHROOM = 7;
    public static final int ID_KITCHEN = 8;
    public static final int ID_WALL = 9;
    public static final int ID_VENTILATOR = 10;

    public String imgUrl, name, id;
    public int hour = 0, price = 0;
    public boolean isStatic = false;
    public boolean isChecked = false;

    public SpcCleaningModel() {
    }

    public SpcCleaningModel(String id, String name, int hour, int price, String imgUrl, boolean isStatic) {
        this.imgUrl = imgUrl;
        this.hour = hour;
        this.price = price;
        this.name = name;
        this.id = id;
        this.isStatic = isStatic;
    }
}
