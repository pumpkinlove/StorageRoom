package com.miaxis.storageroom.app;

import android.app.Application;
import android.widget.Toast;

import com.device.Device;
import com.miaxis.storageroom.bean.Worker;
import com.miaxis.storageroom.event.ToastEvent;
import com.miaxis.storageroom.greendao.GreenDaoManager;
import com.miaxis.storageroom.greendao.gen.ConfigDao;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by xu.nan on 2017/7/10.
 */

public class Storage_App extends Application {

    private Worker curWorker;

    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(this);
        GreenDaoManager.getInstance(this);
        Device.openFinger();
        Device.openRfid();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onToastEvent(ToastEvent e) {
        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
    }

    public Worker getCurWorker() {
        return curWorker;
    }

    public void setCurWorker(Worker curWorker) {
        this.curWorker = curWorker;
    }
}
