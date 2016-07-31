package id.co.okhome.okhomeapp.viewholder;

import android.view.View;
import android.widget.TextView;

import com.mrjodev.jorecyclermanager.JoViewHolder;
import com.mrjodev.jorecyclermanager.annotations.LayoutMatcher;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.okhome.okhomeapp.R;
import id.co.okhome.okhomeapp.model.NoticeModel;

/**
 * Created by josongmin on 2016-06-28.
 */

@LayoutMatcher(layoutId = R.layout.item_notice)
public class NoticeHolder extends JoViewHolder<NoticeModel>{

    @BindView(R.id.itemNotice_tvTitle)
    TextView tvTitle;

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

        tvTitle.setText(m.title);
    }

}
