package com.miaxis.storageroom.view.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.miaxis.storageroom.R;
import com.miaxis.storageroom.adapter.TaskBoxAdapter;
import com.miaxis.storageroom.app.Storage_App;
import com.miaxis.storageroom.bean.Escort;
import com.miaxis.storageroom.bean.Task;
import com.miaxis.storageroom.bean.TaskBox;
import com.miaxis.storageroom.bean.TaskEscort;
import com.miaxis.storageroom.bean.TaskExe;
import com.miaxis.storageroom.bean.Worker;
import com.miaxis.storageroom.event.DeviceCancelEvent;
import com.miaxis.storageroom.event.HandOverDoneEvent;
import com.miaxis.storageroom.event.ScanBoxEvent;
import com.miaxis.storageroom.greendao.GreenDaoManager;
import com.miaxis.storageroom.greendao.gen.TaskBoxDao;
import com.miaxis.storageroom.service.HandOverService;
import com.miaxis.storageroom.service.ScanRfidService;
import com.miaxis.storageroom.util.DateUtil;
import com.miaxis.storageroom.view.fragment.TaskExecFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HandOverActivity extends BaseActivity {
    private static final String ACTION_RFID = "com.miaxis.storageroom.service.action.RFID";
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rv_task_box)
    RecyclerView rvTaskBox;
    @BindView(R.id.btn_start_verify)
    Button btnStartVerify;
    @BindView(R.id.btn_stop_verify)
    Button btnStopVerify;
    @BindView(R.id.btn_submit_task)
    Button btnSubmitTask;

    private TaskBoxAdapter adapter;
    private List<TaskBox> taskBoxList;
    private List<TaskBox> restBoxList;
    private List<TaskBox> showBoxList;
    private ProgressDialog pdHandOver;


    TaskBoxDao taskBoxDao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hand_over);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        initToolBar();
        initData();
        initView();
    }

    private void initToolBar() {
        toolbar.setTitle(R.string.action_settings);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initData() {
        try {
            Task task = (Task) getIntent().getSerializableExtra(TaskExecFragment.SELECTED_TASK);
            GreenDaoManager manager = GreenDaoManager.getInstance(this);
            taskBoxDao = manager.getTaskBoxDao();
            taskBoxList = taskBoxDao.queryBuilder().where(TaskBoxDao.Properties.TaskCode.eq(task.getTaskcode())).list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        restBoxList = new ArrayList<>();
        showBoxList = new ArrayList<>();
        showBoxList.addAll(taskBoxList);
        showBoxList.addAll(restBoxList);
        adapter = new TaskBoxAdapter(showBoxList, this);
    }

    private void initView() {
        pdHandOver = new ProgressDialog(this);
        pdHandOver.setMessage("正在执行任务...");
        pdHandOver.setCancelable(false);
        rvTaskBox.setAdapter(adapter);
        rvTaskBox.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.btn_start_verify)
    void onStartVerifyClicked() {
        btnStartVerify.setVisibility(View.GONE);
        btnStopVerify.setVisibility(View.VISIBLE);
        ScanRfidService.startActionRfid(this);
    }

    @OnClick(R.id.btn_stop_verify)
    void onStopVerifyClicked() {
        btnStopVerify.setVisibility(View.GONE);
        btnStartVerify.setVisibility(View.VISIBLE);
        EventBus.getDefault().post(new DeviceCancelEvent());
        Intent intent = new Intent(this, ScanRfidService.class);
        intent.setAction(ACTION_RFID);
        stopService(intent);
    }

    @OnClick(R.id.btn_submit_task)
    void onSubmitTaskClicked() {
        if (btnStopVerify.getVisibility() == View.VISIBLE) {
            onStopVerifyClicked();
        }
        Escort verifiedEscort1 = (Escort) getIntent().getSerializableExtra(TaskExecFragment.PASS_ESCORT0);
        Escort verifiedEscort2 = (Escort) getIntent().getSerializableExtra(TaskExecFragment.PASS_ESCORT1);
        Worker worker2 = (Worker) getIntent().getSerializableExtra(TaskExecFragment.SECOND_WORKER);
        Task selectedTask = (Task) getIntent().getSerializableExtra(TaskExecFragment.SELECTED_TASK);

        TaskExe taskExe = new TaskExe();
        taskExe.setTaskcode(selectedTask.getTaskcode());
        taskExe.setTasktype(selectedTask.getTasktype());
        Storage_App app = (Storage_App) getApplication();
        Worker curWorker = app.getCurWorker();
        if (curWorker == null) {
            Toast.makeText(this, "登入操作员为空， 请重新登录", Toast.LENGTH_SHORT).show();
            return;
        }
        taskExe.setWorkercode(curWorker.getCode() + "," + worker2.getCode());
        taskExe.setWorkername(curWorker.getName() + "," + worker2.getName());

        taskExe.setEscode1(verifiedEscort1.getCode());
        taskExe.setEsname1(verifiedEscort1.getName());
        taskExe.setEscode2(verifiedEscort2.getCode());
        taskExe.setEsname2(verifiedEscort2.getName());

        taskExe.setDeptno(worker2.getOrgCode());
        taskExe.setCarcode(selectedTask.getCarid());
        taskExe.setPlateno(selectedTask.getPlateno());
        taskExe.setTasktime(DateUtil.toAll(new Date()));
        pdHandOver.show();
        HandOverService.startActionFoo(this, taskExe);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onScanBoxEvent(ScanBoxEvent e) {
        String boxRfids = e.getBoxArray();
        Log.e("boxRfids：", boxRfids);
        if (TextUtils.isEmpty(boxRfids)) {
            return;
        }
        String[] rfidArr = boxRfids.split(",");
        for (int i=0; i < rfidArr.length; i++) {
            for (int j=0; j<taskBoxList.size(); j++) {
                if (rfidArr[i].equals(taskBoxList.get(j).getBoxRfid())) {
                    taskBoxList.get(j).setStatus(TaskBox.VERIFIED);
                    rfidArr[i] = "";
                }
            }
        }

        for (int i=0; i < rfidArr.length; i++) {
            boolean restHas = false;
            for (int k=0; k<restBoxList.size(); k++) {
                if (TextUtils.isEmpty(rfidArr[i]) || rfidArr[i].equals(restBoxList.get(k).getBoxRfid())) {
                    restHas = true;
                }
            }
            if (!restHas) {
                TaskBox t = new TaskBox();
                t.setBoxRfid(rfidArr[i]);
                t.setStatus(TaskBox.REST);
                restBoxList.add(t);
            }

        }

        refreshList();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void refreshList() {
        showBoxList.clear();
        showBoxList.addAll(taskBoxList);
        showBoxList.addAll(restBoxList);
        adapter.notifyDataSetChanged();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onHandOverDone(HandOverDoneEvent e) {
        if (e.getResult() == 0) {
            pdHandOver.dismiss();
            finish();
        } else {
            pdHandOver.setMessage("执行任务失败！");
            pdHandOver.setCancelable(true);
        }
    }
}
