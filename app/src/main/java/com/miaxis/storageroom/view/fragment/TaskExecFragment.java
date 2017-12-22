package com.miaxis.storageroom.view.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.device.Device;
import com.miaxis.storageroom.R;
import com.miaxis.storageroom.adapter.TaskExecAdapter;
import com.miaxis.storageroom.app.Storage_App;
import com.miaxis.storageroom.bean.Escort;
import com.miaxis.storageroom.bean.Task;
import com.miaxis.storageroom.bean.TaskEscort;
import com.miaxis.storageroom.bean.Worker;
import com.miaxis.storageroom.event.ChangeFingerDialogMsgEvent;
import com.miaxis.storageroom.event.DownTaskFinishEvent;
import com.miaxis.storageroom.event.GetFingerEvent;
import com.miaxis.storageroom.event.HandOverDoneEvent;
import com.miaxis.storageroom.event.ToastEvent;
import com.miaxis.storageroom.event.VerifyEscortEvent;
import com.miaxis.storageroom.event.VerifyWorkerEvent;
import com.miaxis.storageroom.greendao.GreenDaoManager;
import com.miaxis.storageroom.greendao.gen.EscortDao;
import com.miaxis.storageroom.greendao.gen.TaskDao;
import com.miaxis.storageroom.greendao.gen.TaskEscortDao;
import com.miaxis.storageroom.greendao.gen.WorkerDao;
import com.miaxis.storageroom.service.DownTaskService;
import com.miaxis.storageroom.service.FingerService;
import com.miaxis.storageroom.util.DateUtil;
import com.miaxis.storageroom.view.activity.GetFingerActivity;
import com.miaxis.storageroom.view.activity.HandOverActivity;
import com.miaxis.storageroom.view.custom.FingerDialog;
import com.miaxis.storageroom.view.custom.ThreeFingerDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.greenrobot.greendao.query.WhereCondition;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class TaskExecFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, TaskExecAdapter.OnItemClickListener {

    private static final String TAG = "TaskExecFragment";
    public static final String SECOND_WORKER = "com.miaxis.storageroom.view.fragment.TaskExecFragment.extra.SECOND_WORKER";
    public static final String PASS_ESCORT0 = "com.miaxis.storageroom.view.fragment.TaskExecFragment.extra.PASS_ESCORT0";
    public static final String PASS_ESCORT1 = "com.miaxis.storageroom.view.fragment.TaskExecFragment.extra.PASS_ESCORT1";
    public static final String SELECTED_TASK = "com.miaxis.storageroom.view.fragment.TaskExecFragment.extra.SELECTED_TASK";

    @BindView(R.id.rv_task_exec)
    RecyclerView rvTaskExec;
    @BindView(R.id.srl_task_exec)
    SwipeRefreshLayout srlTaskExec;
    Unbinder unbinder;

    private TaskExecAdapter adapter;
    private List<Task> taskList = null;
    private TaskDao taskDao;
    private ThreeFingerDialog dialog;
    private int clickedPosition;
    private Escort escort0;
    private Escort escort1;
    private Worker secondWorker;
    private boolean fingerFlag = true;
    private int verifyStep = 1;
    private Thread escort_thread;

    public TaskExecFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "onCreate");
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e(TAG, "onStart");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e(TAG, "onCreateView");
        View view = inflater.inflate(R.layout.fragment_task_exec, container, false);
        unbinder = ButterKnife.bind(this, view);
        initData();
        initView();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        onRefresh();
    }

    private void initData() {
        try {
            GreenDaoManager manager = GreenDaoManager.getInstance(getActivity());
            taskDao = manager.getTaskDao();
            taskList = taskDao.queryBuilder()
                    .where(TaskDao.Properties.Taskdate.eq(DateUtil.toMonthDay(new Date())))
                    .where(TaskDao.Properties.Status.notEq("3"))
                    .where(TaskDao.Properties.Status.notEq("4"))
                    .where(TaskDao.Properties.Status.notEq("5"))
                    .list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        adapter = new TaskExecAdapter(taskList, getActivity());
        adapter.setmOnItemClickListener(this);
    }

    private void initView() {
        rvTaskExec.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvTaskExec.setAdapter(adapter);
        srlTaskExec.setOnRefreshListener(this);
        dialog = new ThreeFingerDialog();
        dialog.setCancelListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                fingerFlag = false;
                Device.cancel();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.e(TAG, "onDestroyView");
        unbinder.unbind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onRefresh() {
        DownTaskService.startActionDownTask(getActivity(), DateUtil.toMonthDay(new Date()));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDownTaskFinishEvent(DownTaskFinishEvent e) {
        srlTaskExec.setRefreshing(false);
        if (e.isSuccess()) {
            reLoadTask();
        } else {
            Toast.makeText(getActivity(), "下载任务信息失败", Toast.LENGTH_SHORT).show();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onHandOverFinishEvent(HandOverDoneEvent e) {
        if (e.getResult() == 0) {
            onRefresh();
        }
    }

    private void reLoadTask() {
        taskList = taskDao.queryBuilder()
                .where(TaskDao.Properties.Taskdate.eq(DateUtil.toMonthDay(new Date())))
                .where(TaskDao.Properties.Status.notEq("3"))
                .where(TaskDao.Properties.Status.notEq("4"))
                .where(TaskDao.Properties.Status.notEq("5"))
                .list();
        adapter.resetList(taskList);
    }

    @Override
    public void onItemClick(View view, int position) {
        if (position != clickedPosition) {
            secondWorker = null;
            escort0 = null;
            escort1 = null;
            verifyStep = 1;
        }
        if (secondWorker != null && escort0 != null && escort1 != null) {
            Intent i = new Intent(getContext(), HandOverActivity.class);
            i.putExtra(SELECTED_TASK, taskList.get(clickedPosition));
            i.putExtra(SECOND_WORKER, secondWorker);
            i.putExtra(PASS_ESCORT0, escort0);
            i.putExtra(PASS_ESCORT1, escort1);
            startActivity(i);
        } else {
            dialog.show(getActivity().getFragmentManager(), TAG);
            try {
                startVerify(taskList.get(position).getTaskcode());
            } catch (Exception e) {
                showToastMessage("验证异常" + e.getMessage());
                dialog.dismiss();
            }
        }
        clickedPosition = position;

    }

    private void startVerify(String taskCode) throws Exception {
        GreenDaoManager manager = GreenDaoManager.getInstance(getActivity());
        EscortDao escortDao = manager.getEscortDao();
        WorkerDao workerDao = manager.getWorkerDao();
        TaskEscortDao taskEscortDao = manager.getTaskEscortDao();
        final List<Worker> workerList = workerDao.loadAll();
        if (null == workerList || workerList.size() < 2) {
            EventBus.getDefault().post(new ToastEvent("库管员少于两人， 无法进行交接"));
            dialog.dismiss();
            return;
        } else {
            Storage_App app = (Storage_App) getActivity().getApplication();
            Worker curWorker = app.getCurWorker();
            if (curWorker == null) {
                EventBus.getDefault().post(new ToastEvent("当前库管员为空， 请重新登入"));
                dialog.dismiss();
                return;
            } else {
                for (int i=0; i<workerList.size(); i++) {
                    if (workerList.get(i).getCode().equals(curWorker.getCode()))
                    workerList.remove(i);
                }
            }
        }
        List<TaskEscort> taskEscortList = taskEscortDao.queryBuilder().where(TaskEscortDao.Properties.TaskCode.eq(taskCode)).list();
        List<String> escortCodes = new ArrayList<>();
        for (TaskEscort taskEscort : taskEscortList) {
            escortCodes.add(taskEscort.getEscortCode());
        }
        escortCodes = new ArrayList<>(new HashSet<>(escortCodes)); // 去重
        if (escort0 != null) {
            escortCodes.remove(escort0.getCode());
        }
        final List<Escort> escortList = escortDao.queryBuilder().where(EscortDao.Properties.Code.in(escortCodes)).list();
        escort_thread = new Thread(new Runnable(){
            @Override
            public void run() {
                try{
                    fingerFlag = true;
                    verifyWorker(workerList);
                    verifyEscort1(escortList);
                    escortList.remove(escort0);
                    verifyEscort2(escortList);
                    if (secondWorker != null && escort0 != null && escort1 != null) {
                        openVerifyBoxActivity(secondWorker, escort0, escort1);
                    }
                } catch(Exception e) {
                    e.printStackTrace();
                } finally {
                    dialog.dismiss();
                }
            }
        });
        escort_thread.start();
    }

    private boolean verifyEscortFinger(Escort escort, String finger) {
        int result;
        for (int i = 0; i < 10; i++) {
            String mbFinger = escort.getFinger(i);
            if (mbFinger == null || mbFinger.equals("")) {
                continue;
            }

            result = com.device.Device.verifyFinger(mbFinger, finger, 3);
            if (result == 0) {
                return true;
            }
        }
        return false;
    }

    private boolean verifyWorkerFinger(Worker worker, String finger) {
        int result;
        for (int i = 0; i < 10; i++) {
            String mbFinger = worker.getFinger(i);
            if (mbFinger == null || mbFinger.equals("")) {
                continue;
            }

            result = com.device.Device.verifyFinger(mbFinger, finger, 3);
            if (result == 0) {
                return true;
            }
        }
        return false;
    }

    private void verifyWorker(List<Worker> workerList) throws Exception {
        while (true) {
            if (!fingerFlag) {
                return;
            }
            if (verifyStep != 1) {
                break;
            }
            Thread.sleep(500);
            changeFingerDialogHead("请库管员按指纹...");
            showToastMessage("请库管员按指纹...");
            byte[] finger = new byte[200];
            byte[] errmsg = new byte[100];
            int result = Device.getFinger(15000, finger, errmsg);
            if (result != 0) {
                changeFingerDialogHead(new String(errmsg, "GBK").trim());
                continue;
            }
            boolean matchFlag = false;
            String tzFinger = new String(finger).trim();
            for (Worker worker : workerList) {
                if (verifyWorkerFinger(worker, tzFinger)) {
                    showToastMessage("库管员验证通过!");
                    matchFlag = true;
                    verifyStep = 2;
                    secondWorker = worker;
                    break;
                }
            }
            if (!matchFlag) {
                changeFingerDialogHead("库管员指纹验证失败\r\n请重新按指纹");
                continue;
            }
        }
    }

    private void verifyEscort1(List<Escort> escortList) throws Exception {
        while (true) {
            if (!fingerFlag) {
                return;
            }
            if (verifyStep != 2) {
                break;
            }
            Thread.sleep(500);
            changeFingerDialogHead("请解款员一按指纹...");
            byte[] finger = new byte[200];
            byte[] errmsg = new byte[100];
            int result = Device.getFinger(15000, finger, errmsg);
            if (result != 0) {
                showToastMessage(new String(errmsg, "GBK").trim());
                changeFingerDialogHead("解款员一指纹采集失败");
                continue;
            }
            boolean matchFlag = false;
            String tzFinger = new String(finger).trim();
            for (Escort escort : escortList) {
                if (verifyEscortFinger(escort, tzFinger)) {
                    showToastMessage("解款员一验证通过!");
                    matchFlag = true;
                    verifyStep = 3;
                    escort0 = escort;
                    break;
                }
            }
            if (!matchFlag) {
                changeFingerDialogHead("解款员一指纹验证失败");
                continue;
            }
        }
    }

    private void verifyEscort2(List<Escort> escortList) throws Exception {
        while (true) {
            if (!fingerFlag) {
                return;
            }
            if (verifyStep != 3) {
                break;
            }
            Thread.sleep(500);
            changeFingerDialogHead("请解款员二按指纹...");
            byte[] finger = new byte[200];
            byte[] errmsg = new byte[100];
            int result = Device.getFinger(15000, finger, errmsg);
            if (result != 0) {
                showToastMessage(new String(errmsg, "GBK").trim());
                changeFingerDialogHead("解款员二指纹采集失败");
                continue;
            }
            boolean matchFlag = false;
            String tzFinger = new String(finger).trim();
            for (Escort escort : escortList) {
                if (verifyEscortFinger(escort, tzFinger)) {
                    showToastMessage("解款员二验证通过!");
                    matchFlag = true;
                    verifyStep = 4;
                    escort1 = escort;
                    break;
                }
            }
            if (!matchFlag) {
                changeFingerDialogHead("解款员二指纹验证失败");
                continue;
            }
        }
    }

    void changeFingerDialogHead(String content) {
        EventBus.getDefault().post(new ChangeFingerDialogMsgEvent(content));
    }

    void showToastMessage(String content) {
        EventBus.getDefault().post(new ToastEvent(content));
    }

    public void openVerifyBoxActivity(Worker secondWorker, Escort escort0, Escort escort1) {
        Intent i = new Intent(getContext(), HandOverActivity.class);
        i.putExtra(SECOND_WORKER, secondWorker);
        i.putExtra(PASS_ESCORT0, escort0);
        i.putExtra(PASS_ESCORT1, escort1);
        i.putExtra(SELECTED_TASK, taskList.get(clickedPosition));
        startActivity(i);
    }
}
