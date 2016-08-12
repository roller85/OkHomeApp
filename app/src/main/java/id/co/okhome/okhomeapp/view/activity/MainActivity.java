package id.co.okhome.okhomeapp.view.activity;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.okhome.okhomeapp.R;
import id.co.okhome.okhomeapp.lib.DrawerLayoutController;
import id.co.okhome.okhomeapp.view.drawerlayout.MainDrawerViewComponentInitiator;
import id.co.okhome.okhomeapp.view.fragment.MakeReservationFragment;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    @BindView(R.id.actMain_dlContent)
    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        DrawerLayoutController
                .with(this, R.id.actMain_flContent, drawerLayout)
                .setViewComponentInitiator(new MainDrawerViewComponentInitiator())
                .commit()
                .show(new MakeReservationFragment());

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
        }
        DrawerLayoutController.with(this).show(new MakeReservationFragment());
    }
}
