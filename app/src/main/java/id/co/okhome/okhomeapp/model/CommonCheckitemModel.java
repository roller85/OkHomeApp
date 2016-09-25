package id.co.okhome.okhomeapp.model;

import java.util.List;

/**
 * Created by josongmin on 2016-09-07.
 */

public class CommonCheckitemModel {
    public String text, tag;

    public final static String HOMETYPE_APARTMENT       = "0";
    public final static String HOMETYPE_HOUSE           = "1";
    public final static String HOMETYPE_STUDIO          = "2";
    public final static String HOMESIZE_100LESS         = "0";
    public final static String HOMETYPE_200LESS         = "1";
    public final static String HOMETYPE_300MORE         = "2";
    public final static String PETTYPE_DOG              = "0";
    public final static String PETTYPE_CAT              = "1";
    public final static String PETTYPE_ETC              = "2";
    public final static String COUNT_4_OR_MORE          = "F";
    public final static String NOPE                     = "-1";



    public static int getFieldPosition(String field){
        if(field.equals(HOMETYPE_APARTMENT) || field.equals(PETTYPE_DOG) || field.equals(HOMESIZE_100LESS))
            return 0;

        else if(field.equals(HOMETYPE_HOUSE) || field.equals(HOMETYPE_200LESS) || field.equals(PETTYPE_CAT)){
            return 1;
        }

        else if(field.equals(HOMETYPE_STUDIO) || field.equals(HOMETYPE_300MORE) || field.equals(PETTYPE_ETC)){
            return 2;
        }
        else if(field.equals(COUNT_4_OR_MORE) || field.equals(NOPE)){
            return 3;
        }
        return -1;
    }

    public CommonCheckitemModel(String text, String tag) {
        this.text = text;
        this.tag = tag;
    }

    public CommonCheckitemModel(String text) {
        this.text = text;
        this.tag = text;
    }

    public static String getCommaValue(List<CommonCheckitemModel> list){
        String value = "";
        for(CommonCheckitemModel m : list){
            value += "," + m.tag;
        }
        if (!value.equals("")) {
            value = value.substring(1);
        }
        return value;
    }
}
