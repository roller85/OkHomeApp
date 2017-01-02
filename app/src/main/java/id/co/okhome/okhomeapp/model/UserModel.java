package id.co.okhome.okhomeapp.model;

import java.util.List;

import id.co.okhome.okhomeapp.lib.Util;

import static java.lang.Integer.parseInt;

/**
 * Created by josongmin on 2016-08-26.
 */
public class UserModel {
    public String uuid;
    public boolean isNewMember = false;
    public String id, name, email, phone, credit, creditBonus = "0", photoUrl, delYN, pushYN, soundYN, vibeYN, logoutYN, joinDate;
    public String cleaningCount = "0";
    public String accountType;
    public List<HomeModel> listHomeModel;

    public int getTotalCredit(){
        return Integer.parseInt(credit) + Integer.parseInt(creditBonus);
    }

    public String getCredit() {
        if(credit == null || credit.equals("") || credit.equals("0")){
            return "0";
        }else{
            return Util.getMoneyString(parseInt(credit), '.');
        }
    }
}
