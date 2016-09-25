package id.co.okhome.okhomeapp.view.viewholder;

import android.view.View;
import android.widget.TextView;

import com.mrjodev.jorecyclermanager.JoViewHolder;
import com.mrjodev.jorecyclermanager.annotations.LayoutMatcher;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.okhome.okhomeapp.R;
import id.co.okhome.okhomeapp.model.AddressItemModel;
import id.co.okhome.okhomeapp.view.dialog.CommonAddressItemDialog;

/**
 * Created by josongmin on 2016-08-17.
 */
@LayoutMatcher(layoutId = R.layout.item_string)
public class AddressItemHolder extends JoViewHolder<AddressItemModel> implements View.OnClickListener{

    @BindView(R.id.itemString_tvText)
    TextView tvText;
    public AddressItemHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void onViewCreated() {
        ButterKnife.bind(this, getView());
    }

    @Override
    public void onBind(AddressItemModel s, int pos, int absPos) {
        super.onBind(s, pos, absPos);
        tvText.setText(s.value);

        getView().setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        AddressItemModel item = getModel();
        Object caller = getRcvParams().getParam("CALLER");
        if(caller instanceof CommonAddressItemDialog){
            ((CommonAddressItemDialog) caller).onItemClick(item);
        }
    }
}