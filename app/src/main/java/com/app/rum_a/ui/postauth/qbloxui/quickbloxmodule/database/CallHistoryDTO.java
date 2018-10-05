package com.app.rum_a.ui.postauth.qbloxui.quickbloxmodule.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vivek on 08-05-2017.
 */

public class CallHistoryDTO {
    public static final String KEY_ID = "id";
    public static final String KEY_OPPONENT_NAME = "opponent_name";
    public static final String KEY_OPPONENT_ID = "opponent_id";
    public static final String KEY_OPPONENT_IMAGE = "opponent_image";
    public static final String KEY_CALL_TIME = "call_time";

    /**
     * can we audio-0,video-1
     */
    public static final String KEY_CALL_TYPE = "call_type";


    /**
     * either missed call-0,recieved-1,dialed-2
     */
    public static final String KEY_CALL_MODE = "call_mode";

    public static final String TABLE_NAME = "table_call_records";

    static CallHistoryDTO callHistoryDTO;

    private CallHistoryDTO() {

    }

    public static CallHistoryDTO getInstance() {
        if (callHistoryDTO == null) {
            callHistoryDTO = new CallHistoryDTO();
        }
        return callHistoryDTO;

    }



    public void insertCallRecord(SQLiteDatabase dbObject, CallHistoryDAO dao) {
        try {
            ContentValues values = new ContentValues();
            try {
                values.put(KEY_OPPONENT_NAME, dao.oppoName);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                values.put(KEY_OPPONENT_ID, dao.oppoID);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                values.put(KEY_OPPONENT_IMAGE, dao.oppoImage);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                values.put(KEY_CALL_TIME, dao.callTime);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                values.put(KEY_CALL_TYPE, dao.callType);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                values.put(KEY_CALL_MODE, dao.callMode);
            } catch (Exception e) {
                e.printStackTrace();
            }
            dbObject.insert(TABLE_NAME, null, values);
        } catch (Exception e) {
            e.printStackTrace();
            dbObject.close();
        } finally {
            dbObject.close();
        }

    }


    public List<CallHistoryDAO> getCallRecords(SQLiteDatabase dbObject) {

        ArrayList<CallHistoryDAO> list = new ArrayList<>();
        try {
            String[] columns = new String[]{KEY_OPPONENT_NAME,
                    KEY_OPPONENT_ID,
                    KEY_OPPONENT_IMAGE,
                    KEY_CALL_TIME,
                    KEY_CALL_TYPE,
                    KEY_CALL_MODE,
            KEY_ID};
            String orderby = KEY_ID+ " desc";
            Cursor cursor = dbObject.query(TABLE_NAME, columns, null, null, null, null, orderby);
            if (cursor.moveToFirst()) {
                do {
                    CallHistoryDAO dao = new CallHistoryDAO();
                    dao.oppoName = cursor.getString(0);
                    dao.oppoID=cursor.getString(1);
                    dao.oppoImage = cursor.getString(2);
                    dao.callTime = cursor.getString(3);
                    dao.callType = cursor.getString(4);
                    dao.callMode = cursor.getString(5);
                    list.add(dao);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            dbObject.close();
        } finally {
            dbObject.close();
        }
        return list;
    }


    public void deleteCallTBl(SQLiteDatabase database) {

        try {
            database.delete(TABLE_NAME, null, null);
        } catch (Exception e) {
            e.printStackTrace();
            database.close();
        } finally {
            database.close();
        }

    }

}
