package id.co.okhome.okhomeapp.view.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mrjodev.jorecyclermanager.JoViewHolder;
import com.mrjodev.jorecyclermanager.annotations.LayoutMatcher;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.okhome.okhomeapp.R;
import id.co.okhome.okhomeapp.lib.Util;
import id.co.okhome.okhomeapp.lib.dialog.DialogController;
import id.co.okhome.okhomeapp.lib.dialog.ViewDialog;
import id.co.okhome.okhomeapp.model.CleaningTicketModel;

/**
 * Created by josongmin on 2016-08-17.
 */
@LayoutMatcher(layoutId = R.layout.item_cleaning_ticket)
public class CleaningTicketHolder extends JoViewHolder<CleaningTicketModel> implements View.OnClickListener{

    @BindView(R.id.itemCleaningTicket_ivbtnMore)        ImageView ivMore;
    @BindView(R.id.itemCleaningTicket_tvCleaningDate)   TextView tvCleaningDate;
    @BindView(R.id.itemCleaningTicket_tvExpiryDate)     TextView tvExpiryDate;
    @BindView(R.id.itemCleaningTicket_tvIssueDate)      TextView tvIssueDate;
    @BindView(R.id.itemCleaningTicket_ivCleaningType)   TextView tvCleaningType;

    public CleaningTicketHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void onViewCreated() {
        ButterKnife.bind(this, getView());
    }

    @Override
    public void onBind(CleaningTicketModel s, int pos, int absPos) {
        super.onBind(s, pos, absPos);

        String expiryDate = Util.getFormattedDateString(s.expiryDate, "yyyy.MM.dd(E)");
        String issueDate = Util.getFormattedDateString(s.issueDate, "yyyy.MM.dd(E)");
        String cleaningDate;
        if(s.cleaningDate != null && s.cleaningDate.length() > 0){
            cleaningDate = Util.getFormattedDateString(s.cleaningDate, "yyyy.MM.dd(E)");
            tvCleaningDate.setTextColor(getContext().getResources().getColor(R.color.okHomeGrayDeep));
        }else{
            cleaningDate = "지정되지 않음";
            tvCleaningDate.setTextColor(getContext().getResources().getColor(R.color.colorPetSister));
        }

        tvCleaningDate.setText(cleaningDate);
        tvIssueDate.setText(issueDate);
        tvExpiryDate.setText(expiryDate);

        if(s.cleaningType.equals("NORMAL")){
            tvCleaningType.setText("일반청소권");
        }else{
            tvCleaningType.setText("이사청소권");
        }

        ivMore.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        CleaningTicketModel m = getModel();
        //취소하기
        //일정변경하기
        if(m.cleaningDate == null) {

            DialogController.showListDialog(getContext(), "선택하세요", new String[]{"청소 신청"}, 0, new ViewDialog.DialogCommonCallback() {
                @Override
                public void onCallback(Object dialog, Map<String, Object> params) {

                }
            });

        }else{
            DialogController.showListDialog(getContext(), "선택하세요", new String[]{"일정 변경", "청소 취소", "환불요청"}, 0, new ViewDialog.DialogCommonCallback() {
                @Override
                public void onCallback(Object dialog, Map<String, Object> params) {

                }
            });
        }


    }
}