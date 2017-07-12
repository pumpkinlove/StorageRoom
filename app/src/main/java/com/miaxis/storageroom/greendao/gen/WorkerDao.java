package com.miaxis.storageroom.greendao.gen;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.miaxis.storageroom.bean.Worker;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "WORKER".
*/
public class WorkerDao extends AbstractDao<Worker, Long> {

    public static final String TABLENAME = "WORKER";

    /**
     * Properties of entity Worker.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, long.class, "id", true, "_id");
        public final static Property Name = new Property(1, String.class, "name", false, "NAME");
        public final static Property Code = new Property(2, String.class, "code", false, "CODE");
        public final static Property OrgCode = new Property(3, String.class, "orgCode", false, "ORG_CODE");
        public final static Property Phone = new Property(4, String.class, "phone", false, "PHONE");
        public final static Property IdCard = new Property(5, String.class, "idCard", false, "ID_CARD");
        public final static Property OpUser = new Property(6, String.class, "opUser", false, "OP_USER");
        public final static Property OpUserName = new Property(7, String.class, "opUserName", false, "OP_USER_NAME");
        public final static Property OpDate = new Property(8, String.class, "opDate", false, "OP_DATE");
        public final static Property Status = new Property(9, String.class, "status", false, "STATUS");
        public final static Property Finger0 = new Property(10, String.class, "finger0", false, "FINGER0");
        public final static Property Finger1 = new Property(11, String.class, "finger1", false, "FINGER1");
        public final static Property Finger2 = new Property(12, String.class, "finger2", false, "FINGER2");
        public final static Property Finger3 = new Property(13, String.class, "finger3", false, "FINGER3");
        public final static Property Finger4 = new Property(14, String.class, "finger4", false, "FINGER4");
        public final static Property Finger5 = new Property(15, String.class, "finger5", false, "FINGER5");
        public final static Property Finger6 = new Property(16, String.class, "finger6", false, "FINGER6");
        public final static Property Finger7 = new Property(17, String.class, "finger7", false, "FINGER7");
        public final static Property Finger8 = new Property(18, String.class, "finger8", false, "FINGER8");
        public final static Property Finger9 = new Property(19, String.class, "finger9", false, "FINGER9");
    }


    public WorkerDao(DaoConfig config) {
        super(config);
    }
    
    public WorkerDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"WORKER\" (" + //
                "\"_id\" INTEGER PRIMARY KEY NOT NULL ," + // 0: id
                "\"NAME\" TEXT," + // 1: name
                "\"CODE\" TEXT," + // 2: code
                "\"ORG_CODE\" TEXT," + // 3: orgCode
                "\"PHONE\" TEXT," + // 4: phone
                "\"ID_CARD\" TEXT," + // 5: idCard
                "\"OP_USER\" TEXT," + // 6: opUser
                "\"OP_USER_NAME\" TEXT," + // 7: opUserName
                "\"OP_DATE\" TEXT," + // 8: opDate
                "\"STATUS\" TEXT," + // 9: status
                "\"FINGER0\" TEXT," + // 10: finger0
                "\"FINGER1\" TEXT," + // 11: finger1
                "\"FINGER2\" TEXT," + // 12: finger2
                "\"FINGER3\" TEXT," + // 13: finger3
                "\"FINGER4\" TEXT," + // 14: finger4
                "\"FINGER5\" TEXT," + // 15: finger5
                "\"FINGER6\" TEXT," + // 16: finger6
                "\"FINGER7\" TEXT," + // 17: finger7
                "\"FINGER8\" TEXT," + // 18: finger8
                "\"FINGER9\" TEXT);"); // 19: finger9
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"WORKER\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, Worker entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.getId());
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(2, name);
        }
 
        String code = entity.getCode();
        if (code != null) {
            stmt.bindString(3, code);
        }
 
        String orgCode = entity.getOrgCode();
        if (orgCode != null) {
            stmt.bindString(4, orgCode);
        }
 
        String phone = entity.getPhone();
        if (phone != null) {
            stmt.bindString(5, phone);
        }
 
        String idCard = entity.getIdCard();
        if (idCard != null) {
            stmt.bindString(6, idCard);
        }
 
        String opUser = entity.getOpUser();
        if (opUser != null) {
            stmt.bindString(7, opUser);
        }
 
        String opUserName = entity.getOpUserName();
        if (opUserName != null) {
            stmt.bindString(8, opUserName);
        }
 
        String opDate = entity.getOpDate();
        if (opDate != null) {
            stmt.bindString(9, opDate);
        }
 
        String status = entity.getStatus();
        if (status != null) {
            stmt.bindString(10, status);
        }
 
        String finger0 = entity.getFinger0();
        if (finger0 != null) {
            stmt.bindString(11, finger0);
        }
 
        String finger1 = entity.getFinger1();
        if (finger1 != null) {
            stmt.bindString(12, finger1);
        }
 
        String finger2 = entity.getFinger2();
        if (finger2 != null) {
            stmt.bindString(13, finger2);
        }
 
        String finger3 = entity.getFinger3();
        if (finger3 != null) {
            stmt.bindString(14, finger3);
        }
 
        String finger4 = entity.getFinger4();
        if (finger4 != null) {
            stmt.bindString(15, finger4);
        }
 
        String finger5 = entity.getFinger5();
        if (finger5 != null) {
            stmt.bindString(16, finger5);
        }
 
        String finger6 = entity.getFinger6();
        if (finger6 != null) {
            stmt.bindString(17, finger6);
        }
 
        String finger7 = entity.getFinger7();
        if (finger7 != null) {
            stmt.bindString(18, finger7);
        }
 
        String finger8 = entity.getFinger8();
        if (finger8 != null) {
            stmt.bindString(19, finger8);
        }
 
        String finger9 = entity.getFinger9();
        if (finger9 != null) {
            stmt.bindString(20, finger9);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, Worker entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.getId());
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(2, name);
        }
 
        String code = entity.getCode();
        if (code != null) {
            stmt.bindString(3, code);
        }
 
        String orgCode = entity.getOrgCode();
        if (orgCode != null) {
            stmt.bindString(4, orgCode);
        }
 
        String phone = entity.getPhone();
        if (phone != null) {
            stmt.bindString(5, phone);
        }
 
        String idCard = entity.getIdCard();
        if (idCard != null) {
            stmt.bindString(6, idCard);
        }
 
        String opUser = entity.getOpUser();
        if (opUser != null) {
            stmt.bindString(7, opUser);
        }
 
        String opUserName = entity.getOpUserName();
        if (opUserName != null) {
            stmt.bindString(8, opUserName);
        }
 
        String opDate = entity.getOpDate();
        if (opDate != null) {
            stmt.bindString(9, opDate);
        }
 
        String status = entity.getStatus();
        if (status != null) {
            stmt.bindString(10, status);
        }
 
        String finger0 = entity.getFinger0();
        if (finger0 != null) {
            stmt.bindString(11, finger0);
        }
 
        String finger1 = entity.getFinger1();
        if (finger1 != null) {
            stmt.bindString(12, finger1);
        }
 
        String finger2 = entity.getFinger2();
        if (finger2 != null) {
            stmt.bindString(13, finger2);
        }
 
        String finger3 = entity.getFinger3();
        if (finger3 != null) {
            stmt.bindString(14, finger3);
        }
 
        String finger4 = entity.getFinger4();
        if (finger4 != null) {
            stmt.bindString(15, finger4);
        }
 
        String finger5 = entity.getFinger5();
        if (finger5 != null) {
            stmt.bindString(16, finger5);
        }
 
        String finger6 = entity.getFinger6();
        if (finger6 != null) {
            stmt.bindString(17, finger6);
        }
 
        String finger7 = entity.getFinger7();
        if (finger7 != null) {
            stmt.bindString(18, finger7);
        }
 
        String finger8 = entity.getFinger8();
        if (finger8 != null) {
            stmt.bindString(19, finger8);
        }
 
        String finger9 = entity.getFinger9();
        if (finger9 != null) {
            stmt.bindString(20, finger9);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.getLong(offset + 0);
    }    

    @Override
    public Worker readEntity(Cursor cursor, int offset) {
        Worker entity = new Worker( //
            cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // name
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // code
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // orgCode
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // phone
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // idCard
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // opUser
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // opUserName
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // opDate
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // status
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10), // finger0
            cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11), // finger1
            cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12), // finger2
            cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13), // finger3
            cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14), // finger4
            cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15), // finger5
            cursor.isNull(offset + 16) ? null : cursor.getString(offset + 16), // finger6
            cursor.isNull(offset + 17) ? null : cursor.getString(offset + 17), // finger7
            cursor.isNull(offset + 18) ? null : cursor.getString(offset + 18), // finger8
            cursor.isNull(offset + 19) ? null : cursor.getString(offset + 19) // finger9
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, Worker entity, int offset) {
        entity.setId(cursor.getLong(offset + 0));
        entity.setName(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setCode(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setOrgCode(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setPhone(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setIdCard(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setOpUser(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setOpUserName(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setOpDate(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setStatus(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setFinger0(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
        entity.setFinger1(cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11));
        entity.setFinger2(cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12));
        entity.setFinger3(cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13));
        entity.setFinger4(cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14));
        entity.setFinger5(cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15));
        entity.setFinger6(cursor.isNull(offset + 16) ? null : cursor.getString(offset + 16));
        entity.setFinger7(cursor.isNull(offset + 17) ? null : cursor.getString(offset + 17));
        entity.setFinger8(cursor.isNull(offset + 18) ? null : cursor.getString(offset + 18));
        entity.setFinger9(cursor.isNull(offset + 19) ? null : cursor.getString(offset + 19));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(Worker entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(Worker entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(Worker entity) {
        throw new UnsupportedOperationException("Unsupported for entities with a non-null key");
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}