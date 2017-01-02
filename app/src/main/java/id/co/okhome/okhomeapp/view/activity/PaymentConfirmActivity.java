package id.co.okhome.okhomeapp.view.activity;

import android.os.Bundle;

import butterknife.ButterKnife;
import id.co.okhome.okhomeapp.R;
import id.co.okhome.okhomeapp.lib.OkHomeActivityParent;

public class PaymentConfirmActivity extends OkHomeActivityParent {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_confirm);
        ButterKnife.bind(this);
    }
}
