package id.co.okhome.okhomeapp.lib.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import id.co.okhome.okhomeapp.R;

public class JoDialog extends Dialog{

    ViewGroup vgContainer;
    View contentView;

    public JoDialog(Context context) {
        super(context);
        //기본설정
        init();

        View vParentView = LayoutInflater.from(context).inflate(R.layout.dialog_bg, null);
        vgContainer = (ViewGroup)vParentView.findViewById(R.id.dialogBg_vgContent);
        setContentView(vParentView);

    }

    private void init(){
        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN,
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN );
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCancelable(true);
    }

    //뷰 세팅
    public void setView(View v){
        vgContainer.removeAllViews();
        vgContainer.addView(v);
    }

}
