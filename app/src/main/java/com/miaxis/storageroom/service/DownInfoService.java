package com.miaxis.storageroom.service;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.miaxis.storageroom.bean.Config;
import com.miaxis.storageroom.bean.Escort;
import com.miaxis.storageroom.bean.TimeStamp;
import com.miaxis.storageroom.bean.Worker;
import com.miaxis.storageroom.comm.BaseComm;
import com.miaxis.storageroom.comm.CountEscortComm;
import com.miaxis.storageroom.comm.DownEscortComm;
import com.miaxis.storageroom.comm.DownWorkerComm;
import com.miaxis.storageroom.event.DownEscortEvent;
import com.miaxis.storageroom.event.DownWorkerEvent;
import com.miaxis.storageroom.event.ToastEvent;
import com.miaxis.storageroom.greendao.GreenDaoManager;
import com.miaxis.storageroom.greendao.gen.ConfigDao;
import com.miaxis.storageroom.greendao.gen.EscortDao;
import com.miaxis.storageroom.greendao.gen.TimeStampDao;
import com.miaxis.storageroom.greendao.gen.WorkerDao;

import org.greenrobot.eventbus.EventBus;

import java.net.Socket;
import java.util.List;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class DownInfoService extends IntentService {
    private static final String TAG = "DownInfoService";

    private static final String ACTION_DOWN_ESCORT = "com.miaxis.storageroom.service.action.ACTION_DOWN_ESCORT";
    private static final String ACTION_DOWN_WORKER = "com.miaxis.storageroom.service.action.ACTION_DOWN_WORKER";
    private static final String ACTION_DOWN_BOX = "com.miaxis.storageroom.service.action.ACTION_DOWN_BOX";

    public static final String TOTAL_PAGE_NUM = "com.miaxis.storageroom.service.extra.TOTAL_PAGE_NUM";
    public static final String PAGE_SIZE = "com.miaxis.storageroom.service.extra.PAGE_SIZE";

    public DownInfoService() {
        super("DownInfoService");
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    public static void startActionDownWorker(Context context) {
        Intent intent = new Intent(context, DownInfoService.class);
        intent.setAction(ACTION_DOWN_WORKER);
        context.startService(intent);
    }

    public static void startActionDownEscort(Context context) {
        Intent intent = new Intent(context, DownInfoService.class);
        intent.setAction(ACTION_DOWN_ESCORT);
        context.startService(intent);
    }

    public static void startActionDownBox(Context context) {
        Intent intent = new Intent(context, DownInfoService.class);
        intent.setAction(ACTION_DOWN_BOX);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_DOWN_WORKER.equals(action)) {
                handleActionDownWorker();
            } else if (ACTION_DOWN_ESCORT.equals(action)) {
                handleActionDownEscort();
            } else if (ACTION_DOWN_BOX.equals(action)) {
                handleActionDownBox();
            }
        }
    }

    private void handleActionDownWorker() {
        try {
            Log.e(TAG, "handleActionDownWorker");
            GreenDaoManager manager = GreenDaoManager.getInstance(getApplicationContext());
            ConfigDao configDao = manager.getConfigDao();
            TimeStampDao timeStampDao = manager.getTimeStampDao();
            WorkerDao workerDao = manager.getWorkerDao();

            Config config = configDao.load(1L);
            TimeStamp workerStamp = timeStampDao.load(1L);
            String timeStamp = "";
            if (workerStamp != null) {
                timeStamp = workerStamp.getOpDateTime();
                if (TextUtils.isEmpty(timeStamp)) {
                    timeStamp = "";
                }
            } else {
                timeStamp = "";
            }
            StringBuilder msgSb = new StringBuilder();
            Socket socket = BaseComm.connect(config.getIp(), config.getPort(), 10000, msgSb);
            if (socket == null) {
                EventBus.getDefault().post(new DownWorkerEvent(DownWorkerEvent.FAILURE));
                return;
            }
            int result;
            DownWorkerComm comm = new DownWorkerComm(socket, config.getOrgCode(), timeStamp);
            result = comm.executeComm();
            if (result == 0) {
                List<Worker> workerList = comm.getWorkerList();
                if (workerList != null && workerList.size() > 0) {
                    workerDao.insertOrReplaceInTx(workerList);
                    if (workerStamp == null) {
                        workerStamp = new TimeStamp();
                    }
                    workerStamp.setId(1L);
                    workerStamp.setStampName("WORKER_STAMP");
                    workerStamp.setOpDateTime(comm.getTimeStamp());
                    timeStampDao.insertOrReplace(workerStamp);
                }
                EventBus.getDefault().post(new DownWorkerEvent(DownWorkerEvent.SUCCESS));
            } else {
                EventBus.getDefault().post(new DownWorkerEvent(DownWorkerEvent.FAILURE));
            }
        } catch (Exception e) {
            EventBus.getDefault().post(new DownWorkerEvent(DownWorkerEvent.FAILURE));
        }
    }

    private void handleActionDownBox() {

    }

    private void handleActionDownEscort() {
        int pageSize = 100;
        int re = -1;
        try {
            re = doCountEscortComm();
            if (re < 0) {
                return;
            }
            int totalPageNum = re % pageSize == 0 ? (re / pageSize) : (re / pageSize + 1);
            for (int i = 1; i <= totalPageNum; i ++) {
                re = doDownEscortComm(i + "", pageSize+"");
            }
        } catch (Exception e) {
            EventBus.getDefault().post(new ToastEvent(e.getMessage()));
            re = -1;
        } finally {
            EventBus.getDefault().post(new DownEscortEvent(re));
        }
    }

    private int doCountEscortComm() {
        try {
            GreenDaoManager manager = GreenDaoManager.getInstance(getApplicationContext());
            ConfigDao configDao = manager.getConfigDao();
            TimeStampDao timeStampDao = manager.getTimeStampDao();
            TimeStamp escortStamp = timeStampDao.load(2L);
            Config config = configDao.load(1L);
            String timeStamp = "";
            if (escortStamp != null) {
                timeStamp = escortStamp.getOpDateTime();
                if (TextUtils.isEmpty(timeStamp)) {
                    timeStamp = "";
                }
            } else {
                timeStamp = "";
            }
            StringBuilder sbMsg = new StringBuilder();
            Socket socket = BaseComm.connect(config.getIp(), config.getPort(), 10000, sbMsg);
            if (socket == null) {
                return -1;
            }
            int result;
            CountEscortComm comm = new CountEscortComm(socket, config.getOrgCode(), timeStamp);
            result = comm.executeComm();
            if (result == 0) {
                return comm.getEscortCount();
            } else {
                return -1;
            }
        } catch (Exception e) {
            return -1;
        }
    }

    private int doDownEscortComm(String pageNum, String pageSize) throws Exception {
        GreenDaoManager manager = GreenDaoManager.getInstance(getApplicationContext());
        ConfigDao configDao = manager.getConfigDao();
        TimeStampDao timeStampDao = manager.getTimeStampDao();
        EscortDao escortDao = manager.getEscortDao();

        Config config = configDao.load(1L);
        TimeStamp escortStamp = timeStampDao.load(2L);
        String timeStamp = "";
        if (escortStamp != null) {
            timeStamp = escortStamp.getOpDateTime();
            if (TextUtils.isEmpty(timeStamp)) {
                timeStamp = "";
            }
        } else {
            timeStamp = "";
        }
        StringBuilder msgSb = new StringBuilder();
        Socket socket = BaseComm.connect(config.getIp(), config.getPort(), 10000, msgSb);
        if (socket == null) {
            return -1;
        }
        int result;
        DownEscortComm comm = new DownEscortComm(socket, config.getOrgCode(), timeStamp, pageNum, pageSize);
        result = comm.executeComm();
        if (result == 0) {
            List<Escort> escorts = comm.getEscortList();
            if (escorts != null && escorts.size() > 0) {
                escortDao.insertOrReplaceInTx(escorts);
                if (escortStamp == null) {
                    escortStamp = new TimeStamp();
                }
                escortStamp.setId(2L);
                escortStamp.setStampName("ESCORT_STAMP");
                escortStamp.setOpDateTime(comm.getTimeStamp());
                timeStampDao.insertOrReplace(escortStamp);
            }
        }
        return result;
    }
}
