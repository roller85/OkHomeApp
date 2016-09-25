package id.co.okhome.okhomeapp.view.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mrjodev.jorecyclermanager.JoViewHolder;
import com.mrjodev.jorecyclermanager.annotations.LayoutMatcher;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.okhome.okhomeapp.R;
import id.co.okhome.okhomeapp.lib.Util;
import id.co.okhome.okhomeapp.model.NoticeModel;

/**
 * Created by josongmin on 2016-06-28.
 */

@LayoutMatcher(layoutId = R.layout.item_notice)
public class NoticeHolder extends JoViewHolder<NoticeModel> implements View.OnClickListener{

    @BindView(R.id.itemNotice_tvTitle)          TextView tvTitle;
    @BindView(R.id.itemNotice_tvContents)       TextView tvContents;
    @BindView(R.id.itemNotice_tvDate)           TextView tvDate;
    @BindView(R.id.itemNotice_ivArrow)          ImageView ivArrow;

    public NoticeHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void onViewCreated() {
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void onBind(NoticeModel m, int pos, int absPos) {
        super.onBind(m, pos, absPos);

        tvTitle.setText(m.subject);
        tvContents.setText(m.content);
        tvDate.setText(Util.getFormattedDateString(m.insertDate, "yyyy-MM-dd"));
        if(m.isOpened){
            tvContents.setVisibility(View.VISIBLE);
            ivArrow.setRotation(0);
        }else{
            tvContents.setVisibility(View.GONE);
            ivArrow.setRotation(-90);
        }

        getView().setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        getModel().isOpened = !getModel().isOpened;
        getAdapter().notifyItemChanged(getAbsPos());
    }
}
