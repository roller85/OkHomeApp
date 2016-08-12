package id.co.okhome.okhomeapp.lib.dialogplus;

import android.content.Context;

import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

/**
 * Created by josongmin on 2016-08-09.
 */

public class DialogController {
    public final static DialogPlus showBottomDialog(final Context context, ViewDialog viewDialog){
        viewDialog.setContext(context);
        viewDialog.init();


        DialogPlus dialog = DialogPlus.newDialog(context)
                .setContentHolder(new ViewHolder(viewDialog.getDecorView()))
                .setContentBackgroundResource(0)
                .setCancelable(true)
                .create();

        viewDialog.setDialogPlus(dialog);
        viewDialog.onViewCreated();
        dialog.show();
        return dialog;
    }
}
