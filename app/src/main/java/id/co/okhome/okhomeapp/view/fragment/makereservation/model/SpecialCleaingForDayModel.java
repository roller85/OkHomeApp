package id.co.okhome.okhomeapp.view.fragment.makereservation.model;

import org.joda.time.LocalDateTime;

import java.util.List;

import id.co.okhome.okhomeapp.model.SpcCleaningModel;

/**
 * Created by josong on 2017-01-02.
 */

public class SpecialCleaingForDayModel{
    public LocalDateTime jodaDate;
    public int pos;
    public boolean hasExtra = false;
    public List<SpcCleaningModel> listSpcCleaning;
    public SpecialCleaingForDayModel(LocalDateTime jodaDate, int pos) {
        this.jodaDate = jodaDate;
        this.pos = pos;
    }
}