package id.co.okhome.okhomeapp.view.fragment.tabitem;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.okhome.okhomeapp.R;
import id.co.okhome.okhomeapp.lib.AnimatedTooltipTextController;
import id.co.okhome.okhomeapp.lib.CalendarController;
import id.co.okhome.okhomeapp.lib.dialog.DialogController;
import id.co.okhome.okhomeapp.lib.dialog.ViewDialog;
import id.co.okhome.okhomeapp.model.CleaningItemModel;
import id.co.okhome.okhomeapp.view.activity.MainActivity;
import id.co.okhome.okhomeapp.view.customview.OkHomeViewPager;
import id.co.okhome.okhomeapp.view.customview.calendar.CalendarMonthView;
import id.co.okhome.okhomeapp.view.customview.calendar.dayview.CalendarDayView;
import id.co.okhome.okhomeapp.view.customview.calendar.model.CalendarDayType1Model;
import id.co.okhome.okhomeapp.view.dialog.ChooseCleaningDialog;
import id.co.okhome.okhomeapp.view.dialog.CleaningScheduleSettingDialog;

/**
 * Created by josongmin on 2016-07-28.
 */

public class MyCleaningCalendarFragment extends Fragment implements CalendarMonthView.OnDayClickListener<CalendarDayType1Model>, CalendarController.OnCalendarChangeListener{

    @BindView(R.id.fragmentMyCleaningCalendar_incCalendar)  View vCalendar;
    @BindView(R.id.layerCalendar_rlDays)                    View vDays;
    @BindView(R.id.fragmentMyCleaningCalendar_rlPopup)      View vPopup;
    @BindView(R.id.layerCalendar_vp)                        OkHomeViewPager vpCalendar;
    @BindView(R.id.fragmentMyCleaningCalendar_tvToolTip)    TextView tvToolTip;

    CalendarController calendarController;

    int calendarDayHeight = 0, calendarHeaderHeight = 0;
    int calendarTop = 0;
    int popupHeight = 0;
    int popupTop = 0;
    final int animationDuration = 300;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_cleaning_calendar, null);
    }

    @Override
    public void onStart() {
        super.onStart();

        ButterKnife.bind(this, getView());

        //캘린더
        calendarController = CalendarController.with(this)
                .setCalendarType(CalendarMonthView.CalendarType.TYPE1)
                .setOnDayClickListner(this)
                .setOnCalendarChangeListener(this)
                .initCalendar();

        //하단 데이팝업은 안보이는상태로
        vPopup.setVisibility(View.INVISIBLE);

        //툴팁 변경 핸들러
        AnimatedTooltipTextController.with(tvToolTip).setArrTooltips(new String[]{"For changing schedule, Click setting icon", "Click day for making reservation"}).start();

        //세팅 클릭시처리. 메인화면 설정 버튼임.
        if(getActivity() instanceof MainActivity){
            ((MainActivity) getActivity()).setSettingBtnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogController.showCenterDialog(getContext(), new CleaningScheduleSettingDialog()).show();
                }
            });
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        AnimatedTooltipTextController.with(tvToolTip).stop();
    }


    @Override
    public void onMonthChange(int year, int month) {

    }

    @Override
    public void onCalendarLoad() {
        if(getView() == null) return;

        //달력 로드됨
        getView().post(new Runnable() {
            @Override
            public void run() {
                calendarHeaderHeight = getView().findViewById(R.id.layerCalendar_llTop).getHeight() + getView().findViewById(R.id.layerCalendar_llWeeks).getHeight();
                calendarTop = vCalendar.getTop();
                popupHeight = vPopup.getHeight();
                calendarDayHeight = calendarController.getDayViewSample().getHeight();
                popupTop = vPopup.getTop();
                Log.d("JO", "calendarHeaderHeight : " + calendarHeaderHeight);
                Log.d("JO", "calendarTop : " + calendarTop);
                Log.d("JO", "popupHeight : " + popupHeight);
                Log.d("JO", "calendarDayHeight : " + calendarDayHeight);
                Log.d("JO", "popupTop : " + popupTop);

                moveViewPosition(false, null);
            }
        });
    }


    //팝업 전환. 현재 날짜의 주 위치가 팝업top보다 높으면 캘린더를 위로 땡겨야댐
    private void moveViewPosition(boolean popupShow, Calendar dayCalendar){

        //보기
        if(popupShow){
            int currentTop = (dayCalendar.get(Calendar.WEEK_OF_MONTH) * calendarDayHeight) + calendarTop + calendarHeaderHeight;
            if(currentTop > popupTop){
                int diffTop = currentTop - popupTop + 10;
                vDays.animate().translationY(-diffTop).setDuration(animationDuration).start();
            }
            vPopup.animate().translationY(0).setDuration(animationDuration).start();
            vpCalendar.setPagingEnabled(false);
            vPopup.setVisibility(View.VISIBLE);

        }
        //닫기
        else{
            vDays.animate().translationY(0).setDuration(animationDuration).start();
            vPopup.animate().translationY(popupHeight).setDuration(animationDuration).start();
            vpCalendar.setPagingEnabled(true);
        }
    }


    @OnClick(R.id.fragmentMyCleaningCalendar_btnClose)
    public void onBtnPopupClose(View v){
        moveViewPosition(false, null);
    }

    @OnClick(R.id.fragmentMyCleaningCalendar_btnSeeAllSpecialCleaning)
    public void onSeeAllSpecialCleaning(View v){
        DialogController.showTopDialog(getContext(),
                new ChooseCleaningDialog()
                        .setCommonCallback(new ViewDialog.DialogCommonCallback() {
                            @Override
                            public void onCallback(Object dialog, Map<String, Object> params) {
                                boolean isChked = (boolean)params.get("isChked");
                                int pos = (int)params.get("pos");
                                CleaningItemModel model = (CleaningItemModel)params.get("CleaningItemModel");

                                if(isChked){
                                    //리스트에 추가
                                }else{
                                }
                            }
                        }));
    }

    @Override
    public void onDayClick(List<CalendarDayView> listCalendarDayView, CalendarDayType1Model dayModel, String year, String month, String day) {
        moveViewPosition(true, dayModel.cal);

    }
}
