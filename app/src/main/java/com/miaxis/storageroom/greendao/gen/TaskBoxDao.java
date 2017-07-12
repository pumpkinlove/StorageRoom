package com.miaxis.storageroom.greendao.gen;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.miaxis.storageroom.bean.TaskBox;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "TASK_BOX".
*/
public class TaskBoxDao extends AbstractDao<TaskBox, Void> {

    public static final String TABLENAME = "TASK_BOX";

    /**
     * Properties of entity TaskBox.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property BoxCode = new Property(0, String.class, "boxCode", false, "BOX_CODE");
        public final static Property BoxRfid = new Property(1, String.class, "boxRfid", false, "BOX_RFID");
        public final static Property TaskCode = new Property(2, String.class, "taskCode", false, "TASK_CODE");
    }


    public TaskBoxDao(DaoConfig config) {
        super(config);
    }
    
    public TaskBoxDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"TASK_BOX\" (" + //
                "\"BOX_CODE\" TEXT," + // 0: boxCode
                "\"BOX_RFID\" TEXT," + // 1: boxRfid
                "\"TASK_CODE\" TEXT);"); // 2: taskCode
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"TASK_BOX\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, TaskBox entity) {
        stmt.clearBindings();
 
        String boxCode = entity.getBoxCode();
        if (boxCode != null) {
            stmt.bindString(1, boxCode);
        }
 
        String boxRfid = entity.getBoxRfid();
        if (boxRfid != null) {
            stmt.bindString(2, boxRfid);
        }
 
        String taskCode = entity.getTaskCode();
        if (taskCode != null) {
            stmt.bindString(3, taskCode);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, TaskBox entity) {
        stmt.clearBindings();
 
        String boxCode = entity.getBoxCode();
        if (boxCode != null) {
            stmt.bindString(1, boxCode);
        }
 
        String boxRfid = entity.getBoxRfid();
        if (boxRfid != null) {
            stmt.bindString(2, boxRfid);
        }
 
        String taskCode = entity.getTaskCode();
        if (taskCode != null) {
            stmt.bindString(3, taskCode);
        }
    }

    @Override
    public Void readKey(Cursor cursor, int offset) {
        return null;
    }    

    @Override
    public TaskBox readEntity(Cursor cursor, int offset) {
        TaskBox entity = new TaskBox( //
            cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // boxCode
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // boxRfid
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2) // taskCode
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, TaskBox entity, int offset) {
        entity.setBoxCode(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setBoxRfid(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setTaskCode(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
     }
    
    @Override
    protected final Void updateKeyAfterInsert(TaskBox entity, long rowId) {
        // Unsupported or missing PK type
        return null;
    }
    
    @Override
    public Void getKey(TaskBox entity) {
        return null;
    }

    @Override
    public boolean hasKey(TaskBox entity) {
        // TODO
        return false;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}