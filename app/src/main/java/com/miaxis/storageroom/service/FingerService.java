package com.miaxis.storageroom.service;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;

import com.device.Device;
import com.miaxis.storageroom.bean.Worker;
import com.miaxis.storageroom.event.DeviceCancelEvent;
import com.miaxis.storageroom.event.ToastEvent;
import com.miaxis.storageroom.event.VerifyWorkerEvent;
import com.miaxis.storageroom.greendao.GreenDaoManager;
import com.miaxis.storageroom.greendao.gen.WorkerDao;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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

    public static void startActionVerifyEscort(Context context) {
        Intent intent = new Intent(context, FingerService.class);
        intent.setAction(ACTION_VERIFY_ESCORT);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_VERIFY_WORKER.equals(action)) {
                handleActionVerifyWorker();
            } else if (ACTION_VERIFY_ESCORT.equals(action)) {
                handleActionVerifyEscort();
            }
        }
    }

    private void handleActionVerifyWorker() {
        try {
            String curFinger = getFinger();
            GreenDaoManager manager = GreenDaoManager.getInstance(this);
            WorkerDao workerDao = manager.getWorkerDao();
            List<Worker> workerList = workerDao.loadAll();
            if (null == workerList || workerList.size() == 0) {
                EventBus.getDefault().post(new VerifyWorkerEvent(false, null));
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
                        EventBus.getDefault().post(new VerifyWorkerEvent(true, workerList.get(i)));
                        return;
                    }
                }
            }
            EventBus.getDefault().post(new VerifyWorkerEvent(false, null));
        } catch (Exception e) {
            EventBus.getDefault().post(new VerifyWorkerEvent(false, null));
        }
    }

    private void handleActionVerifyEscort() {
    }

    private String getFinger() throws Exception {
        try {
            Device.openFinger();
            Device.openRfid();
            Thread.sleep(500);
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
            Device.closeRfid();
            Device.closeFinger();
        }
    }

    @Subscribe
    public void onDeviceCancelEvent(DeviceCancelEvent e) {
        Device.cancel();
    }

}
