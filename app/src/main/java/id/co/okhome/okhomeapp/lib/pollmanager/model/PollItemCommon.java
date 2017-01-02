package id.co.okhome.okhomeapp.lib.pollmanager.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by josong on 2016-12-30.
 */
public class PollItemCommon {
    public static String TYPE_RADIO = "RADIO";
    public static String TYPE_CHECK = "CHECK";
    public static String TYPE_INPUT = "INPUT";

    public String type = TYPE_INPUT; // Or CHECK
    public String title = "";
    public String hint = ""; //인풋용
    public List<SubCheckItem> listChkItems = new ArrayList<>(); //체크박스용

    public PollItemCommon(String hint) {
        this.hint = hint;
    }

    public PollItemCommon() {
    }
}
