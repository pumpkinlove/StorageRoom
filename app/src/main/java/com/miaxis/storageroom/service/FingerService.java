package com.miaxis.storageroom.service;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.text.TextUtils;

import com.device.Device;
import com.miaxis.storageroom.bean.Escort;
import com.miaxis.storageroom.bean.TaskEscort;
import com.miaxis.storageroom.bean.Worker;
import com.miaxis.storageroom.event.DeviceCancelEvent;
import com.miaxis.storageroom.event.ToastEvent;
import com.miaxis.storageroom.event.VerifyEscortEvent;
import com.miaxis.storageroom.event.VerifyWorkerEvent;
import com.miaxis.storageroom.greendao.GreenDaoManager;
import com.miaxis.storageroom.greendao.gen.EscortDao;
import com.miaxis.storageroom.greendao.gen.TaskEscortDao;
import com.miaxis.storageroom.greendao.gen.WorkerDao;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class FingerService extends IntentService {

    private static final String ACTION_VERIFY_WORKER = "com.miaxis.storageroom.service.action.ACTION_VERIFY_WORKER";
    private static final String ACTION_VERIFY_ESCORT = "com.miaxis.storageroom.service.action.ACTION_VERIFY_ESCORT";
    private static final String ACTION_GET_FINGER = "com.miaxis.storageroom.service.action.ACTION_GET_FINGER";

    private static final String TASK_CODE = "com.miaxis.storageroom.service.extra.TASK_CODE";
    private static final String PASSED_ESCODE = "com.miaxis.storageroom.service.extra.PASSED_ESCODE";

    public FingerService() {
        super("FingerService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public static void startActionVerifyWorker(Context context) {
        Intent intent = new Intent(context, FingerService.class);
        intent.setAction(ACTION_VERIFY_WORKER);
        context.startService(intent);
    }

    public static void startActionVerifyEscort(Context context, String taskCode, String passedEscode) {
        Intent intent = new Intent(context, FingerService.class);
        intent.setAction(ACTION_VERIFY_ESCORT);
        intent.putExtra(TASK_CODE, taskCode);
        intent.putExtra(TASK_CODE, passedEscode);
        context.startService(intent);
    }

    public static void startActionGetFinger(Context context) {
        Intent intent = new Intent(context, FingerService.class);
        intent.setAction(ACTION_GET_FINGER);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_VERIFY_WORKER.equals(action)) {
                handleActionVerifyWorker();
            } else if (ACTION_VERIFY_ESCORT.equals(action)) {
                final String taskCode = intent.getStringExtra(TASK_CODE);
                final String passedEscode = intent.getStringExtra(PASSED_ESCODE);
                handleActionVerifyEscort(taskCode, passedEscode);
            } else if (ACTION_GET_FINGER.equals(action)) {
                handleActionGetFinger();
            }
        }
    }

    private void handleActionGetFinger() {
        try {
            String mFinger = getFinger();
            EventBus.getDefault().post(mFinger);
        } catch (Exception e) {
            EventBus.getDefault().post(null);
        }
    }

    private void handleActionVerifyWorker() {
        try {
            GreenDaoManager manager = GreenDaoManager.getInstance(this);
            WorkerDao workerDao = manager.getWorkerDao();
            List<Worker> workerList = workerDao.loadAll();
            if (null == workerList || workerList.size() == 0) {
                EventBus.getDefault().post(new VerifyWorkerEvent(VerifyWorkerEvent.NO_WORKER, null));
                return;
            }
            String curFinger = getFinger();
            if (TextUtils.isEmpty(curFinger)) {
                EventBus.getDefault().post(new VerifyWorkerEvent(VerifyWorkerEvent.FAIL, null));
                return;
            }
            for (int i=0; i<workerList.size(); i++) {
                for (int j=0; j<10; j++) {
                    String mFinger = workerList.get(i).getFinger(j);
                    if (mFinger == null || mFinger.length() == 0) {
                        continue;
                    }
                    int result = Device.verifyFinger(mFinger, curFinger, 3);
                    if (result == 0) {
                        EventBus.getDefault().post(new VerifyWorkerEvent(VerifyWorkerEvent.SUCCESS, workerList.get(i)));
                        return;
                    }
                }
            }
            EventBus.getDefault().post(new VerifyWorkerEvent(VerifyWorkerEvent.FAIL, null));
        } catch (Exception e) {
            EventBus.getDefault().post(new VerifyWorkerEvent(VerifyWorkerEvent.FAIL, null));
        }
    }

    private void handleActionVerifyEscort(String taskCode, String passedEscortCode) {
        try {
            String curFinger = getFinger();
            GreenDaoManager manager = GreenDaoManager.getInstance(this);
            EscortDao escortDao = manager.getEscortDao();
            TaskEscortDao taskEscortDao = manager.getTaskEscortDao();
            List<TaskEscort> taskEscortList = taskEscortDao.queryBuilder().where(TaskEscortDao.Properties.TaskCode.eq(taskCode)).list();
            List<String> escortCodes = new ArrayList<>();
            for (TaskEscort taskEscort : taskEscortList) {
                escortCodes.add(taskEscort.getEscortCode());
            }
            escortCodes = new ArrayList<>(new HashSet<>(escortCodes)); // 去重
            escortCodes.remove(passedEscortCode);
            List<Escort> escortList = escortDao.queryBuilder().where(EscortDao.Properties.Code.in(escortCodes)).list();
            if (null == escortList || escortList.size() == 0) {
                EventBus.getDefault().post(new VerifyEscortEvent(false, null));
                return;
            }
            for (int i=0; i<escortList.size(); i++) {
                for (int j=0; j<10; j++) {
                    String mFinger = escortList.get(i).getFinger(j);
                    if (mFinger == null || mFinger.length() == 0) {
                        continue;
                    }
                    int result = Device.verifyFinger(mFinger, curFinger, 3);
                    if (result == 0) {
                        EventBus.getDefault().post(new VerifyEscortEvent(true, escortList.get(i)));
                        return;
                    }
                }
            }
            EventBus.getDefault().post(new VerifyEscortEvent(false, null));
        } catch (Exception e) {
            EventBus.getDefault().post(new VerifyEscortEvent(false, null));
        }
    }

    private String getFinger() {
        try {
            byte[] finger = new byte[200];
            byte[] message = new byte[100];
            int result = Device.getFinger(10000, finger, message);
            if (result != 0) {
                String errmsg = new String(message, "GBK");
                EventBus.getDefault().post(new ToastEvent("指纹验证失败:"+errmsg));
                return null;
            }
            return new String(finger).trim();
        } catch (Exception e) {
            return null;
        } finally {
        }
    }

    @Subscribe
    public void onDeviceCancelEvent(DeviceCancelEvent e) {
        Device.cancel();
    }

}
