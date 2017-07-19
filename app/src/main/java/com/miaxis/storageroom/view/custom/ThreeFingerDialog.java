package com.miaxis.storageroom.view.custom;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.TextView;

import com.miaxis.storageroom.R;
import com.miaxis.storageroom.event.ChangeFingerDialogMsgEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ThreeFingerDialog extends DialogFragment {

    Unbinder unbinder;
    @BindView(R.id.tv_head)
    TextView tvHead;
    @BindView(R.id.gif_finger)
    GifView gifFinger;
    @BindView(R.id.tv_cancel)
    TextView tvCancel;

    private String finger;
    private OnClickListener cancelListener;

    public String getFinger() {
        return finger;
    }

    public void setCancelListener(OnClickListener cancelListener) {
        this.cancelListener = cancelListener;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            DisplayMetrics dm = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
            dialog.getWindow().setLayout((int) (dm.widthPixels * 0.77), LayoutParams.WRAP_CONTENT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        EventBus.getDefault().register(this);
        View view = inflater.inflate(R.layout.dialog_three_finger, container);
        unbinder = ButterKnife.bind(this, view);
        tvCancel.setOnClickListener(cancelListener);
        gifFinger.setMovieResource(R.raw.put_finger);
        setCancelable(false);

        return view;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onChangeHead(ChangeFingerDialogMsgEvent e) {
        tvHead.setText(e.getContent());
    }

    @Override
    public void onDestroyView() {
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
        unbinder.unbind();
    }


}
