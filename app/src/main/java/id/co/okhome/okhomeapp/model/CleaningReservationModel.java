package id.co.okhome.okhomeapp.model;

import java.util.List;

/**
 * Created by josongmin on 2016-09-14.
 */
public class CleaningReservationModel {
    public String
            rownum, id, cTicketId, orderNo, userId, homeId, managerId,
            basicCleaningPrice,
            status, spcCleaningIds, when, whenDate, duration, expiryDate, insertDate;

    public int price;
    public List listExtraCleaning = null;
}