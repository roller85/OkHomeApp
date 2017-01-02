package id.co.okhome.okhomeapp.view.viewholder;

import android.view.View;
import android.widget.TextView;

import com.mrjodev.jorecyclermanager.JoViewHolder;
import com.mrjodev.jorecyclermanager.annotations.LayoutMatcher;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.okhome.okhomeapp.R;

/**
 * Created by josongmin on 2016-08-17.
 */
@LayoutMatcher(layoutId = R.layout.header_cleaningticket)
public class CleaningTicketHeaderHolder extends JoViewHolder<Map<String, String>> implements View.OnClickListener{

    @BindView(R.id.headerCleaningTicket_tvTotal)    TextView tvTotal;
    @BindView(R.id.headerCleaningTicket_tvUnused)   TextView tvUnused;
    @BindView(R.id.headerCleaningTicket_tvUsed)     TextView tvUsed;


    public CleaningTicketHeaderHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void onViewCreated() {
        ButterKnife.bind(this, getView());
    }

    @Override
    public void onBind(Map<String, String> map, int pos, int absPos) {
        super.onBind(map, pos, absPos);

        String totalCnt = map.get("allTicketCnt").toString();
        String usedTicketlCnt = map.get("usedTicketCnt").toString();
        String notUsedTicketCnt = map.get("notUsedTicketCnt").toString();

        tvTotal.setText(totalCnt + "개");
        tvUnused.setText(notUsedTicketCnt + "개");
        tvUsed.setText(usedTicketlCnt + "개");
    }

    @Override
    public void onClick(View v) {
    }
}