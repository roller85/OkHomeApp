package id.co.okhome.okhomeapp.view.viewholder;

import android.view.View;
import android.widget.TextView;

import com.mrjodev.jorecyclermanager.JoViewHolder;
import com.mrjodev.jorecyclermanager.annotations.LayoutMatcher;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.okhome.okhomeapp.R;
import id.co.okhome.okhomeapp.view.dialog.CommonListDialog;

/**
 * Created by josongmin on 2016-08-17.
 */
@LayoutMatcher(layoutId = R.layout.item_string)
public class StringHolder extends JoViewHolder<String> implements View.OnClickListener{

    @BindView(R.id.itemString_tvText)
    TextView tvText;
    public StringHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void onViewCreated() {
        ButterKnife.bind(this, getView());
    }

    @Override
    public void onBind(String s, int pos, int absPos) {
        super.onBind(s, pos, absPos);
        tvText.setText(s);

        getView().setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String item = getModel();
        Object caller = getRcvParams().getParam("CALLER");
        if(caller instanceof CommonListDialog){
            ((CommonListDialog) caller).onItemClick(item);
        }
    }
}