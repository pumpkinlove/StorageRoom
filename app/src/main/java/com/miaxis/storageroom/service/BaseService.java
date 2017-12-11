package com.miaxis.storageroom.service;

import android.app.IntentService;

import com.miaxis.storageroom.bean.Config;
import com.miaxis.storageroom.comm.BaseComm;
import com.miaxis.storageroom.greendao.GreenDaoManager;
import com.miaxis.storageroom.greendao.gen.ConfigDao;

import java.net.Socket;

/**
 * Created by xu.nan on 2017/12/11.
 */

public abstract class BaseService extends IntentService {
    public BaseService(String name) {
        super(name);
    }

    protected Socket socketComm() throws Exception {
        GreenDaoManager manager = GreenDaoManager.getInstance(getApplicationContext());
        ConfigDao configDao = manager.getConfigDao();
        Config config = configDao.load(1L);
        StringBuilder msgSb = new StringBuilder();
        return BaseComm.connect(config.getIp(), config.getPort(), 10000, msgSb);
    }

}
