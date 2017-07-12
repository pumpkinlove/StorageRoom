package com.miaxis.storageroom.view.fragment;


import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.miaxis.storageroom.R;
import com.miaxis.storageroom.bean.Config;
import com.miaxis.storageroom.event.DownWorkerEvent;
import com.miaxis.storageroom.greendao.GreenDaoManager;
import com.miaxis.storageroom.greendao.gen.ConfigDao;
import com.miaxis.storageroom.service.DownInfoService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ConfigFragment extends Fragment {

    private static final String TAG = "ConfigFragment";

    @BindView(R.id.etv_ip)
    EditText etvIp;
    @BindView(R.id.etv_port)
    EditText etvPort;
    @BindView(R.id.etv_deptno)
    EditText etvDeptno;
    @BindView(R.id.btn_confirm)
    Button btnConfirm;
    @BindView(R.id.btn_cancel)
    Button btnCancel;
    Unbinder unbinder;
    private ConfigDao configDao;
    private Config config;
    private ProgressDialog pdLoadEscort;
    private OnConfigClickListener mListener;

    public ConfigFragment() {
        // Required empty public constructor
    }

    @TargetApi(23)
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        onAttachToContext(context);
    }

    /*
     * Deprecated on API 23
     * Use onAttachToContext instead
     */
    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            onAttachToContext(activity);
        }
    }

    private void onAttachToContext(Context context) {
        if (context instanceof ConfigFragment.OnConfigClickListener) {
            mListener = (ConfigFragment.OnConfigClickListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnConfigClickListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_config, container, false);
        unbinder = ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);

        initView();
        initConfigView();
        return view;
    }

    void initView() {
        pdLoadEscort = new ProgressDialog(getActivity());
//        pdLoadEscort.setCancelable(false);
    }

    void initConfigView() {
        configDao = GreenDaoManager.getInstance(getActivity()).getNewSession().getConfigDao();
        config = configDao.load(1L);
        if (config != null) {
            etvIp.setText(config.getIp());
            etvPort.setText(config.getPort()+"");
            etvDeptno.setText(config.getOrgCode());
        }
    }

    @OnClick(R.id.btn_confirm)
    void onSave(View view) {
        try {
            pdLoadEscort.setMessage("正在保存设置...");
            pdLoadEscort.show();
            if (config == null) {
                config = new Config();
                config.setId(1L);
                config.setIp(etvIp.getText().toString());
                config.setPort(Integer.valueOf(etvPort.getText().toString()));
                config.setOrgCode(etvDeptno.getText().toString());
                configDao.insert(config);
            } else {
                config.setIp(etvIp.getText().toString());
                config.setPort(Integer.valueOf(etvPort.getText().toString()));
                config.setOrgCode(etvDeptno.getText().toString());
                configDao.update(config);
            }
            DownInfoService.startActionDownWorker(getActivity());
        } catch (Exception e) {
            Toast.makeText(getActivity(), "保存失败", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.btn_cancel)
    void onCancel(View view) {
        mListener.onConfig(view);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
        unbinder.unbind();
    }

    public interface OnConfigClickListener {
        void onConfig(View view);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDownWorkerEvent(DownWorkerEvent e) {
        Log.e(TAG, "onDownWorkerEvent");
        switch (e.getResult()) {
            case DownWorkerEvent.SUCCESS:
                mListener.onConfig(btnConfirm);
                break;
            case DownWorkerEvent.FAILURE:

                break;
            case DownWorkerEvent.NO_WORKER:
                mListener.onConfig(btnConfirm);
                break;
        }
        pdLoadEscort.dismiss();
    }

}
