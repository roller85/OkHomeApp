package id.co.okhome.okhomeapp.lib.pollmanager.model;

/**
 * Created by josong on 2016-12-30.
 */
public class SubCheckItem {
    public boolean isChecked = false;
    public String item;

    public SubCheckItem(boolean isChecked, String item) {
        this.isChecked = isChecked;
        this.item = item;
    }

    public SubCheckItem(String item) {
        this.item = item;
    }

    public SubCheckItem() {
    }
}
