package id.co.okhome.okhomeapp.viewholder;

import android.view.View;
import android.widget.TextView;

import com.mrjodev.jorecyclermanager.JoViewHolder;
import com.mrjodev.jorecyclermanager.annotations.LayoutMatcher;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.okhome.okhomeapp.R;
import id.co.okhome.okhomeapp.model.QnaModel;

/**
 * Created by josongmin on 2016-06-28.
 */

@LayoutMatcher(layoutId = R.layout.item_qna)
public class QnAHolder extends JoViewHolder<QnaModel>{


    @BindView(R.id.itemQna_tvAnswer)
    TextView tvAnswer;

    @BindView(R.id.itemQna_tvQuestion)
    TextView tvQuestion;


    public QnAHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void onViewCreated() {
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void onBind(QnaModel m, int pos, int absPos) {
        super.onBind(m, pos, absPos);

        tvAnswer.setText(m.answer);
        tvQuestion.setText(m.question);
    }

}
