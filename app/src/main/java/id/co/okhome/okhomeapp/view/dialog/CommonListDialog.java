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
import id.co.okhome.okhomeapp.lib.Util;
import id.co.okhome.okhomeapp.lib.dialog.ViewDialog;
import id.co.okhome.okhomeapp.view.viewholder.BlankHolder;
import id.co.okhome.okhomeapp.view.viewholder.StringHolder;

/**
 * Created by josongmin on 2016-08-09.
 */

public class CommonListDialog extends ViewDialog{

    @BindView(R.id.dialogCommonInputbox_tvTitle)
    TextView tvTitle;

    @BindView(R.id.dialogCommonList_rcv)
    RecyclerView rcv;

    String title;
    DialogCommonCallback callback;
    List<String> listItems;
    JoRecyclerAdapter adapter;


    public CommonListDialog(String title, List<String> listItems, DialogCommonCallback callback) {
        this.title = title;
        this.listItems = listItems;
        this.callback = callback;
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
                        .setItemViewHolderCls(StringHolder.class)
                        .setHeaderViewHolderCls(BlankHolder.class)
                        .setFooterViewHolderCls(BlankHolder.class)

        );

        adapter.addFooterItem("");
        adapter.setListItems(listItems);

    }

    public void onItemClick(String item){
        callback.onCallback(getDialog(), Util.makeMap("ITEM", item));
        dismiss();
    }

}
