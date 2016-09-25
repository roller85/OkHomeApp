package id.co.okhome.okhomeapp.view.dialog;

import android.view.LayoutInflater;
import android.view.View;

import butterknife.ButterKnife;
import id.co.okhome.okhomeapp.R;
import id.co.okhome.okhomeapp.lib.dialog.ViewDialog;

/**
 * Created by josongmin on 2016-08-09.
 */

public class ChooseCleaningType2Dialog extends ViewDialog{


    DialogCommonCallback callback;
    public ChooseCleaningType2Dialog(DialogCommonCallback callback) {
        this.callback = callback;
    }

    @Override
    public View getView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.dialog_choose_cleaning_type2, null);
    }

    @Override
    public void onViewCreated() {
        ButterKnife.bind(this, getDecorView());
//        AnimatedTooltipTextController.with(tvSubText)
//                .setArrTooltips(new String[]{"We recommend you periodic cleaning", "Feel comportable and cozy", "How can i help you"})
//                .start();
    }

}
