package id.co.okhome.okhomeapp.view.fragment.tabitem;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.okhome.okhomeapp.R;
import id.co.okhome.okhomeapp.config.CurrentUserInfo;
import id.co.okhome.okhomeapp.view.fragment.tabitem.flow.TabFragmentFlow;

/**
 * Created by josongmin on 2016-07-28.
 */

public class InviteUserFragment extends Fragment implements TabFragmentFlow {

    @BindView(R.id.fragmentInviteUser_tvUuid)
    TextView tvUUID;

    String uuid = "";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_invite_user, null);
    }

    @Override
    public void onStart() {
        super.onStart();
        ButterKnife.bind(this, getView());

        uuid = CurrentUserInfo.get(getContext()).uuid;
        tvUUID.setText(uuid);
    }

    @Override
    public String getTitle() {
        return "친구 초대하기";
    }

    @Override
    public View.OnClickListener onTabSettingClick(ImageView ivIcon) {
        return null;
    }

    @OnClick(R.id.fragmentInviteUser_tvbtnCopy)
    public void onBtnCopy(View view){
        ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("CODE", uuid);
        clipboard.setPrimaryClip(clip);
    }

    @OnClick(R.id.fragmentInviteUser_tvbtnSend)
    public void onBtnSend(View view){
        //공유하기.
        String subject = "오케이홈에 가입하세요!";
        String text = "내용입니다. 처음두번 동안 어쩌구 히히히 코드는 "+ uuid + "로 가입하세용. 자세한 내용은 : http://www.okhome.id";

        Intent intent = new Intent(android.content.Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, text);
        Intent chooser = Intent.createChooser(intent, "공유하기");
        startActivity(chooser);
    }


}
