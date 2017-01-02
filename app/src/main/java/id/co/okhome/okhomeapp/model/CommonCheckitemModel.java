package id.co.okhome.okhomeapp.model;

import java.util.List;

/**
 * Created by josongmin on 2016-09-07.
 */

public class CommonCheckitemModel {
    public String text, tag;

    //해당 선택버튼이 몇번째 위치해 있는지
    public static int getFieldPosition(String tag, String field){
        String[] fields = null;

        if(tag.equals(HomeModel.TAG_HOMETYPE)){
            fields = new String[]{HomeModel.HOMETYPE_APT, HomeModel.HOMETYPE_HOUSE, HomeModel.HOMETYPE_STUDIO};
        }

        else if(tag.equals(HomeModel.TAG_HOMESIZE_HOME_APT)){
            fields = new String[]{HomeModel.HOMESIZE_100LESS, HomeModel.HOMESIZE_100_150, HomeModel.HOMESIZE_150_200, HomeModel.HOMESIZE_200_250
                    , HomeModel.HOMESIZE_250MORE, HomeModel.HOMESIZE_DONTKNOW};
        }

        else if(tag.equals(HomeModel.TAG_HOMESIZE_STUDIO)){
            fields = new String[]{HomeModel.HOMESIZE_50LESS, HomeModel.HOMESIZE_50_100, HomeModel.HOMESIZE_DONTKNOW};
        }

        else if(tag.equals(HomeModel.TAG_PET)){
            fields = new String[]{HomeModel.HOMEPET_DOG, HomeModel.HOMEPET_CAT, HomeModel.HOMEPET_ETC, HomeModel.HOMEPET_NOPE};
        }

        else if(tag.equals(HomeModel.TAG_ROOMCOUNT)){
            fields = new String[]{HomeModel.COUNT_1, HomeModel.COUNT_2, HomeModel.COUNT_3, HomeModel.COUNT_4MORE};
        }

        else if(tag.equals(HomeModel.TAG_TOILETCOUNT)){
            fields = new String[]{HomeModel.COUNT_1, HomeModel.COUNT_2, HomeModel.COUNT_3, HomeModel.COUNT_4MORE};
        }

        if(fields == null){
            ;
        }else{
            for(int i = 0; i < fields.length; i++){
                String v = fields[i];
                if(v.equals(field)){
                    return i;
                }
            }
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
