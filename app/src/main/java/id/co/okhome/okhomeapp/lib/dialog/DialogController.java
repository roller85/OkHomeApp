package id.co.okhome.okhomeapp.lib.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;

import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

/**
 * Created by josongmin on 2016-08-09.
 */

public class DialogController {
    public final static DialogPlus showBottomDialog(final Context context, ViewDialog viewDialog){
        return showDialog(context, viewDialog, Gravity.BOTTOM);
    }

    public final static DialogPlus showTopDialog(final Context context, ViewDialog viewDialog){
        return showDialog(context, viewDialog, Gravity.TOP);
    }

    public final static JoDialog showCenterDialog(final Context context, ViewDialog viewDialog){
        return showPlainDialog(context, viewDialog, Gravity.CENTER);
    }

    public final static DialogPlus showDialog(final Context context, ViewDialog viewDialog, int gravity){
        viewDialog.setContext(context);
        viewDialog.init();

        DialogPlus dialog = DialogPlus.newDialog(context)
                .setContentHolder(new ViewHolder(viewDialog.getDecorView()))
                .setContentBackgroundResource(0)
                .setGravity(gravity)
                .setCancelable(true)
                .create();

        viewDialog.setDialog(dialog);
        viewDialog.onViewCreated();
        dialog.show();
        return dialog;
    }

    public final static JoDialog showPlainDialog(final Context context, ViewDialog viewDialog, int gravity){
        viewDialog.setContext(context);
        viewDialog.init();

        JoDialog joDialog = new JoDialog(context);
        joDialog.setView(viewDialog.getDecorView());

        viewDialog.setDialog(joDialog);
        viewDialog.onViewCreated();
        joDialog.show();
        return joDialog;
    }

    public final static void dismissDialog(Object dialog){
        if(dialog instanceof Dialog){
            ((Dialog) dialog).dismiss();
        }else if(dialog instanceof DialogPlus){
            ((DialogPlus) dialog).dismiss();
        }
    }
}
