package id.co.okhome.okhomeapp.view.dialog;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.mrjodev.jorecyclermanager.JoRecyclerAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.okhome.okhomeapp.R;
import id.co.okhome.okhomeapp.lib.dialog.ViewDialog;
import id.co.okhome.okhomeapp.model.AddressItemModel;
import id.co.okhome.okhomeapp.view.viewholder.AddressItemHolder;
import id.co.okhome.okhomeapp.view.viewholder.BlankHolder;

/**
 * Created by josongmin on 2016-08-09.
 */

public class CommonAddressItemDialog extends ViewDialog{

    @BindView(R.id.dialogCommonInputbox_tvTitle)
    TextView tvTitle;

    @BindView(R.id.dialogCommonList_rcv)
    RecyclerView rcv;

    String title;
    OnAddressClick onAddressClick;
    List<AddressItemModel> listItems;
    JoRecyclerAdapter adapter;


    public CommonAddressItemDialog(String title, List<AddressItemModel> listItems, OnAddressClick onAddressClick) {
        this.title = title;
        this.listItems = listItems;
        this.onAddressClick = onAddressClick;
    }

    @Override
    public View getView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.dialog_common_list, null);
    }

    @Override
    public void onViewCreated() {
        ButterKnife.bind(this, getDecorView());
        tvTitle.setText(title);

        adapter = new JoRecyclerAdapter(
                new JoRecyclerAdapter.Params()
                        .setRecyclerView(rcv)
                        .addParam("CALLER", this)
                        .setItemViewHolderCls(AddressItemHolder.class)
                        .setHeaderViewHolderCls(BlankHolder.class)
                        .setFooterViewHolderCls(BlankHolder.class)

        );

        adapter.addFooterItem("");
        adapter.setListItems(listItems);
    }

    //콜백됨
    public void onItemClick(AddressItemModel item){
        onAddressClick.onAddressClick(item);
        dismiss();
    }

    public interface OnAddressClick{
        public void onAddressClick(AddressItemModel addressItemModel);
    }

}
