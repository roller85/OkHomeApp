package id.co.okhome.okhomeapp.view.fragment.makereservation.oneday;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.orhanobut.dialogplus.DialogPlus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.okhome.okhomeapp.R;
import id.co.okhome.okhomeapp.config.CurrentUserInfo;
import id.co.okhome.okhomeapp.lib.ProgressDialogController;
import id.co.okhome.okhomeapp.lib.Util;
import id.co.okhome.okhomeapp.lib.dialog.DialogController;
import id.co.okhome.okhomeapp.lib.dialog.ViewDialog;
import id.co.okhome.okhomeapp.lib.joviewrepeator.JoRepeatorCallback;
import id.co.okhome.okhomeapp.lib.joviewrepeator.JoViewRepeator;
import id.co.okhome.okhomeapp.lib.retrofit.RetrofitCallback;
import id.co.okhome.okhomeapp.lib.retrofit.restmodel.ErrorModel;
import id.co.okhome.okhomeapp.model.SpcCleaningModel;
import id.co.okhome.okhomeapp.restclient.RestClient;
import id.co.okhome.okhomeapp.view.dialog.ChooseCleaningGridDialog;
import id.co.okhome.okhomeapp.view.dialog.CommonTextDialog;
import id.co.okhome.okhomeapp.view.fragment.makereservation.flow.MakeReservationFlow;
import id.co.okhome.okhomeapp.view.fragment.makereservation.flow.MakeReservationParam;

/**
 * Created by josongmin on 2016-07-28.
 */

public class OnedayCleaningInvoiceFragment extends Fragment implements MakeReservationFlow{

    @BindView(R.id.fragmentMakeReservationDayCleaningInvoice_vgCleaningItem)                ViewGroup vgCleaningItem;
    @BindView(R.id.fragmentMakeReservationDayCleaningInvoice_vgServiceItem)                 ViewGroup vgServiceItem;
    @BindView(R.id.fragmentMakeReservationDayCleaningInvoice_tvDatetime)                    TextView tvDateTime;
    @BindView(R.id.fragmentMakeReservationDayCleaningInvoice_vgSpecialPackage)              View vgSpecialPackage;
    @BindView(R.id.fragmentMakeReservationDayCleaningInvoice_vbtnRequestSpecialCleaning)    View vbtnReqSpecialCleaning;


    TextView tvTotalDuration;
    TextView tvTotalPrice;

    DialogPlus dialogChooseCleaning; //팝업 프레임
    ChooseCleaningGridDialog dialogCleaningGrid; //실제 팝업내용
    JoViewRepeator<SpcCleaningModel> viewRepeater;
    JoViewRepeator<SpcCleaningModel> viewRepeaterForSpecialService;

    int hour = 0;
    int pricePerHour = 0;
    String specialCleaningIds = "";
    String consultingYN = "N";

    String type = ""; //ONEDAY, MOVEIN
    String mainCleaningName = "";

    List<SpcCleaningModel> listChoosedItems = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_m_r_daycleaning_invoice, null);
    }

    @Override
    public void onCurrentPage(int pos, MakeReservationParam params) {

        //params에서 분석
//        tvDateTime.setText(Util.getFormattedDateString(params.datetime+":00", "yyyy-MM-dd ahh:mm "));

        type = getArguments().getString("type");

        //파라미터 설정
        if(type.equals("ONEDAY")){
            pricePerHour = 80000;
            mainCleaningName = "Basic cleaning";
        }else if(type.equals("MOVEIN")){
            pricePerHour = 100000;
            mainCleaningName = "Move in cleaning";
        }else{
            pricePerHour = 0;
            mainCleaningName = "????";
        }


        //시간, 가격 기본셋 처리
//        if(params.homeType == null){
//            hour = 4;
//        }
//        else if(params.homeType.equals("1") && params.homeSize.equals("2")){
//            hour = 5;
//        }else{
//            hour = 4;
//        }


        adaptServiceList();
        adaptExtraServiceList();

        //엑스트라서비스 기본셋불러오기
        initCleaningList();
    }



    @Override
    public boolean next(final MakeReservationParam paramsCleaning) {

//        try{
//            paramsCleaning.homeId = CurrentUserInfo.getHomeId(getContext());
//            paramsCleaning.specialCleaingIds = specialCleaningIds;
//            paramsCleaning.consultingYN = consultingYN;
//            paramsCleaning.cleaningDuration = hour+"";
//            paramsCleaning.type = (type.equals("MOVEIN") ? "2" : "1");
//
//        }catch(Exception e){
//            Util.showToast(getContext(), e.getMessage());
//            return false;
//        }

        DialogController.showCenterDialog(getContext(), new CommonTextDialog("Proceed?", "Touch Confirm, Cleaning reservation will be complete", new ViewDialog.DialogCommonCallback() {
            @Override
            public void onCallback(Object dialog, Map<String, Object> params) {
                String onClick = Util.getMapValue(params, "ONCLICK");
                if(onClick.equals("OK")){
                    String paramJson = new Gson().toJson(paramsCleaning);
                    final int pId = ProgressDialogController.show(getContext());
                    RestClient.getCleaningRequestClient().onedayRequest(CurrentUserInfo.getId(getContext()), paramJson).enqueue(new RetrofitCallback<String>() {
                        @Override
                        public void onFinish() {
                            ProgressDialogController.dismiss(pId);
                        }

                        @Override
                        public void onSuccess(String result) {
                            //완료됬슴다
                            getActivity().finish();
                        }

                        @Override
                        public void onJodevError(ErrorModel jodevErrorModel) {
                            Util.showToast(getContext(), jodevErrorModel.message);
                        }
                    });

                }
            }
        }));

        return true;
    }

    @Override
    public void onStart() {
        super.onStart();
        ButterKnife.bind(this, getView());

        //인보이스내용
        viewRepeater = new JoViewRepeator<SpcCleaningModel>(getContext())
                .setContainer(vgServiceItem)
                .setItemLayoutId(R.layout.item_cleaning_simple)
                .setCallBack(new InvoiceRepetorCallback(true));

        viewRepeaterForSpecialService = new JoViewRepeator<SpcCleaningModel>(getContext())
                .setContainer(vgCleaningItem)
                .setItemLayoutId(R.layout.item_cleaning_simple)
                .setCallBack(new InvoiceRepetorCallback(false));

        vgSpecialPackage.setVisibility(View.GONE);
        if(type.equals("MOVEIN")){
            vbtnReqSpecialCleaning.setVisibility(View.GONE);
        }else{
            vbtnReqSpecialCleaning.setVisibility(View.VISIBLE);
        }

    }

    //모든청소 리스트 설정
    private void initCleaningList(){
//        if(dialogCleaningGrid == null){
//            List<SpcCleaningModel> listExtraCleaning =  JoSharedPreference.with(getContext()).get("ExtraCleaningList");
//
//            dialogCleaningGrid = new ChooseCleaningGridDialog(hour, listExtraCleaning, new ChooseCleaningGridDialog.OnExtraCleaningChoosedListener() {
//                @Override
//                public void onChoosed(List<SpcCleaningModel> list) {
//                    if(list.size() > 0){
//                        String ids = "";
//                        for(SpcCleaningModel m : list){
//                            ids += "," + m.id;
//                        }
//                        ids = ids.substring(1);
//
//                        //서버에 날릴거
//                        specialCleaningIds = ids;
//                    }else{
//                        specialCleaningIds = "";
//                    }
//                    listChoosedItems = list;
//                    adaptExtraServiceList();
//                }
//            });
//        }

    }


    private void adaptServiceList(){
        List<SpcCleaningModel> listCurrentCleaningItems = new ArrayList<>();

        listCurrentCleaningItems.add(new SpcCleaningModel("", mainCleaningName, hour, pricePerHour * hour, "", true));

        if(CurrentUserInfo.get(getContext()).cleaningCount.equals("0")){
            consultingYN = "Y";
            listCurrentCleaningItems.add(new SpcCleaningModel("", "Consulting", 0, 0, "", true));
        }

        viewRepeater.setList(listCurrentCleaningItems);
        viewRepeater.notifyDataSetChanged();

    }
    //기본 셋 설정. 선택된거 불러옴
    private void adaptExtraServiceList(){
        if(listChoosedItems != null && listChoosedItems.size() > 0){
            //버튼없애고
            vbtnReqSpecialCleaning.setVisibility(View.GONE);

            //일반내역에서 푸터없애고
            ((InvoiceRepetorCallback)viewRepeater.getCallBack()).setHasFooter(false);

            //스페셜내역에 푸터 나오기
            ((InvoiceRepetorCallback)viewRepeaterForSpecialService.getCallBack()).setHasFooter(true);

            //스페셜클리닝 보이게하고
            vgSpecialPackage.setVisibility(View.VISIBLE);



        }else{

            //스페셜클리닝 숨기기
            vgSpecialPackage.setVisibility(View.GONE);

            //일반내역에서 푸터생기기
            ((InvoiceRepetorCallback)viewRepeater.getCallBack()).setHasFooter(true);

            //스페셜내역에 푸터 없애기
            ((InvoiceRepetorCallback)viewRepeaterForSpecialService.getCallBack()).setHasFooter(false);

            //버튼나오고
            if(type.equals("MOVEIN")){
                vbtnReqSpecialCleaning.setVisibility(View.GONE);
            }else{
                vbtnReqSpecialCleaning.setVisibility(View.VISIBLE);
            }
        }

        viewRepeaterForSpecialService.setList(listChoosedItems);
        viewRepeaterForSpecialService.notifyDataSetChanged();

        viewRepeater.notifyDataSetChanged();
    }


    //전체가격처리
    private void refreshTotalPrice(){
        int price = 0;
        int hour = 0;

        List<SpcCleaningModel> listAllCleaningItems = new ArrayList<>();

        listAllCleaningItems.addAll(viewRepeater.getList());
        listAllCleaningItems.addAll(viewRepeaterForSpecialService.getList());

        for(SpcCleaningModel m : listAllCleaningItems){
            price += m.price;
            hour += m.hour;

            tvTotalPrice.setText(Util.getMoneyString(price+"", '.') + "Rp");
            tvTotalDuration.setText(hour +"h");

        }
    }

    private void removeSpecialItem(){

    }

    //서비스 더 보기 눌렀을때
    @OnClick({R.id.fragmentMakeReservationDayCleaningInvoice_vbtnServices, R.id.fragmentMakeReservationDayCleaningInvoice_vbtnRequestSpecialCleaning})
    public void onBtnServicesClick(){

        if(dialogChooseCleaning == null){
            dialogChooseCleaning = DialogController.showBottomDialog(getContext(), dialogCleaningGrid);
        }

        if(dialogCleaningGrid == null){
            Util.showToast(getContext(), "Not loaded");
            return;
        }else{
            dialogCleaningGrid.show();
        }
        dialogChooseCleaning.show();
    }

    //인보이스 아이템 리피터
    class InvoiceRepetorCallback extends JoRepeatorCallback<SpcCleaningModel>{

        boolean hasFooter;
        public InvoiceRepetorCallback(boolean hasFooter) {
            this.hasFooter = hasFooter;
        }

        public void setHasFooter(boolean hasFooter) {
            this.hasFooter = hasFooter;
        }

        @Override
        public View getHeaderView() {
            return null;
        }

        @Override
        public View getFooterView() {
            if(hasFooter){
                View v = LayoutInflater.from(getContext()).inflate(R.layout.item_cleaning_simple_footer, null);
                tvTotalDuration = (TextView)v.findViewById(R.id.itemCleaningSimpleFooter_tvTotalDuration);
                tvTotalPrice = (TextView)v.findViewById(R.id.itemCleaningSimpleFooter_tvTotalPrice);

                refreshTotalPrice();
                return v;
            }else{
                return null;
            }
        }

        @Override
        public void onBind(View v, SpcCleaningModel model) {
            TextView tvTitle = (TextView) v.findViewById(R.id.itemCleaningSimple_tvTitle);
            TextView tvPrice = (TextView) v.findViewById(R.id.itemCleaningSimple_tvPrice);
            TextView tvDuration = (TextView) v.findViewById(R.id.itemCleaningSimple_tvDuration);
            ImageView ivChk = (ImageView)v.findViewById(R.id.itemCleaningSimple_ivbtnChk);

            //matching data
            tvTitle.setText(model.name);

            tvPrice.setText(Util.getMoneyString(model.price, '.') + "Rp");
            tvDuration.setText(model.hour == 0 ? "-" : model.hour + "h");

            if(model.isStatic){
                ivChk.setVisibility(View.GONE);
            }else{
                ivChk.setVisibility(View.VISIBLE);
                ivChk.setOnClickListener(onChkClickListener);
                ivChk.setTag(model);
            }
        }

        View.OnClickListener onChkClickListener = new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //아이템에서 제거하는이벤튼데..
                SpcCleaningModel model = (SpcCleaningModel)v.getTag();
                getViewRepeator().getList().remove(model);
                listChoosedItems = getViewRepeator().getList();
                adaptExtraServiceList();

                //팝업하고도 동기화시키자.
                dialogCleaningGrid.setCheck(model.id, false);
            }
        };
    }

}
