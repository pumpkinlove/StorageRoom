package com.miaxis.storageroom.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.device.Device;
import com.miaxis.storageroom.bean.Box;
import com.miaxis.storageroom.event.DeviceCancelEvent;
import com.miaxis.storageroom.event.ScanBoxEvent;
import com.miaxis.storageroom.event.ToastEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class ScanRfidService extends IntentService {
    private static final String ACTION_RFID = "com.miaxis.storageroom.service.action.RFID";

    public ScanRfidService() {
        super("ScanRfidService");
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

    public static void startActionRfid(Context context) {
        Intent intent = new Intent(context, ScanRfidService.class);
        intent.setAction(ACTION_RFID);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_RFID.equals(action)) {
                scanRfid();
            }
        }
    }

    boolean scanFlag;

    private void scanRfid() {
        try {
            scanFlag = true;
            while (scanFlag) {
                byte[] tids = new byte[20000];
                byte[] epcids = new byte[20000];
                byte[] errmsg = new byte[100];

                int result = com.device.Device.getRfid(1000, tids, epcids, errmsg);
                if (result != 0) {
                    EventBus.getDefault().post(new ToastEvent("扫描失败：" + new String(errmsg, "GBK").trim()));
                    continue;
                }
                String boxRfids = new String(epcids).trim();
                EventBus.getDefault().post(new ScanBoxEvent(boxRfids));
            }
        } catch (Exception e) {
        } finally {
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDismiss(DeviceCancelEvent e) {
        scanFlag = false;
    }
}
