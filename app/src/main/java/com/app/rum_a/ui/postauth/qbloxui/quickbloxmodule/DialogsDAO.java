package com.app.rum_a.ui.postauth.qbloxui.quickbloxmodule;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.app.rum_a.ui.postauth.qbloxui.quickbloxmodule.database.DatabaseHelper;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.chat.model.QBDialogType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sunny on 5/20/2017.
 */

public class DialogsDAO {

    public static final String KEY_DIALOG_ID = "dialogid";
    public static final String KEY_LAST_MESSAGE = "lastmessage";
    public static final String KEY_LAST_MESSAGE_DATE_SENT = "lastmessageDateSent";
    public static final String KEY_LAST_MESSAGE_USER_ID = "lastmessageuserid";
    public static final String KEY_DIALOG_NAME = "dialogname";
    public static final String KEY_OCCUPANT_IDS = "occupantids";
    public static final String KEY_PHOTO = "photo";
    public static final String KEY_UNREAD_MESSAGE_COUNT = "unreadmessagecount";
    public static final String KEY_USER_ID = "userid";
    public static final String KEY_TYPE = "type";
    public static final String KEY_ID = "id";
    public static final String TABLE_NAME = "chat_dialogs";


    static DialogsDAO dialogsDAO;

    public static DialogsDAO getInstance() {

        if (dialogsDAO == null) {
            dialogsDAO = new DialogsDAO();
        }
        return dialogsDAO;
    }


    public void insertChatDialogs(Context context, List<QBChatDialog> dialogs) {

        if (dialogs == null || dialogs.size() == 0) {
            return;
        }
        for (int i = 0; i < dialogs.size(); i++) {
            QBChatDialog dialog = getDialogInfo(context, dialogs.get(i).getDialogId());
            if (dialog == null) {
                insertDailogs(context, dialogs.get(i));
            } else {
                updateDialog(context, dialogs.get(i));
            }
        }

    }

    public void removeChatDialogs(Context context, QBChatDialog dialogs) {

        if (dialogs == null) {
            return;
        }

        QBChatDialog dialog = getDialogInfo(context, dialogs.getDialogId());
        if (dialog == null) {
            removeDailogs(context, dialogs);
        }


    }

    public QBChatDialog getDialogInfo(Context context, String dialogid) {

        QBChatDialog dialog = null;
        SQLiteDatabase database = getDbInstance(context.getApplicationContext(), 0);
        List<QBChatDialog> chatDialogList = new ArrayList<>();
        String colums[] = {KEY_DIALOG_ID,
                KEY_USER_ID,
                KEY_PHOTO,
                KEY_OCCUPANT_IDS,
                KEY_DIALOG_NAME,
                KEY_LAST_MESSAGE,
                KEY_LAST_MESSAGE_DATE_SENT,
                KEY_LAST_MESSAGE_USER_ID,
                KEY_UNREAD_MESSAGE_COUNT,
                KEY_TYPE};
        String selections = KEY_DIALOG_ID + " =?";
        String[] selectionargs = {dialogid};
        try {
            Cursor cursor = database.query(TABLE_NAME, colums, selections, selectionargs, null, null, null);

            if (cursor.moveToNext()) {

                do {
                    dialog = new QBChatDialog();
                    dialog.setDialogId(cursor.getString(0));
                    dialog.setUserId(Integer.parseInt(cursor.getString(1)));
                    dialog.setPhoto(cursor.getString(2) + "");
                    String oIds = cursor.getString(3);
                    String[] arryids = oIds.split(",");
                    List<Integer> occupendIds = new ArrayList<>();
                    for (String id : arryids) {
                        occupendIds.add(Integer.parseInt(id));
                    }
                    dialog.setOccupantsIds(occupendIds);
                    dialog.setName(cursor.getString(4));
                    dialog.setLastMessage(cursor.getString(5));
                    dialog.setLastMessageDateSent(Integer.parseInt(cursor.getString(6)));
                    dialog.setUserId(Integer.parseInt(cursor.getString(7)));
                    dialog.setUnreadMessageCount(Integer.parseInt(cursor.getString(8)));
                    dialog.setType(QBDialogType.parseByCode(Integer.parseInt(cursor.getString(9))));
                    chatDialogList.add(dialog);
                } while (cursor.moveToNext());

            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            database.close();
        } finally {
            database.close();
        }
        return dialog;

    }


    public void insertDailogs(Context context, QBChatDialog dialog) {
        SQLiteDatabase database = getDbInstance(context, 1);
        ContentValues values = new ContentValues();
        values.put(KEY_DIALOG_ID, dialog.getDialogId());
        values.put(KEY_DIALOG_NAME, dialog.getName());
        if (!TextUtils.isEmpty(dialog.getLastMessage())) {
            values.put(KEY_LAST_MESSAGE, dialog.getLastMessage());
        }
        if (!TextUtils.isEmpty(dialog.getLastMessageUserId() + "") || !(dialog.getLastMessageDateSent() + "").equalsIgnoreCase("null")) {
            values.put(KEY_LAST_MESSAGE_USER_ID, dialog.getLastMessageUserId());
        }
//        values.put(KEY_LAST_MESSAGE, dialog.getLastMessage());
//        values.put(KEY_LAST_MESSAGE_USER_ID, dialog.getLastMessageUserId());
        List<Integer> occupents = dialog.getOccupants();
        values.put(KEY_OCCUPANT_IDS, TextUtils.join(",", occupents));
        try {
            values.put(KEY_PHOTO, dialog.getPhoto());
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        values.put(KEY_UNREAD_MESSAGE_COUNT, dialog.getUnreadMessageCount());
        values.put(KEY_USER_ID, dialog.getUserId() + "");
        values.put(KEY_TYPE, dialog.getType().getCode());
        values.put(KEY_LAST_MESSAGE_DATE_SENT, dialog.getLastMessageDateSent());
        try {
            database.insert(TABLE_NAME, null, values);
        } catch (Exception e) {
            database.close();

            e.printStackTrace();
        } finally {
            database.close();
        }
    }

    public void removeDailogs(Context context, QBChatDialog dialog) {
        SQLiteDatabase database = getDbInstance(context, 1);
        ContentValues values = new ContentValues();
        values.put(KEY_DIALOG_NAME, dialog.getName());
        if (!TextUtils.isEmpty(dialog.getLastMessage())) {
            values.put(KEY_LAST_MESSAGE, dialog.getLastMessage());
        }
        if (!TextUtils.isEmpty(dialog.getLastMessageUserId() + "") || !(dialog.getLastMessageDateSent() + "").equalsIgnoreCase("null")) {
            values.put(KEY_LAST_MESSAGE_USER_ID, dialog.getLastMessageUserId());
        }
        List<Integer> occupents = dialog.getOccupants();
        values.put(KEY_OCCUPANT_IDS, TextUtils.join(",", occupents));
        try {
            values.put(KEY_PHOTO, dialog.getPhoto());
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        values.put(KEY_UNREAD_MESSAGE_COUNT, dialog.getUnreadMessageCount());
        values.put(KEY_USER_ID, dialog.getUserId() + "");
        values.put(KEY_TYPE, dialog.getType().getCode());
        values.put(KEY_LAST_MESSAGE_DATE_SENT, dialog.getLastMessageDateSent());

        String whereclus = KEY_DIALOG_ID + " =?";
        String[] wherevalues = {dialog.getDialogId()};
        try {
            database.delete(TABLE_NAME, whereclus, wherevalues);
        } catch (Exception e) {
            e.printStackTrace();
            database.close();
        } finally {
            database.close();
        }
    }

    public void updateDialog(Context context, QBChatDialog dialog) {
        SQLiteDatabase database = getDbInstance(context, 1);
        ContentValues values = new ContentValues();
        values.put(KEY_DIALOG_NAME, dialog.getName());
        if (!TextUtils.isEmpty(dialog.getLastMessage())) {
            values.put(KEY_LAST_MESSAGE, dialog.getLastMessage());
        }
        if (!TextUtils.isEmpty(dialog.getLastMessageUserId() + "") || !(dialog.getLastMessageDateSent() + "").equalsIgnoreCase("null")) {
            values.put(KEY_LAST_MESSAGE_USER_ID, dialog.getLastMessageUserId());
        }
        List<Integer> occupents = dialog.getOccupants();
        values.put(KEY_OCCUPANT_IDS, TextUtils.join(",", occupents));
        try {
            values.put(KEY_PHOTO, dialog.getPhoto());
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        values.put(KEY_UNREAD_MESSAGE_COUNT, dialog.getUnreadMessageCount());
        values.put(KEY_USER_ID, dialog.getUserId() + "");
        values.put(KEY_TYPE, dialog.getType().getCode());
        values.put(KEY_LAST_MESSAGE_DATE_SENT, dialog.getLastMessageDateSent());

        String whereclus = KEY_DIALOG_ID + " =?";
        String[] wherevalues = {dialog.getDialogId()};
        try {
            database.update(TABLE_NAME, values, whereclus, wherevalues);
        } catch (Exception e) {
            e.printStackTrace();
            database.close();
        } finally {
            database.close();
        }

    }

    public QBChatDialog getDialogForUser(Context context, String qbid) {

        SQLiteDatabase database = getDbInstance(context.getApplicationContext(), 0);
        QBChatDialog dialog = null;
        String colums[] = {KEY_DIALOG_ID,
                KEY_USER_ID,
                KEY_PHOTO,
                KEY_OCCUPANT_IDS,
                KEY_DIALOG_NAME,
                KEY_LAST_MESSAGE,
                KEY_LAST_MESSAGE_DATE_SENT,
                KEY_LAST_MESSAGE_USER_ID,
                KEY_UNREAD_MESSAGE_COUNT,
                KEY_TYPE};

        String selection = KEY_TYPE + " =? AND " + KEY_OCCUPANT_IDS + " Like ?";
        String[] selectionargs = new String[]{"3", "%" + qbid + "%"};

        Cursor cursor = database.query(TABLE_NAME, colums, selection, selectionargs, null, null, null);
        try {

            if (cursor.moveToFirst()) {
                dialog = new QBChatDialog();
                dialog.setDialogId(cursor.getString(0));
                dialog.setUserId(Integer.parseInt(cursor.getString(1)));
                dialog.setPhoto(cursor.getString(2) + "");
                String oIds = cursor.getString(3);
                String[] arryids = oIds.split(",");
                List<Integer> occupendIds = new ArrayList<>();
                for (String id : arryids) {
                    occupendIds.add(Integer.parseInt(id));
                }
                dialog.setOccupantsIds(occupendIds);
                dialog.setName(cursor.getString(4));
                dialog.setLastMessage(cursor.getString(5));
                dialog.setLastMessageDateSent(Integer.parseInt(cursor.getString(6)));
                try {
                    dialog.setLastMessageUserId(Integer.parseInt(cursor.getString(7)));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                dialog.setUnreadMessageCount(Integer.parseInt(cursor.getString(8)));
                dialog.setType(QBDialogType.parseByCode(Integer.parseInt(cursor.getString(9))));

            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            database.close();
        } finally {
            database.close();
        }
        return dialog;

    }

    public List<QBChatDialog> getAllChatDialogs(Context context) {

        SQLiteDatabase database = getDbInstance(context.getApplicationContext(), 0);
        List<QBChatDialog> chatDialogList = new ArrayList<>();
        String colums[] = {KEY_DIALOG_ID,
                KEY_USER_ID,
                KEY_PHOTO,
                KEY_OCCUPANT_IDS,
                KEY_DIALOG_NAME,
                KEY_LAST_MESSAGE,
                KEY_LAST_MESSAGE_DATE_SENT,
                KEY_LAST_MESSAGE_USER_ID,
                KEY_UNREAD_MESSAGE_COUNT,
                KEY_TYPE};
        String orderby = KEY_LAST_MESSAGE_DATE_SENT + " desc";
        try {
            Cursor cursor = database.query(TABLE_NAME, colums, null, null, null, null, orderby);

            if (cursor.moveToFirst()) {

                do {

                    QBChatDialog dialog = new QBChatDialog();
                    dialog.setDialogId(cursor.getString(0));
                    dialog.setUserId(Integer.parseInt(cursor.getString(1)));
                    dialog.setPhoto(cursor.getString(2) + "");
                    String oIds = cursor.getString(3);
                    String[] arryids = oIds.split(",");
                    List<Integer> occupendIds = new ArrayList<>();
                    for (String id : arryids) {
                        occupendIds.add(Integer.parseInt(id));
                    }
                    dialog.setOccupantsIds(occupendIds);
                    dialog.setName(cursor.getString(4));
                    dialog.setLastMessage(cursor.getString(5));
                    dialog.setLastMessageDateSent(Integer.parseInt(cursor.getString(6)));
                    try {
                        dialog.setLastMessageUserId(Integer.parseInt(cursor.getString(7)));
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                    dialog.setUnreadMessageCount(Integer.parseInt(cursor.getString(8)));
                    dialog.setType(QBDialogType.parseByCode(Integer.parseInt(cursor.getString(9))));
                    chatDialogList.add(dialog);

                } while (cursor.moveToNext());

            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            database.close();
        } finally {
            database.close();
        }


        return chatDialogList;
    }


    /**
     * readable 0
     * writable 1
     *
     * @param context
     * @param readable
     * @return
     */
    public SQLiteDatabase getDbInstance(Context context, int readable) {
        return DatabaseHelper.getDatabaseInstanse(context.getApplicationContext()).getwritableDatabase(readable);
    }

    public void deleteTable(SQLiteDatabase database2) {
        try {
            database2.delete(TABLE_NAME, null, null);
        } catch (Exception e) {
            e.printStackTrace();
            database2.close();
        } finally {
            database2.close();
        }
    }
}
