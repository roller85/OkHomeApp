package id.co.okhome.okhomeapp.model;

import java.util.List;

/**
 * Created by josongmin on 2016-09-14.
 */
public class CleaningReservationModel {
    public String
            rownum, id, userId, homeId, managerId, periodicYN, counsultingYN,
            type, status, userComment, specialCleaningIds, when, duration, insertDate;

    public int price;
    public List listExtraCleaning = null;
}