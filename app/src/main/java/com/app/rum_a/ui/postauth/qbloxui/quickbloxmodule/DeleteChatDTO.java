package com.app.rum_a.ui.postauth.qbloxui.quickbloxmodule;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.HashSet;
import java.util.Set;

import static com.app.rum_a.ui.postauth.qbloxui.quickbloxmodule.database.DatabaseHelper.KEY_MESSAGE_ID;
import static com.app.rum_a.ui.postauth.qbloxui.quickbloxmodule.database.DatabaseHelper.TABLE_MESSAGE_DELETE;


/**
 * Created by Sunny on 3/29/2017.
 */

public class DeleteChatDTO {

    static DeleteChatDTO mTblInstallApps;
    DeleteChatDAO mTblInstallAppsDAO;

    private DeleteChatDTO() {

    }

    public static DeleteChatDTO getInstance() {
        if (mTblInstallApps == null) {
            mTblInstallApps = new DeleteChatDTO();
        }
        return mTblInstallApps;

    }


    public void insertChatID(SQLiteDatabase dbObject, DeleteChatDAO dao) {
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_MESSAGE_ID, dao.message_id);
            dbObject.insert(TABLE_MESSAGE_DELETE, null, values);
        } catch (Exception e) {
            e.printStackTrace();
            dbObject.close();
        } finally {
            dbObject.close();
        }
    }


    public Set<String> getIdsWithSets(SQLiteDatabase database) {
        Set<String> set = new HashSet<String>();
        try {
            String FETCH_QUERY = "SELECT * FROM " + TABLE_MESSAGE_DELETE;
            Cursor cursor = database.rawQuery(FETCH_QUERY, null);
            if (cursor.moveToFirst()){
                do{
                    set.add(new String(cursor.getString(cursor.getColumnIndex(KEY_MESSAGE_ID))));
                    // do what ever you want here
                }while(cursor.moveToNext());
            }
            database.close();
            return set;

        } catch (Exception e) {
            database.close();
            e.printStackTrace();
        } finally {
            database.close();
        }

        return set;


    }


    public void deleteChatIds(SQLiteDatabase db) {
        try {
            db.delete(TABLE_MESSAGE_DELETE, null, null);
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        } finally {
            db.close();
        }
    }
}
