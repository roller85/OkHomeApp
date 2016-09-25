package id.co.okhome.okhomeapp.lib.retrofit.restmodel;

/**
 * Created by josongmin on 2016-02-17.
 */
public class ErrorModel {
    public String code, message;
    public Object obj;

    public ErrorModel(String code, String message, Object obj) {
        this.code = code;
        this.message = message;
        this.obj = obj;
    }
}
