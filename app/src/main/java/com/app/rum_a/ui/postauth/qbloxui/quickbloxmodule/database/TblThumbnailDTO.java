package com.app.rum_a.ui.postauth.qbloxui.quickbloxmodule.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Vivek on 19-03-2017.
 */

public class TblThumbnailDTO {

    public static final String KEY_ID = "id";
    public static final String KEY_FILEPATH = "filepath";
    public static final String TABLE_NAME = "thumbnails";
    //    public static final String
    static TblThumbnailDTO mTblInstallApps;
    TblThumbnailsDAO mTblInstallAppsDAO;

    private TblThumbnailDTO() {

    }

    public static TblThumbnailDTO getInstance() {
        if (mTblInstallApps == null) {
            mTblInstallApps = new TblThumbnailDTO();
        }
        return mTblInstallApps;

    }


    public void insertChatFileRecord(SQLiteDatabase dbObject, TblThumbnailsDAO dao) {
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_ID, dao.id);
            values.put(KEY_FILEPATH, dao.localFile);
            dbObject.insert(TABLE_NAME, null, values);
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
            values.put(KEY_FILEPATH, localurl);
            dbObject.update(TABLE_NAME, values, KEY_ID + " =?", new String[]{id});
        } catch (Exception e) {
            e.printStackTrace();
            dbObject.close();
        } finally {
            dbObject.close();
        }

    }

    public String getThumbnail(SQLiteDatabase sqLiteDatabase, String id) {
        String FETCH_QUERY = "SELECT * FROM " + TABLE_NAME + " where " + KEY_ID + " =? ";
        try {
            Cursor cursor = sqLiteDatabase.rawQuery(FETCH_QUERY, new String[]{id});
            if (cursor.moveToFirst()) {
                return cursor.getString(cursor.getColumnIndex(KEY_FILEPATH));
            }
        } catch (Exception e) {
            e.printStackTrace();
            sqLiteDatabase.close();
        } finally {
            sqLiteDatabase.close();
        }

        return null;
    }

    void deleteNow(SQLiteDatabase db) {
        try {
            db.delete(TABLE_NAME, null, null);
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        } finally {
            db.close();
        }
    }
}
