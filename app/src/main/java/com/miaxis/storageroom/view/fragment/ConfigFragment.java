package com.miaxis.storageroom.view.fragment;


import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.miaxis.storageroom.R;
import com.miaxis.storageroom.bean.Config;
import com.miaxis.storageroom.event.CommExecEvent;
import com.miaxis.storageroom.event.DownEscortEvent;
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
    private ProgressDialog pdSaveConfig;
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
        pdSaveConfig = new ProgressDialog(getActivity());
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
            pdSaveConfig.setMessage("正在保存设置...");
            pdSaveConfig.show();
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
            pdSaveConfig.setMessage("正在同步员工信息...");
            DownInfoService.startActionDownWorker(getActivity());
            DownInfoService.startActionDownEscort(getActivity());
        } catch (Exception e) {
            pdSaveConfig.setTitle("保存失败");
            pdSaveConfig.setCancelable(true);
        }
    }

    @OnClick(R.id.btn_cancel)
    void onCancel(View view) {
        mListener.onConfigCancel();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
        unbinder.unbind();
    }

    public interface OnConfigClickListener {
        void onConfigSave();
        void onConfigCancel();
    }

    private boolean workerDown = false;
    private boolean escortDown = false;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCommExecEvent(CommExecEvent e) {
        if (e.getCommCode() == CommExecEvent.COMM_DOWN_WORKER) {
            switch (e.getResult()) {
                case CommExecEvent.RESULT_SUCCESS:
                    workerDown = true;
                    pdSaveConfig.setMessage("员工信息同步完成");
                    break;
                case CommExecEvent.RESULT_EXCEPTION:
                    pdSaveConfig.setMessage("员工信息同步异常");
                    pdSaveConfig.setCancelable(true);
                    break;
                case CommExecEvent.RESULT_SOCKET_NULL:
                    pdSaveConfig.setMessage("网络连接失败，请检查ip地址、端口号");
                    pdSaveConfig.setCancelable(true);
                    break;
                default:
            }
//        } else if (e.getCommCode() == CommExecEvent.COMM_DOWN_ESCORT) {
//            switch (e.getResult()) {
//                case CommExecEvent.RESULT_SUCCESS:
//                    escortDown = true;
//                    pdSaveConfig.setMessage("押运员信息同步完成");
//                    break;
//                case CommExecEvent.RESULT_EXCEPTION:
//                    pdSaveConfig.setMessage("押运员信息同步异常");
//                    pdSaveConfig.setCancelable(true);
//                    break;
//                case CommExecEvent.RESULT_SOCKET_NULL:
//                    pdSaveConfig.setMessage("网络连接失败，请检查ip地址、端口号。");
//                    pdSaveConfig.setCancelable(true);
//                    break;
//                default:
//            }
        }
        if (workerDown && escortDown) {
            pdSaveConfig.setMessage("信息同步完成");
            pdSaveConfig.dismiss();
            mListener.onConfigSave();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDownEscortEvent(DownEscortEvent e) {
        if (e.getResult() == 0) {
            pdSaveConfig.setMessage("押运员信息同步完成");
            escortDown = true;
        } else {
            pdSaveConfig.setMessage("押运员信息同步失败");
            escortDown = false;
            pdSaveConfig.setCancelable(true);
        }
        if (workerDown && escortDown) {
            pdSaveConfig.setMessage("信息同步完成");
            pdSaveConfig.dismiss();
            mListener.onConfigSave();
        }
    }

}
