package id.co.okhome.okhomeapp.view.activity;

import android.os.Bundle;

import butterknife.ButterKnife;
import id.co.okhome.okhomeapp.R;
import id.co.okhome.okhomeapp.gcm.OkhomeGcmController;
import id.co.okhome.okhomeapp.lib.OkHomeActivityParent;
import id.co.okhome.okhomeapp.lib.Util;

public class PushTestActivity extends OkHomeActivityParent {

    private OkhomeGcmController okhomeGcmController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push_test);

        ButterKnife.bind(this);

        okhomeGcmController = OkhomeGcmController.with(this)
                .setOkHomeGcmListener(new OkhomeGcmController.OkHomeGcmListener() {
                    @Override
                    public void onGcmTokenReady(String token) {
                        Util.showToast(PushTestActivity.this, token);
                    }
                });

        okhomeGcmController.requestGcmToken();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        okhomeGcmController.stop();

    }
}
