package id.co.okhome.okhomeapp.lib.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;

import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.DialogPlusBuilder;
import com.orhanobut.dialogplus.OnCancelListener;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.List;

import id.co.okhome.okhomeapp.R;
import id.co.okhome.okhomeapp.lib.JoSharedPreference;
import id.co.okhome.okhomeapp.lib.ProgressDialogController;
import id.co.okhome.okhomeapp.lib.retrofit.RetrofitCallback;
import id.co.okhome.okhomeapp.model.SpcCleaningModel;
import id.co.okhome.okhomeapp.restclient.RestClient;
import id.co.okhome.okhomeapp.view.dialog.ChooseCleaningGridDialog;
import id.co.okhome.okhomeapp.view.dialog.CommonListDialog;
import id.co.okhome.okhomeapp.view.dialog.CommonTextDialog;

/**
 * Created by josongmin on 2016-08-09.
 */

public class DialogController {

    public final static String TAG_CLICK = "ONCLICK";

    //그리그 청소 선택팝업 열기
    public final static void showChooseExtraCleaningList(final Context context, final int defDuration, final List<SpcCleaningModel> defListSet, final ChooseCleaningGridDialog.OnExtraCleaningChoosedListener onExtraCleaningChoosedListener){

        final Handler handlerShowDialog = new Handler(){
            @Override
            public void dispatchMessage(Message msg) {
                List<SpcCleaningModel> listCleaningItem = (List<SpcCleaningModel>)msg.obj;
                DialogController.showBottomDialog(context, new ChooseCleaningGridDialog(defDuration, listCleaningItem, defListSet, onExtraCleaningChoosedListener));
            }
        };

        List<SpcCleaningModel> listCleaningItem = null;
        List list = JoSharedPreference.with().get("ExtraCleaningList");

        final Message m = new Message();
        m.what = 0;

        if(list != null){
            m.obj = list;
            handlerShowDialog.sendMessage(m);

        }else{
            final int pId = ProgressDialogController.show(context, "잠시만 기다리세요!");

            RestClient.getCleaningClient().getCleaningList().enqueue(new RetrofitCallback<List<SpcCleaningModel>>() {

                @Override
                public void onFinish() {
                    super.onFinish();
                    ProgressDialogController.dismiss(pId);
                }

                @Override
                public void onSuccess(List<SpcCleaningModel> result) {
                    JoSharedPreference.with().push("ExtraCleaningList", result);
                    m.obj = result;
                    handlerShowDialog.sendMessage(m);
                }
            });
        }
    }

    public final static CommonTextDialog showAlertDialog(Context context, String title, String description){
        CommonTextDialog commonTextDialog = new CommonTextDialog(title, description, true, null);
        showCenterDialog(context, commonTextDialog);
        return commonTextDialog;

    }

    public final static CommonTextDialog showAlertDialog(Context context, String title, String description, boolean singleBtn){
        CommonTextDialog commonTextDialog = new CommonTextDialog(title, description, singleBtn, null);
        showCenterDialog(context, commonTextDialog);
        return commonTextDialog;

    }

    public final static CommonTextDialog showAlertDialog(Context context, String title, String description, boolean singleBtn, ViewDialog.DialogCommonCallback callback){
        CommonTextDialog commonTextDialog = new CommonTextDialog(title, description, singleBtn, callback);
        showCenterDialog(context, commonTextDialog);
        return commonTextDialog;
    }

    public final static DialogPlus showListDialog(Context context, String title, String[] arr, int height, ViewDialog.DialogCommonCallback callback){
        return showBottomDialog(context, height, new CommonListDialog(title, arr,  callback));
    }


    public final static DialogPlus showBottomDialog(final Context context, ViewDialog viewDialog, OnCancelListener onCancelListener){
        return showDialog(context, viewDialog, Gravity.BOTTOM, 0, onCancelListener);
    }

    public final static DialogPlus showBottomDialog(final Context context, ViewDialog viewDialog, int height, OnCancelListener onCancelListener){
        return showDialog(context, viewDialog, Gravity.BOTTOM, height, onCancelListener);
    }

    public final static DialogPlus showBottomDialog(final Context context, ViewDialog viewDialog){
        return showDialog(context, viewDialog, Gravity.BOTTOM, 0, null);
    }

    public final static DialogPlus showBottomDialog(final Context context, int height, ViewDialog viewDialog){
        return showDialog(context, viewDialog, Gravity.BOTTOM, height, null);
    }

    public final static DialogPlus showTopDialog(final Context context, ViewDialog viewDialog){
        return showDialog(context, viewDialog, Gravity.TOP, 0, null);
    }

    public final static JoDialog showCenterDialog(final Context context, ViewDialog viewDialog){
        return showPlainDialog(context, viewDialog, Gravity.CENTER);
    }



    public final static DialogPlus showDialog(final Context context, ViewDialog viewDialog, int gravity, int height, OnCancelListener onCancelListener){
        viewDialog.setContext(context);
        viewDialog.init();

        DialogPlusBuilder dialogPlusBuilder = DialogPlus.newDialog(context)
                .setContentHolder(new ViewHolder(viewDialog.getDecorView()))
                .setContentBackgroundResource(android.R.color.white)
                .setGravity(gravity)
                .setCancelable(true)
                .setOverlayBackgroundResource(R.color.transparentBlack)
                .setOnCancelListener(onCancelListener);

        if(height > 0){
            dialogPlusBuilder.setContentHeight(height);
        }

        DialogPlus dialog = dialogPlusBuilder.create();

        viewDialog.setDialog(dialog);
        viewDialog.onViewCreated();
        viewDialog.show();
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
        viewDialog.show();
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
