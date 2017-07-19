package com.miaxis.storageroom.view.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.miaxis.storageroom.R;
import com.miaxis.storageroom.app.Storage_App;
import com.miaxis.storageroom.bean.Config;
import com.miaxis.storageroom.bean.Escort;
import com.miaxis.storageroom.bean.Task;
import com.miaxis.storageroom.bean.TaskBox;
import com.miaxis.storageroom.bean.TaskEscort;
import com.miaxis.storageroom.bean.TimeStamp;
import com.miaxis.storageroom.bean.Worker;
import com.miaxis.storageroom.event.DownWorkerEvent;
import com.miaxis.storageroom.event.VerifyWorkerEvent;
import com.miaxis.storageroom.greendao.GreenDaoManager;
import com.miaxis.storageroom.greendao.gen.ConfigDao;
import com.miaxis.storageroom.greendao.gen.EscortDao;
import com.miaxis.storageroom.greendao.gen.TaskBoxDao;
import com.miaxis.storageroom.greendao.gen.TaskDao;
import com.miaxis.storageroom.greendao.gen.TaskEscortDao;
import com.miaxis.storageroom.greendao.gen.TimeStampDao;
import com.miaxis.storageroom.greendao.gen.WorkerDao;
import com.miaxis.storageroom.service.DownInfoService;
import com.miaxis.storageroom.service.DownTaskService;
import com.miaxis.storageroom.service.FingerService;
import com.miaxis.storageroom.util.DateUtil;
import com.miaxis.storageroom.view.custom.FingerDialog;
import com.miaxis.storageroom.view.fragment.ConfigFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity implements ConfigFragment.OnConfigClickListener {

    @BindView(R.id.iv_setting_login)
    ImageView ivSettingLogin;
    @BindView(R.id.fl_config)
    FrameLayout flConfig;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.tv_company)
    TextView tvCompany;
    @BindView(R.id.tv_version)
    TextView tvVersion;

    private FingerDialog fingerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        initConfig();
        fingerDialog = new FingerDialog();
        tvVersion.append(getVersion());
    }

    private void initConfig() {
        toggleConfig(!checkConfig());
    }

    private boolean checkConfig() {
        ConfigDao configDao = GreenDaoManager.getInstance(this).getNewSession().getConfigDao();
        Config config = configDao.loadByRowId(1L);
        if (config == null) {
            return false;
        } else {
            return true;
        }
    }

    @OnClick(R.id.iv_setting_login)
    void onSettingClicked() {
        if (checkConfig()) {
            toggleConfig(flConfig.getVisibility() != View.VISIBLE);
        }
    }

    @OnClick(R.id.btn_login)
    void onLoginClicked() {
//        try {
//            GreenDaoManager manager = GreenDaoManager.getInstance(getApplicationContext());
//            ConfigDao configDao = manager.getConfigDao();
//            TimeStampDao timeStampDao = manager.getTimeStampDao();
//            EscortDao escortDao = manager.getEscortDao();
//            WorkerDao workerDao = manager.getWorkerDao();
//            TaskDao taskDao = manager.getTaskDao();
//            TaskBoxDao taskBoxDao = manager.getTaskBoxDao();
//            TaskEscortDao taskEscortDao = manager.getTaskEscortDao();
//
//            List<Worker> workerList = workerDao.loadAll();
//            List<Escort> escorts = escortDao.loadAll();
//            List<TimeStamp> timeStamps = timeStampDao.loadAll();
//            List<Task> taskList = taskDao.loadAll();
//            List<TaskBox> taskBoxList = taskBoxDao.loadAll();
//            List<TaskEscort> taskEscortList = taskEscortDao.loadAll();

//            DownInfoService.startActionDownEscort(this);
//            DownTaskService.startActionDownTask(this, DateUtil.toMonthDay(new Date()));
//        } catch (Exception e) {
//
//        }
        FingerService.startActionVerifyWorker(this);
        fingerDialog.show(getFragmentManager(), "f");

    }

    @Override
    public void onConfig(View view) {
        switch (view.getId()) {
            case R.id.btn_confirm:
                Log.e("login", "btn_confirm");
                initConfig();
                break;
            case R.id.btn_cancel:
                Log.e("login", "btn_cancel");
                initConfig();
                break;
        }
    }

    private String getVersion() {
        String versionName = "";
        try {
            // ---get the package info---
            PackageManager pm = getPackageManager();
            PackageInfo pi = pm.getPackageInfo(getPackageName(), 0);
            versionName = pi.versionName;
            if (versionName == null || versionName.length() <= 0) {
                return "";
            }
        } catch (Exception e) {
            Log.e("VersionInfo", "Exception", e);
        }
        return versionName;
    }

    private void toggleConfig(boolean flag) {
        if (flag) {
            FragmentManager fm = getFragmentManager();
            FragmentTransaction transaction = fm.beginTransaction();
            ConfigFragment fragment = new ConfigFragment();
            transaction.replace(R.id.fl_config, fragment);
            transaction.commit();
            flConfig.setVisibility(View.VISIBLE);
            btnLogin.setVisibility(View.INVISIBLE);
        } else {
            flConfig.setVisibility(View.INVISIBLE);
            btnLogin.setVisibility(View.VISIBLE);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onVerifyWorkerEvent(VerifyWorkerEvent e) {
        Storage_App app = (Storage_App) getApplication();
        app.setCurWorker(e.getWorker());
        if (e.isSuccess()) {
            fingerDialog.dismiss();
            startActivity(new Intent(this, MainPlateActivity.class));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
