package com.miaxis.storageroom.service;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;

import com.miaxis.storageroom.bean.Config;
import com.miaxis.storageroom.bean.TimeStamp;
import com.miaxis.storageroom.bean.Worker;
import com.miaxis.storageroom.comm.AddWorkerComm;
import com.miaxis.storageroom.comm.BaseComm;
import com.miaxis.storageroom.event.CommExecEvent;
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
public class AddWorkerService extends IntentService {
    private static final String ACTION_ADD_WORKER = "com.miaxis.storageroom.service.action.ACTION_ADD_WORKER";

    private static final String ADD_WORKER = "com.miaxis.storageroom.service.extra.ADD_WORKER";

    public AddWorkerService() {
        super("AddWorkerService");
    }

    public static void startActionAddWorker(Context context, Worker worker) {
        Intent intent = new Intent(context, AddWorkerService.class);
        intent.setAction(ACTION_ADD_WORKER);
        intent.putExtra(ADD_WORKER, worker);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_ADD_WORKER.equals(action)) {
                final Worker worker = (Worker) intent.getSerializableExtra(ADD_WORKER);
                handleActionAddWorker(worker);
            }
        }
    }

    private void handleActionAddWorker(Worker worker) {

        try {
            GreenDaoManager manager = GreenDaoManager.getInstance(getApplicationContext());
            ConfigDao configDao = manager.getConfigDao();
            TimeStampDao timeStampDao = manager.getTimeStampDao();
            WorkerDao workerDao = manager.getWorkerDao();

            Config config = configDao.load(1L);
            TimeStamp workerStamp = timeStampDao.load(1L);
            String timeStamp = "";
            StringBuilder msgSb = new StringBuilder();
            Socket socket = BaseComm.connect(config.getIp(), config.getPort(), 10000, msgSb);
            if (socket == null) {
                EventBus.getDefault().post(new CommExecEvent(CommExecEvent.RESULT_SOCKET_NULL, CommExecEvent.COMM_ADD_WORKER));
                return;
            }
            worker.setOrgCode(config.getOrgCode());
            AddWorkerComm comm = new AddWorkerComm(socket, worker);
            int result = comm.executeComm();
            if (result == 0) {
                workerDao.insert(worker);
            }
            EventBus.getDefault().post(new CommExecEvent(result, CommExecEvent.COMM_ADD_WORKER));
        } catch (Exception e) {
            EventBus.getDefault().post(new CommExecEvent(CommExecEvent.RESULT_EXCEPTION, CommExecEvent.COMM_ADD_WORKER));
        }

    }

}
