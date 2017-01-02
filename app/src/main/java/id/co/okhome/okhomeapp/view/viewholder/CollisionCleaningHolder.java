package id.co.okhome.okhomeapp.view.viewholder;

import android.view.View;
import android.widget.TextView;

import com.mrjodev.jorecyclermanager.JoViewHolder;
import com.mrjodev.jorecyclermanager.annotations.LayoutMatcher;

import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.okhome.okhomeapp.R;
import id.co.okhome.okhomeapp.lib.Util;
import id.co.okhome.okhomeapp.model.CleaningReservationModel;

import static org.joda.time.format.DateTimeFormat.forPattern;

/**
 * Created by josongmin on 2016-06-28.
 */

@LayoutMatcher(layoutId = R.layout.item_collision_cleaning)
public class CollisionCleaningHolder extends JoViewHolder<CleaningReservationModel>{


    @BindView(R.id.itemCollisionCleaning_tvbtnChangeDate)
    TextView tvbtnChangeDate;

    @BindView(R.id.itemCollisionCleaning_tvDate)
    TextView tvDate;

    @BindView(R.id.itemCollisionCleaning_tvExpiryDate)
    TextView tvExpiryDate;

    @BindView(R.id.itemCollisionCleaning_tvDuration)
    TextView tvDuration;

    @BindView(R.id.itemCollisionCleaning_tvPrice)
    TextView tvPrice;

    public CollisionCleaningHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void onViewCreated() {
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void onBind(CleaningReservationModel m, int pos, int absPos) {
        super.onBind(m, pos, absPos);

        LocalDateTime dateTime = forPattern("yyyy-MM-dd HH:mm:ss").parseLocalDateTime(m.when.substring(0, 19));
        LocalDateTime dateTimeForExpiry = forPattern("yyyy-MM-dd").parseLocalDateTime(m.expiryDate);
        String dateTitle = DateTimeFormat.forPattern("yy년 MM월 dd일(E)").withLocale(Locale.KOREAN).print(dateTime);
        String beginTime = DateTimeFormat.forPattern("HH:mm").withLocale(Locale.KOREAN).print(dateTime);
        String expiryDate = DateTimeFormat.forPattern("yy.MM.dd(E)").withLocale(Locale.KOREAN).print(dateTimeForExpiry);

        float duration = Float.parseFloat(m.duration);
        int iDuration = (int)duration;
        float minute = iDuration % 1 * 60;

        String endTime = DateTimeFormat.forPattern("HH:mm").withLocale(Locale.KOREAN).print(dateTime.plusHours(iDuration).plusMinutes((int)minute));

        tvDate.setText(dateTitle);
        tvDuration.setText(beginTime + "~" + endTime);
        tvPrice.setText(Util.getMoneyString(m.basicCleaningPrice, '.') + "Rp");
        tvExpiryDate.setText(expiryDate);

//                DateTimeFormatter dtfOut = DateTimeFormat.forPattern("yy년 MM월 dd일(E)");
//        String datetime = dtfOut.withLocale(Locale.KOREAN).print(model.jodaDate);

    }

}
