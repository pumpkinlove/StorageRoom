package com.miaxis.storageroom.service;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;

import com.miaxis.storageroom.bean.Config;
import com.miaxis.storageroom.bean.TaskExe;
import com.miaxis.storageroom.comm.BaseComm;
import com.miaxis.storageroom.comm.ExecTaskComm;
import com.miaxis.storageroom.event.HandOverDoneEvent;
import com.miaxis.storageroom.greendao.GreenDaoManager;
import com.miaxis.storageroom.greendao.gen.ConfigDao;
import com.miaxis.storageroom.greendao.gen.TimeStampDao;
import com.miaxis.storageroom.greendao.gen.WorkerDao;

import org.greenrobot.eventbus.EventBus;

import java.net.Socket;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class HandOverService extends IntentService {
    private static final String ACTION_EXEC_TASK = "com.miaxis.storageroom.service.action.ACTION_EXEC_TASK";

    private static final String TASK_EXE = "com.miaxis.storageroom.service.extra.TASK_EXE";

    public HandOverService() {
        super("HandOverService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionFoo(Context context, TaskExe taskExe) {
        Intent intent = new Intent(context, HandOverService.class);
        intent.setAction(ACTION_EXEC_TASK);
        intent.putExtra(TASK_EXE, taskExe);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_EXEC_TASK.equals(action)) {
                final TaskExe taskExe = (TaskExe) intent.getSerializableExtra(TASK_EXE);
                handleActionExecTask(taskExe);
            }
        }
    }

    private void handleActionExecTask(TaskExe taskExe) {

        try {
            GreenDaoManager manager = GreenDaoManager.getInstance(getApplicationContext());
            ConfigDao configDao = manager.getConfigDao();
            Config config = configDao.load(1L);

            StringBuilder msgSb = new StringBuilder();
            Socket socket = BaseComm.connect(config.getIp(), config.getPort(), 10000, msgSb);
            if (socket == null) {
                EventBus.getDefault().post(new HandOverDoneEvent(-1));
                return;
            }
            ExecTaskComm comm = new ExecTaskComm(socket, taskExe);
            int re = comm.executeComm();
            EventBus.getDefault().post(new HandOverDoneEvent(re));
        } catch (Exception e) {
            EventBus.getDefault().post(new HandOverDoneEvent(-1));
        }

     }

}
