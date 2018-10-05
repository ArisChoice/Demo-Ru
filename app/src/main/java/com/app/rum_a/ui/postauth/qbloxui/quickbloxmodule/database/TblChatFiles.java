package com.app.rum_a.ui.postauth.qbloxui.quickbloxmodule.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


/**
 * Created by Vivek on 8/31/2016.
 */
public class TblChatFiles {

    public static final String TBL_INSTALL_APPS = "kuku_doc";
    public static final String KEY_ID = "id";
    public static final String KEY_dialog_id = "dialog_id";
    public static final String KEY_web_url = "web_url";
    public static final String KEY_local_uri = "local_uri";

    private TblChatFiles() {

    }

    static TblChatFiles mTblInstallApps;
    TblChatFilesDAO mTblInstallAppsDAO;

    public static TblChatFiles getInstance() {
        if (mTblInstallApps == null) {
            mTblInstallApps = new TblChatFiles();
        }
        return mTblInstallApps;

    }

    public void insertChatFileRecord(SQLiteDatabase dbObject, TblChatFilesDAO dao) {
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_ID, dao.id);
            values.put(KEY_dialog_id, dao.dialog_id);
            values.put(KEY_local_uri, dao.local_uri);
            values.put(KEY_web_url, dao.web_url);
            dbObject.insert(TBL_INSTALL_APPS, null, values);
        } catch (Exception e) {
            e.printStackTrace();
            dbObject.close();
        } finally {
            dbObject.close();
        }

    }

    public void updateLocalFilePath(SQLiteDatabase dbObject, String id, String localurl) {
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_local_uri, localurl);
            dbObject.update(TBL_INSTALL_APPS, values, KEY_ID + " =?", new String[]{id});
        } catch (Exception e) {
            e.printStackTrace();
            dbObject.close();
        } finally {
            dbObject.close();
        }

    }

    public TblChatFilesDAO getChatFileUri(SQLiteDatabase sqLiteDatabase, String id) {
        TblChatFilesDAO returnData;
        String FETCH_QUERY = "SELECT * FROM " + TBL_INSTALL_APPS + " where " + KEY_ID + " =? ";
        TblChatFilesDAO mTblInstallAppsDAO = null;
        try {
            mTblInstallAppsDAO = null;
            Cursor cursor = sqLiteDatabase.rawQuery(FETCH_QUERY, new String[]{id});
            if (cursor.moveToFirst()) {
                returnData = new TblChatFilesDAO();
                returnData.id = cursor.getString(cursor.getColumnIndex(TblChatFiles.KEY_ID));
                returnData.dialog_id = cursor.getString(cursor.getColumnIndex(TblChatFiles.KEY_dialog_id));
                returnData.local_uri = cursor.getString(cursor.getColumnIndex(TblChatFiles.KEY_local_uri));
                returnData.web_url = cursor.getString(cursor.getColumnIndex(TblChatFiles.KEY_web_url));
                return returnData;
            }
        } catch (Exception e) {
            e.printStackTrace();
            sqLiteDatabase.close();
        } finally {
            sqLiteDatabase.close();
        }

        return mTblInstallAppsDAO;
    }

    void deleteNow(SQLiteDatabase db) {
        try {
            db.delete(TBL_INSTALL_APPS, null, null);
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        } finally {
            db.close();
        }
    }


}
