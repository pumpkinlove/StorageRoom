package com.miaxis.storageroom.greendao.gen;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.miaxis.storageroom.bean.Escort;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "ESCORT".
*/
public class EscortDao extends AbstractDao<Escort, Long> {

    public static final String TABLENAME = "ESCORT";

    /**
     * Properties of entity Escort.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, long.class, "id", true, "_id");
        public final static Property Name = new Property(1, String.class, "name", false, "NAME");
        public final static Property Finger0 = new Property(2, String.class, "finger0", false, "FINGER0");
        public final static Property Finger1 = new Property(3, String.class, "finger1", false, "FINGER1");
        public final static Property Finger2 = new Property(4, String.class, "finger2", false, "FINGER2");
        public final static Property Finger3 = new Property(5, String.class, "finger3", false, "FINGER3");
        public final static Property Finger4 = new Property(6, String.class, "finger4", false, "FINGER4");
        public final static Property Finger5 = new Property(7, String.class, "finger5", false, "FINGER5");
        public final static Property Finger6 = new Property(8, String.class, "finger6", false, "FINGER6");
        public final static Property Finger7 = new Property(9, String.class, "finger7", false, "FINGER7");
        public final static Property Finger8 = new Property(10, String.class, "finger8", false, "FINGER8");
        public final static Property Finger9 = new Property(11, String.class, "finger9", false, "FINGER9");
        public final static Property Code = new Property(12, String.class, "code", false, "CODE");
        public final static Property Photo = new Property(13, byte[].class, "photo", false, "PHOTO");
        public final static Property Password = new Property(14, String.class, "password", false, "PASSWORD");
        public final static Property OpDate = new Property(15, String.class, "opDate", false, "OP_DATE");
        public final static Property OpUserCode = new Property(16, String.class, "opUserCode", false, "OP_USER_CODE");
        public final static Property OpUserName = new Property(17, String.class, "opUserName", false, "OP_USER_NAME");
        public final static Property IsCollected = new Property(18, boolean.class, "isCollected", false, "IS_COLLECTED");
        public final static Property Phone = new Property(19, String.class, "phone", false, "PHONE");
        public final static Property IdCard = new Property(20, String.class, "idCard", false, "ID_CARD");
    }


    public EscortDao(DaoConfig config) {
        super(config);
    }
    
    public EscortDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"ESCORT\" (" + //
                "\"_id\" INTEGER PRIMARY KEY NOT NULL ," + // 0: id
                "\"NAME\" TEXT," + // 1: name
                "\"FINGER0\" TEXT," + // 2: finger0
                "\"FINGER1\" TEXT," + // 3: finger1
                "\"FINGER2\" TEXT," + // 4: finger2
                "\"FINGER3\" TEXT," + // 5: finger3
                "\"FINGER4\" TEXT," + // 6: finger4
                "\"FINGER5\" TEXT," + // 7: finger5
                "\"FINGER6\" TEXT," + // 8: finger6
                "\"FINGER7\" TEXT," + // 9: finger7
                "\"FINGER8\" TEXT," + // 10: finger8
                "\"FINGER9\" TEXT," + // 11: finger9
                "\"CODE\" TEXT," + // 12: code
                "\"PHOTO\" BLOB," + // 13: photo
                "\"PASSWORD\" TEXT," + // 14: password
                "\"OP_DATE\" TEXT," + // 15: opDate
                "\"OP_USER_CODE\" TEXT," + // 16: opUserCode
                "\"OP_USER_NAME\" TEXT," + // 17: opUserName
                "\"IS_COLLECTED\" INTEGER NOT NULL ," + // 18: isCollected
                "\"PHONE\" TEXT," + // 19: phone
                "\"ID_CARD\" TEXT);"); // 20: idCard
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"ESCORT\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, Escort entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.getId());
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(2, name);
        }
 
        String finger0 = entity.getFinger0();
        if (finger0 != null) {
            stmt.bindString(3, finger0);
        }
 
        String finger1 = entity.getFinger1();
        if (finger1 != null) {
            stmt.bindString(4, finger1);
        }
 
        String finger2 = entity.getFinger2();
        if (finger2 != null) {
            stmt.bindString(5, finger2);
        }
 
        String finger3 = entity.getFinger3();
        if (finger3 != null) {
            stmt.bindString(6, finger3);
        }
 
        String finger4 = entity.getFinger4();
        if (finger4 != null) {
            stmt.bindString(7, finger4);
        }
 
        String finger5 = entity.getFinger5();
        if (finger5 != null) {
            stmt.bindString(8, finger5);
        }
 
        String finger6 = entity.getFinger6();
        if (finger6 != null) {
            stmt.bindString(9, finger6);
        }
 
        String finger7 = entity.getFinger7();
        if (finger7 != null) {
            stmt.bindString(10, finger7);
        }
 
        String finger8 = entity.getFinger8();
        if (finger8 != null) {
            stmt.bindString(11, finger8);
        }
 
        String finger9 = entity.getFinger9();
        if (finger9 != null) {
            stmt.bindString(12, finger9);
        }
 
        String code = entity.getCode();
        if (code != null) {
            stmt.bindString(13, code);
        }
 
        byte[] photo = entity.getPhoto();
        if (photo != null) {
            stmt.bindBlob(14, photo);
        }
 
        String password = entity.getPassword();
        if (password != null) {
            stmt.bindString(15, password);
        }
 
        String opDate = entity.getOpDate();
        if (opDate != null) {
            stmt.bindString(16, opDate);
        }
 
        String opUserCode = entity.getOpUserCode();
        if (opUserCode != null) {
            stmt.bindString(17, opUserCode);
        }
 
        String opUserName = entity.getOpUserName();
        if (opUserName != null) {
            stmt.bindString(18, opUserName);
        }
        stmt.bindLong(19, entity.getIsCollected() ? 1L: 0L);
 
        String phone = entity.getPhone();
        if (phone != null) {
            stmt.bindString(20, phone);
        }
 
        String idCard = entity.getIdCard();
        if (idCard != null) {
            stmt.bindString(21, idCard);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, Escort entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.getId());
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(2, name);
        }
 
        String finger0 = entity.getFinger0();
        if (finger0 != null) {
            stmt.bindString(3, finger0);
        }
 
        String finger1 = entity.getFinger1();
        if (finger1 != null) {
            stmt.bindString(4, finger1);
        }
 
        String finger2 = entity.getFinger2();
        if (finger2 != null) {
            stmt.bindString(5, finger2);
        }
 
        String finger3 = entity.getFinger3();
        if (finger3 != null) {
            stmt.bindString(6, finger3);
        }
 
        String finger4 = entity.getFinger4();
        if (finger4 != null) {
            stmt.bindString(7, finger4);
        }
 
        String finger5 = entity.getFinger5();
        if (finger5 != null) {
            stmt.bindString(8, finger5);
        }
 
        String finger6 = entity.getFinger6();
        if (finger6 != null) {
            stmt.bindString(9, finger6);
        }
 
        String finger7 = entity.getFinger7();
        if (finger7 != null) {
            stmt.bindString(10, finger7);
        }
 
        String finger8 = entity.getFinger8();
        if (finger8 != null) {
            stmt.bindString(11, finger8);
        }
 
        String finger9 = entity.getFinger9();
        if (finger9 != null) {
            stmt.bindString(12, finger9);
        }
 
        String code = entity.getCode();
        if (code != null) {
            stmt.bindString(13, code);
        }
 
        byte[] photo = entity.getPhoto();
        if (photo != null) {
            stmt.bindBlob(14, photo);
        }
 
        String password = entity.getPassword();
        if (password != null) {
            stmt.bindString(15, password);
        }
 
        String opDate = entity.getOpDate();
        if (opDate != null) {
            stmt.bindString(16, opDate);
        }
 
        String opUserCode = entity.getOpUserCode();
        if (opUserCode != null) {
            stmt.bindString(17, opUserCode);
        }
 
        String opUserName = entity.getOpUserName();
        if (opUserName != null) {
            stmt.bindString(18, opUserName);
        }
        stmt.bindLong(19, entity.getIsCollected() ? 1L: 0L);
 
        String phone = entity.getPhone();
        if (phone != null) {
            stmt.bindString(20, phone);
        }
 
        String idCard = entity.getIdCard();
        if (idCard != null) {
            stmt.bindString(21, idCard);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.getLong(offset + 0);
    }    

    @Override
    public Escort readEntity(Cursor cursor, int offset) {
        Escort entity = new Escort( //
            cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // name
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // finger0
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // finger1
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // finger2
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // finger3
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // finger4
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // finger5
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // finger6
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // finger7
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10), // finger8
            cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11), // finger9
            cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12), // code
            cursor.isNull(offset + 13) ? null : cursor.getBlob(offset + 13), // photo
            cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14), // password
            cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15), // opDate
            cursor.isNull(offset + 16) ? null : cursor.getString(offset + 16), // opUserCode
            cursor.isNull(offset + 17) ? null : cursor.getString(offset + 17), // opUserName
            cursor.getShort(offset + 18) != 0, // isCollected
            cursor.isNull(offset + 19) ? null : cursor.getString(offset + 19), // phone
            cursor.isNull(offset + 20) ? null : cursor.getString(offset + 20) // idCard
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, Escort entity, int offset) {
        entity.setId(cursor.getLong(offset + 0));
        entity.setName(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setFinger0(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setFinger1(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setFinger2(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setFinger3(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setFinger4(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setFinger5(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setFinger6(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setFinger7(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setFinger8(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
        entity.setFinger9(cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11));
        entity.setCode(cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12));
        entity.setPhoto(cursor.isNull(offset + 13) ? null : cursor.getBlob(offset + 13));
        entity.setPassword(cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14));
        entity.setOpDate(cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15));
        entity.setOpUserCode(cursor.isNull(offset + 16) ? null : cursor.getString(offset + 16));
        entity.setOpUserName(cursor.isNull(offset + 17) ? null : cursor.getString(offset + 17));
        entity.setIsCollected(cursor.getShort(offset + 18) != 0);
        entity.setPhone(cursor.isNull(offset + 19) ? null : cursor.getString(offset + 19));
        entity.setIdCard(cursor.isNull(offset + 20) ? null : cursor.getString(offset + 20));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(Escort entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(Escort entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(Escort entity) {
        throw new UnsupportedOperationException("Unsupported for entities with a non-null key");
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
