package com.miaxis.storageroom.greendao;

import android.app.AppOpsManager;
import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.miaxis.storageroom.app.Storage_App;
import com.miaxis.storageroom.greendao.gen.ConfigDao;
import com.miaxis.storageroom.greendao.gen.DaoMaster;
import com.miaxis.storageroom.greendao.gen.DaoSession;
import com.miaxis.storageroom.greendao.gen.EscortDao;
import com.miaxis.storageroom.greendao.gen.TaskBoxDao;
import com.miaxis.storageroom.greendao.gen.TaskDao;
import com.miaxis.storageroom.greendao.gen.TaskEscortDao;
import com.miaxis.storageroom.greendao.gen.TimeStampDao;
import com.miaxis.storageroom.greendao.gen.WorkerDao;

import java.security.Timestamp;

/**
 * Created by xu.nan on 2017/7/11.
 */

public class GreenDaoManager {

    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;
    private static volatile GreenDaoManager mInstance = null;

    private GreenDaoManager(Context context){
        if (mInstance == null) {
            DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(new GreenDaoContext(context), "StorageRoom.db", null);
            SQLiteDatabase db = helper.getWritableDatabase();
            mDaoMaster = new DaoMaster(db);
            mDaoSession = mDaoMaster.newSession();
        }
    }

    public static GreenDaoManager getInstance(Context context) {
        if (mInstance == null) {
            synchronized (GreenDaoManager.class) {
                if (mInstance == null) {
                    mInstance = new GreenDaoManager(context);
                }
            }
        }
        return mInstance;
    }

    public DaoMaster getMaster() {
        return mDaoMaster;
    }

    public DaoSession getSession() {
        return mDaoSession;
    }

    public DaoSession getNewSession() {
        mDaoSession = mDaoMaster.newSession();
        return mDaoSession;
    }

    public ConfigDao getConfigDao() throws Exception {
        return mDaoSession.getConfigDao();
    }

    public TimeStampDao getTimeStampDao() throws Exception {
        return mDaoSession.getTimeStampDao();
    }

    public WorkerDao getWorkerDao() throws Exception {
        return mDaoSession.getWorkerDao();
    }

    public EscortDao getEscortDao() throws Exception {
        return mDaoSession.getEscortDao();
    }

    public TaskDao getTaskDao() throws Exception {
        return mDaoSession.getTaskDao();
    }

    public TaskBoxDao getTaskBoxDao() throws Exception {
        return mDaoSession.getTaskBoxDao();
    }

    public TaskEscortDao getTaskEscortDao() throws Exception{
        return mDaoSession.getTaskEscortDao();
    }
}
