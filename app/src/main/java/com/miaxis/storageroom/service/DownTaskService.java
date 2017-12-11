package com.miaxis.storageroom.service;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;

import com.miaxis.storageroom.bean.Config;
import com.miaxis.storageroom.bean.Task;
import com.miaxis.storageroom.bean.TaskBox;
import com.miaxis.storageroom.bean.TaskEscort;
import com.miaxis.storageroom.comm.BaseComm;
import com.miaxis.storageroom.comm.DownTaskComm;
import com.miaxis.storageroom.event.DownTaskFinishEvent;
import com.miaxis.storageroom.greendao.GreenDaoManager;
import com.miaxis.storageroom.greendao.gen.ConfigDao;
import com.miaxis.storageroom.greendao.gen.TaskBoxDao;
import com.miaxis.storageroom.greendao.gen.TaskDao;
import com.miaxis.storageroom.greendao.gen.TaskEscortDao;

import org.greenrobot.eventbus.EventBus;

import java.net.Socket;
import java.util.List;

public class DownTaskService extends IntentService {

    private static final String ACTION_DOWN_TASK = "com.miaxis.storageroom.service.action.ACTION_DOWN_TASK";

    private static final String TASK_DATE = "com.miaxis.storageroom.service.extra.TASK_DATE";

    public DownTaskService() {
        super("DownTaskService");
    }

    public static void startActionDownTask(Context context, String taskDate) {
        Intent intent = new Intent(context, DownTaskService.class);
        intent.setAction(ACTION_DOWN_TASK);
        intent.putExtra(TASK_DATE, taskDate);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_DOWN_TASK.equals(action)) {
                final String taskDate = intent.getStringExtra(TASK_DATE);
                handleActionDownTask(taskDate);
            }
        }
    }

    private void handleActionDownTask(String taskDate) {

        try {
            GreenDaoManager manager = GreenDaoManager.getInstance(getApplicationContext());
            ConfigDao configDao = manager.getConfigDao();
            TaskDao taskDao = manager.getTaskDao();
            TaskBoxDao taskBoxDao = manager.getTaskBoxDao();
            TaskEscortDao taskEscortDao = manager.getTaskEscortDao();

            Config config = configDao.load(1L);
            StringBuilder msgSb = new StringBuilder();
            Socket socket = BaseComm.connect(config.getIp(), config.getPort(), 10000, msgSb);
            if (socket == null) {
                EventBus.getDefault().post(new DownTaskFinishEvent(false));
                return;
            }
            int result;
            DownTaskComm comm = new DownTaskComm(socket, config.getOrgCode(), taskDate);
            result = comm.executeComm();
            if (result == 0) {
                List<Task> taskList = comm.getTaskList();
                List<TaskBox> taskBoxList = comm.getTaskBoxList();
                List<TaskEscort> taskEscortList = comm.getTaskEscortList();
                if (taskList.size() > 0) {
                    taskDao.insertOrReplaceInTx(taskList);
                }
                if (taskBoxList.size() > 0) {
                    for (int i=0; i<taskBoxList.size(); i++) {
                        taskBoxDao.queryBuilder().where(TaskBoxDao.Properties.TaskCode.eq(taskBoxList.get(i).getTaskCode())).buildDelete().executeDeleteWithoutDetachingEntities();
                    }
                    taskBoxDao.insertInTx(taskBoxList);
                }
                if (taskEscortList.size() > 0) {
                    for (int i=0; i<taskEscortList.size(); i++) {
                        taskEscortDao.queryBuilder().where(TaskEscortDao.Properties.TaskCode.eq(taskEscortList.get(i).getTaskCode())).buildDelete().executeDeleteWithoutDetachingEntities();
                    }
                    taskEscortDao.insertInTx(taskEscortList);
                }
                EventBus.getDefault().post(new DownTaskFinishEvent(true));
            } else {
                EventBus.getDefault().post(new DownTaskFinishEvent(false));
            }
        } catch (Exception e) {
            EventBus.getDefault().post(new DownTaskFinishEvent(false));
        }

    }


}
