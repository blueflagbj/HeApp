package com.heclient.heapp.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "RESULT_INFO".
*/
public class ResultInfoDao extends AbstractDao<ResultInfo, Long> {

    public static final String TABLENAME = "RESULT_INFO";

    /**
     * Properties of entity ResultInfo.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property _id = new Property(0, Long.class, "_id", true, "_id");
        public final static Property X_RESULTINFO = new Property(1, String.class, "X_RESULTINFO", false, "X__RESULTINFO");
        public final static Property ReturnCode = new Property(2, String.class, "returnCode", false, "RETURN_CODE");
        public final static Property User_name = new Property(3, String.class, "user_name", false, "USER_NAME");
        public final static Property ReturnMsg = new Property(4, String.class, "returnMsg", false, "RETURN_MSG");
        public final static Property User_full_name = new Property(5, String.class, "user_full_name", false, "USER_FULL_NAME");
        public final static Property Phone = new Property(6, String.class, "phone", false, "PHONE");
        public final static Property IsVip = new Property(7, String.class, "isVip", false, "IS_VIP");
        public final static Property PreSave = new Property(8, String.class, "preSave", false, "PRE_SAVE");
        public final static Property CurrentOfferId = new Property(9, String.class, "currentOfferId", false, "CURRENT_OFFER_ID");
        public final static Property State = new Property(10, String.class, "state", false, "STATE");
        public final static Property StarLevelCreditLimit = new Property(11, String.class, "starLevelCreditLimit", false, "STAR_LEVEL_CREDIT_LIMIT");
        public final static Property BrandName = new Property(12, String.class, "brandName", false, "BRAND_NAME");
        public final static Property BrandId = new Property(13, String.class, "brandId", false, "BRAND_ID");
        public final static Property Username = new Property(14, String.class, "username", false, "USERNAME");
        public final static Property BalOrgName = new Property(15, String.class, "balOrgName", false, "BAL_ORG_NAME");
        public final static Property BrandCode = new Property(16, String.class, "brandCode", false, "BRAND_CODE");
        public final static Property HistoryCost = new Property(17, String.class, "historyCost", false, "HISTORY_COST");
        public final static Property ValidateLevel = new Property(18, String.class, "validateLevel", false, "VALIDATE_LEVEL");
        public final static Property CreateDate = new Property(19, String.class, "createDate", false, "CREATE_DATE");
        public final static Property OfferName = new Property(20, String.class, "offerName", false, "OFFER_NAME");
        public final static Property RegionId = new Property(21, String.class, "regionId", false, "REGION_ID");
        public final static Property NoticeFlag = new Property(22, String.class, "noticeFlag", false, "NOTICE_FLAG");
        public final static Property Banlance = new Property(23, String.class, "banlance", false, "BANLANCE");
        public final static Property BalOrgId = new Property(24, String.class, "balOrgId", false, "BAL_ORG_ID");
        public final static Property RegionName = new Property(25, String.class, "regionName", false, "REGION_NAME");
        public final static Property Cost = new Property(26, String.class, "cost", false, "COST");
        public final static Property Is4G = new Property(27, String.class, "is4G", false, "IS4_G");
        public final static Property StarLevel = new Property(28, String.class, "starLevel", false, "STAR_LEVEL");
        public final static Property Point = new Property(29, String.class, "point", false, "POINT");
        public final static Property CurrentOweFee = new Property(30, String.class, "currentOweFee", false, "CURRENT_OWE_FEE");
        public final static Property StarLevelValue = new Property(31, String.class, "starLevelValue", false, "STAR_LEVEL_VALUE");
        public final static Property Age = new Property(32, String.class, "age", false, "AGE");
        public final static Property BroadbandSpeed = new Property(33, String.class, "broadbandSpeed", false, "BROADBAND_SPEED");
        public final static Property AmountCredit = new Property(34, String.class, "amountCredit", false, "AMOUNT_CREDIT");
        public final static Property X_RESULTCODE = new Property(35, String.class, "X_RESULTCODE", false, "X__RESULTCODE");
        public final static Property X_RECORDNUM = new Property(36, String.class, "X_RECORDNUM", false, "X__RECORDNUM");
    }


    public ResultInfoDao(DaoConfig config) {
        super(config);
    }
    
    public ResultInfoDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"RESULT_INFO\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: _id
                "\"X__RESULTINFO\" TEXT," + // 1: X_RESULTINFO
                "\"RETURN_CODE\" TEXT," + // 2: returnCode
                "\"USER_NAME\" TEXT," + // 3: user_name
                "\"RETURN_MSG\" TEXT," + // 4: returnMsg
                "\"USER_FULL_NAME\" TEXT," + // 5: user_full_name
                "\"PHONE\" TEXT," + // 6: phone
                "\"IS_VIP\" TEXT," + // 7: isVip
                "\"PRE_SAVE\" TEXT," + // 8: preSave
                "\"CURRENT_OFFER_ID\" TEXT," + // 9: currentOfferId
                "\"STATE\" TEXT," + // 10: state
                "\"STAR_LEVEL_CREDIT_LIMIT\" TEXT," + // 11: starLevelCreditLimit
                "\"BRAND_NAME\" TEXT," + // 12: brandName
                "\"BRAND_ID\" TEXT," + // 13: brandId
                "\"USERNAME\" TEXT," + // 14: username
                "\"BAL_ORG_NAME\" TEXT," + // 15: balOrgName
                "\"BRAND_CODE\" TEXT," + // 16: brandCode
                "\"HISTORY_COST\" TEXT," + // 17: historyCost
                "\"VALIDATE_LEVEL\" TEXT," + // 18: validateLevel
                "\"CREATE_DATE\" TEXT," + // 19: createDate
                "\"OFFER_NAME\" TEXT," + // 20: offerName
                "\"REGION_ID\" TEXT," + // 21: regionId
                "\"NOTICE_FLAG\" TEXT," + // 22: noticeFlag
                "\"BANLANCE\" TEXT," + // 23: banlance
                "\"BAL_ORG_ID\" TEXT," + // 24: balOrgId
                "\"REGION_NAME\" TEXT," + // 25: regionName
                "\"COST\" TEXT," + // 26: cost
                "\"IS4_G\" TEXT," + // 27: is4G
                "\"STAR_LEVEL\" TEXT," + // 28: starLevel
                "\"POINT\" TEXT," + // 29: point
                "\"CURRENT_OWE_FEE\" TEXT," + // 30: currentOweFee
                "\"STAR_LEVEL_VALUE\" TEXT," + // 31: starLevelValue
                "\"AGE\" TEXT," + // 32: age
                "\"BROADBAND_SPEED\" TEXT," + // 33: broadbandSpeed
                "\"AMOUNT_CREDIT\" TEXT," + // 34: amountCredit
                "\"X__RESULTCODE\" TEXT," + // 35: X_RESULTCODE
                "\"X__RECORDNUM\" TEXT);"); // 36: X_RECORDNUM
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"RESULT_INFO\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, ResultInfo entity) {
        stmt.clearBindings();
 
        Long _id = entity.get_id();
        if (_id != null) {
            stmt.bindLong(1, _id);
        }
 
        String X_RESULTINFO = entity.getX_RESULTINFO();
        if (X_RESULTINFO != null) {
            stmt.bindString(2, X_RESULTINFO);
        }
 
        String returnCode = entity.getReturnCode();
        if (returnCode != null) {
            stmt.bindString(3, returnCode);
        }
 
        String user_name = entity.getUser_name();
        if (user_name != null) {
            stmt.bindString(4, user_name);
        }
 
        String returnMsg = entity.getReturnMsg();
        if (returnMsg != null) {
            stmt.bindString(5, returnMsg);
        }
 
        String user_full_name = entity.getUser_full_name();
        if (user_full_name != null) {
            stmt.bindString(6, user_full_name);
        }
 
        String phone = entity.getPhone();
        if (phone != null) {
            stmt.bindString(7, phone);
        }
 
        String isVip = entity.getIsVip();
        if (isVip != null) {
            stmt.bindString(8, isVip);
        }
 
        String preSave = entity.getPreSave();
        if (preSave != null) {
            stmt.bindString(9, preSave);
        }
 
        String currentOfferId = entity.getCurrentOfferId();
        if (currentOfferId != null) {
            stmt.bindString(10, currentOfferId);
        }
 
        String state = entity.getState();
        if (state != null) {
            stmt.bindString(11, state);
        }
 
        String starLevelCreditLimit = entity.getStarLevelCreditLimit();
        if (starLevelCreditLimit != null) {
            stmt.bindString(12, starLevelCreditLimit);
        }
 
        String brandName = entity.getBrandName();
        if (brandName != null) {
            stmt.bindString(13, brandName);
        }
 
        String brandId = entity.getBrandId();
        if (brandId != null) {
            stmt.bindString(14, brandId);
        }
 
        String username = entity.getUsername();
        if (username != null) {
            stmt.bindString(15, username);
        }
 
        String balOrgName = entity.getBalOrgName();
        if (balOrgName != null) {
            stmt.bindString(16, balOrgName);
        }
 
        String brandCode = entity.getBrandCode();
        if (brandCode != null) {
            stmt.bindString(17, brandCode);
        }
 
        String historyCost = entity.getHistoryCost();
        if (historyCost != null) {
            stmt.bindString(18, historyCost);
        }
 
        String validateLevel = entity.getValidateLevel();
        if (validateLevel != null) {
            stmt.bindString(19, validateLevel);
        }
 
        String createDate = entity.getCreateDate();
        if (createDate != null) {
            stmt.bindString(20, createDate);
        }
 
        String offerName = entity.getOfferName();
        if (offerName != null) {
            stmt.bindString(21, offerName);
        }
 
        String regionId = entity.getRegionId();
        if (regionId != null) {
            stmt.bindString(22, regionId);
        }
 
        String noticeFlag = entity.getNoticeFlag();
        if (noticeFlag != null) {
            stmt.bindString(23, noticeFlag);
        }
 
        String banlance = entity.getBanlance();
        if (banlance != null) {
            stmt.bindString(24, banlance);
        }
 
        String balOrgId = entity.getBalOrgId();
        if (balOrgId != null) {
            stmt.bindString(25, balOrgId);
        }
 
        String regionName = entity.getRegionName();
        if (regionName != null) {
            stmt.bindString(26, regionName);
        }
 
        String cost = entity.getCost();
        if (cost != null) {
            stmt.bindString(27, cost);
        }
 
        String is4G = entity.getIs4G();
        if (is4G != null) {
            stmt.bindString(28, is4G);
        }
 
        String starLevel = entity.getStarLevel();
        if (starLevel != null) {
            stmt.bindString(29, starLevel);
        }
 
        String point = entity.getPoint();
        if (point != null) {
            stmt.bindString(30, point);
        }
 
        String currentOweFee = entity.getCurrentOweFee();
        if (currentOweFee != null) {
            stmt.bindString(31, currentOweFee);
        }
 
        String starLevelValue = entity.getStarLevelValue();
        if (starLevelValue != null) {
            stmt.bindString(32, starLevelValue);
        }
 
        String age = entity.getAge();
        if (age != null) {
            stmt.bindString(33, age);
        }
 
        String broadbandSpeed = entity.getBroadbandSpeed();
        if (broadbandSpeed != null) {
            stmt.bindString(34, broadbandSpeed);
        }
 
        String amountCredit = entity.getAmountCredit();
        if (amountCredit != null) {
            stmt.bindString(35, amountCredit);
        }
 
        String X_RESULTCODE = entity.getX_RESULTCODE();
        if (X_RESULTCODE != null) {
            stmt.bindString(36, X_RESULTCODE);
        }
 
        String X_RECORDNUM = entity.getX_RECORDNUM();
        if (X_RECORDNUM != null) {
            stmt.bindString(37, X_RECORDNUM);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, ResultInfo entity) {
        stmt.clearBindings();
 
        Long _id = entity.get_id();
        if (_id != null) {
            stmt.bindLong(1, _id);
        }
 
        String X_RESULTINFO = entity.getX_RESULTINFO();
        if (X_RESULTINFO != null) {
            stmt.bindString(2, X_RESULTINFO);
        }
 
        String returnCode = entity.getReturnCode();
        if (returnCode != null) {
            stmt.bindString(3, returnCode);
        }
 
        String user_name = entity.getUser_name();
        if (user_name != null) {
            stmt.bindString(4, user_name);
        }
 
        String returnMsg = entity.getReturnMsg();
        if (returnMsg != null) {
            stmt.bindString(5, returnMsg);
        }
 
        String user_full_name = entity.getUser_full_name();
        if (user_full_name != null) {
            stmt.bindString(6, user_full_name);
        }
 
        String phone = entity.getPhone();
        if (phone != null) {
            stmt.bindString(7, phone);
        }
 
        String isVip = entity.getIsVip();
        if (isVip != null) {
            stmt.bindString(8, isVip);
        }
 
        String preSave = entity.getPreSave();
        if (preSave != null) {
            stmt.bindString(9, preSave);
        }
 
        String currentOfferId = entity.getCurrentOfferId();
        if (currentOfferId != null) {
            stmt.bindString(10, currentOfferId);
        }
 
        String state = entity.getState();
        if (state != null) {
            stmt.bindString(11, state);
        }
 
        String starLevelCreditLimit = entity.getStarLevelCreditLimit();
        if (starLevelCreditLimit != null) {
            stmt.bindString(12, starLevelCreditLimit);
        }
 
        String brandName = entity.getBrandName();
        if (brandName != null) {
            stmt.bindString(13, brandName);
        }
 
        String brandId = entity.getBrandId();
        if (brandId != null) {
            stmt.bindString(14, brandId);
        }
 
        String username = entity.getUsername();
        if (username != null) {
            stmt.bindString(15, username);
        }
 
        String balOrgName = entity.getBalOrgName();
        if (balOrgName != null) {
            stmt.bindString(16, balOrgName);
        }
 
        String brandCode = entity.getBrandCode();
        if (brandCode != null) {
            stmt.bindString(17, brandCode);
        }
 
        String historyCost = entity.getHistoryCost();
        if (historyCost != null) {
            stmt.bindString(18, historyCost);
        }
 
        String validateLevel = entity.getValidateLevel();
        if (validateLevel != null) {
            stmt.bindString(19, validateLevel);
        }
 
        String createDate = entity.getCreateDate();
        if (createDate != null) {
            stmt.bindString(20, createDate);
        }
 
        String offerName = entity.getOfferName();
        if (offerName != null) {
            stmt.bindString(21, offerName);
        }
 
        String regionId = entity.getRegionId();
        if (regionId != null) {
            stmt.bindString(22, regionId);
        }
 
        String noticeFlag = entity.getNoticeFlag();
        if (noticeFlag != null) {
            stmt.bindString(23, noticeFlag);
        }
 
        String banlance = entity.getBanlance();
        if (banlance != null) {
            stmt.bindString(24, banlance);
        }
 
        String balOrgId = entity.getBalOrgId();
        if (balOrgId != null) {
            stmt.bindString(25, balOrgId);
        }
 
        String regionName = entity.getRegionName();
        if (regionName != null) {
            stmt.bindString(26, regionName);
        }
 
        String cost = entity.getCost();
        if (cost != null) {
            stmt.bindString(27, cost);
        }
 
        String is4G = entity.getIs4G();
        if (is4G != null) {
            stmt.bindString(28, is4G);
        }
 
        String starLevel = entity.getStarLevel();
        if (starLevel != null) {
            stmt.bindString(29, starLevel);
        }
 
        String point = entity.getPoint();
        if (point != null) {
            stmt.bindString(30, point);
        }
 
        String currentOweFee = entity.getCurrentOweFee();
        if (currentOweFee != null) {
            stmt.bindString(31, currentOweFee);
        }
 
        String starLevelValue = entity.getStarLevelValue();
        if (starLevelValue != null) {
            stmt.bindString(32, starLevelValue);
        }
 
        String age = entity.getAge();
        if (age != null) {
            stmt.bindString(33, age);
        }
 
        String broadbandSpeed = entity.getBroadbandSpeed();
        if (broadbandSpeed != null) {
            stmt.bindString(34, broadbandSpeed);
        }
 
        String amountCredit = entity.getAmountCredit();
        if (amountCredit != null) {
            stmt.bindString(35, amountCredit);
        }
 
        String X_RESULTCODE = entity.getX_RESULTCODE();
        if (X_RESULTCODE != null) {
            stmt.bindString(36, X_RESULTCODE);
        }
 
        String X_RECORDNUM = entity.getX_RECORDNUM();
        if (X_RECORDNUM != null) {
            stmt.bindString(37, X_RECORDNUM);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public ResultInfo readEntity(Cursor cursor, int offset) {
        ResultInfo entity = new ResultInfo( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // _id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // X_RESULTINFO
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // returnCode
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // user_name
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // returnMsg
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // user_full_name
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // phone
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // isVip
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // preSave
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // currentOfferId
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10), // state
            cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11), // starLevelCreditLimit
            cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12), // brandName
            cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13), // brandId
            cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14), // username
            cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15), // balOrgName
            cursor.isNull(offset + 16) ? null : cursor.getString(offset + 16), // brandCode
            cursor.isNull(offset + 17) ? null : cursor.getString(offset + 17), // historyCost
            cursor.isNull(offset + 18) ? null : cursor.getString(offset + 18), // validateLevel
            cursor.isNull(offset + 19) ? null : cursor.getString(offset + 19), // createDate
            cursor.isNull(offset + 20) ? null : cursor.getString(offset + 20), // offerName
            cursor.isNull(offset + 21) ? null : cursor.getString(offset + 21), // regionId
            cursor.isNull(offset + 22) ? null : cursor.getString(offset + 22), // noticeFlag
            cursor.isNull(offset + 23) ? null : cursor.getString(offset + 23), // banlance
            cursor.isNull(offset + 24) ? null : cursor.getString(offset + 24), // balOrgId
            cursor.isNull(offset + 25) ? null : cursor.getString(offset + 25), // regionName
            cursor.isNull(offset + 26) ? null : cursor.getString(offset + 26), // cost
            cursor.isNull(offset + 27) ? null : cursor.getString(offset + 27), // is4G
            cursor.isNull(offset + 28) ? null : cursor.getString(offset + 28), // starLevel
            cursor.isNull(offset + 29) ? null : cursor.getString(offset + 29), // point
            cursor.isNull(offset + 30) ? null : cursor.getString(offset + 30), // currentOweFee
            cursor.isNull(offset + 31) ? null : cursor.getString(offset + 31), // starLevelValue
            cursor.isNull(offset + 32) ? null : cursor.getString(offset + 32), // age
            cursor.isNull(offset + 33) ? null : cursor.getString(offset + 33), // broadbandSpeed
            cursor.isNull(offset + 34) ? null : cursor.getString(offset + 34), // amountCredit
            cursor.isNull(offset + 35) ? null : cursor.getString(offset + 35), // X_RESULTCODE
            cursor.isNull(offset + 36) ? null : cursor.getString(offset + 36) // X_RECORDNUM
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, ResultInfo entity, int offset) {
        entity.set_id(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setX_RESULTINFO(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setReturnCode(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setUser_name(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setReturnMsg(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setUser_full_name(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setPhone(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setIsVip(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setPreSave(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setCurrentOfferId(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setState(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
        entity.setStarLevelCreditLimit(cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11));
        entity.setBrandName(cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12));
        entity.setBrandId(cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13));
        entity.setUsername(cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14));
        entity.setBalOrgName(cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15));
        entity.setBrandCode(cursor.isNull(offset + 16) ? null : cursor.getString(offset + 16));
        entity.setHistoryCost(cursor.isNull(offset + 17) ? null : cursor.getString(offset + 17));
        entity.setValidateLevel(cursor.isNull(offset + 18) ? null : cursor.getString(offset + 18));
        entity.setCreateDate(cursor.isNull(offset + 19) ? null : cursor.getString(offset + 19));
        entity.setOfferName(cursor.isNull(offset + 20) ? null : cursor.getString(offset + 20));
        entity.setRegionId(cursor.isNull(offset + 21) ? null : cursor.getString(offset + 21));
        entity.setNoticeFlag(cursor.isNull(offset + 22) ? null : cursor.getString(offset + 22));
        entity.setBanlance(cursor.isNull(offset + 23) ? null : cursor.getString(offset + 23));
        entity.setBalOrgId(cursor.isNull(offset + 24) ? null : cursor.getString(offset + 24));
        entity.setRegionName(cursor.isNull(offset + 25) ? null : cursor.getString(offset + 25));
        entity.setCost(cursor.isNull(offset + 26) ? null : cursor.getString(offset + 26));
        entity.setIs4G(cursor.isNull(offset + 27) ? null : cursor.getString(offset + 27));
        entity.setStarLevel(cursor.isNull(offset + 28) ? null : cursor.getString(offset + 28));
        entity.setPoint(cursor.isNull(offset + 29) ? null : cursor.getString(offset + 29));
        entity.setCurrentOweFee(cursor.isNull(offset + 30) ? null : cursor.getString(offset + 30));
        entity.setStarLevelValue(cursor.isNull(offset + 31) ? null : cursor.getString(offset + 31));
        entity.setAge(cursor.isNull(offset + 32) ? null : cursor.getString(offset + 32));
        entity.setBroadbandSpeed(cursor.isNull(offset + 33) ? null : cursor.getString(offset + 33));
        entity.setAmountCredit(cursor.isNull(offset + 34) ? null : cursor.getString(offset + 34));
        entity.setX_RESULTCODE(cursor.isNull(offset + 35) ? null : cursor.getString(offset + 35));
        entity.setX_RECORDNUM(cursor.isNull(offset + 36) ? null : cursor.getString(offset + 36));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(ResultInfo entity, long rowId) {
        entity.set_id(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(ResultInfo entity) {
        if(entity != null) {
            return entity.get_id();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(ResultInfo entity) {
        return entity.get_id() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}