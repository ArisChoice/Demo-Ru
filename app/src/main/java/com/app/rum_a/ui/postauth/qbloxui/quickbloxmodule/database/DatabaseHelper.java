package com.app.rum_a.ui.postauth.qbloxui.quickbloxmodule.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.app.rum_a.ui.postauth.qbloxui.quickbloxmodule.DialogsDAO;


/**
 * Created by Vivek on 8/31/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {


    public static final int WRITABLE_DATABASE = 1;
    public static final int READABLE_DATABASE = 0;
    public static final String DATABASE_NAME = "earn_bucket";
    public static final int DATABASE_VERSION = 1;
    public static final String CREATE_TABLE_TO_THUMBNAIL = "CREATE TABLE "
            + TblThumbnailDTO.TABLE_NAME + "(" + TblThumbnailDTO.KEY_ID + " text,"
            + TblThumbnailDTO.KEY_FILEPATH + " TEXT)";
    public static final String KEY_MESSAGE_ID = "message_id";
    public static final String TABLE_MESSAGE_DELETE = "message_delete_table";

    /**
     * table to contacts list
     */
    public static final String TABLE_CONTACTS = "contacts";
    public static final String KEY_FULLNAME = "fullName";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_LOGIN = "login";
    public static final String KEY_PHONE = "phone";
    public static final String KEY_BLOBID = "blobId";
    public static final String KEY_QBID = "qbid";

    /**
     * this key use to know weather qbuser is also in contact list(is_contact=1)
     */
    public static final String KEY_IS_CONTACT = "is_contact";

    private static final String CREATE_TABLE_CONTACT = "CREATE TABLE "
            + TABLE_CONTACTS
            + "("
            + TblChatFiles.KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_FULLNAME + " text,"
            + KEY_EMAIL + " text,"
            + KEY_LOGIN + " text,"
            + KEY_PHONE + " text,"
            + KEY_BLOBID + " text,"
            + KEY_QBID + " text unique,"
            + KEY_IS_CONTACT + " text)";

    private static final String CREATE_CALL_TABLE = "CREATE TABLE "
            + CallHistoryDTO.TABLE_NAME
            + " ("
            + CallHistoryDTO.KEY_ID + " INTEGER PRIMARY KEY,"
            + CallHistoryDTO.KEY_OPPONENT_ID + " TEXT,"
            + CallHistoryDTO.KEY_OPPONENT_NAME + " TEXT,"
            + CallHistoryDTO.KEY_OPPONENT_IMAGE + " TEXT,"
            + CallHistoryDTO.KEY_CALL_TYPE + " TEXT,"
            + CallHistoryDTO.KEY_CALL_TIME + " INTEGER,"
            + CallHistoryDTO.KEY_CALL_MODE + " TEXT)";

    private static final String CREATE_CHAT_DIALOG_TABLE = "CREATE TABLE "
            + DialogsDAO.TABLE_NAME
            + " ("
            + DialogsDAO.KEY_ID + " INTEGER PRIMARY KEY,"
            + DialogsDAO.KEY_DIALOG_ID + " TEXT UNIQUE,"
            + DialogsDAO.KEY_DIALOG_NAME + " TEXT,"
            + DialogsDAO.KEY_LAST_MESSAGE + " TEXT,"
            + DialogsDAO.KEY_LAST_MESSAGE_DATE_SENT + " INTEGER,"
            + DialogsDAO.KEY_OCCUPANT_IDS + " TEXT,"
            + DialogsDAO.KEY_PHOTO + " TEXT,"
            + DialogsDAO.KEY_UNREAD_MESSAGE_COUNT + " TEXT,"
            + DialogsDAO.KEY_USER_ID + " TEXT,"
            + DialogsDAO.KEY_LAST_MESSAGE_USER_ID + " TEXT,"
            + DialogsDAO.KEY_TYPE + " TEXT)";


    private static final String CREATE_TABLE_INSTALL_APPS = "CREATE TABLE "
            + TblChatFiles.TBL_INSTALL_APPS + "(" + TblChatFiles.KEY_ID + " TEXT UNIQUE,"
            + TblChatFiles.KEY_dialog_id + " TEXT,"
            + TblChatFiles.KEY_web_url + " TEXT,"
            + TblChatFiles.KEY_local_uri + " TEXT)";
    private static final String CREATE_TABLE_TO_DELETE_CHAT = "CREATE TABLE " + TABLE_MESSAGE_DELETE
            + "(" + TblChatFiles.KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_MESSAGE_ID + " TEXT)";
    private static DatabaseHelper helper;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    public static DatabaseHelper getDatabaseInstanse(Context context) {

        if (helper == null) {
            helper = new DatabaseHelper(context);
        }
        return helper;

    }

    public SQLiteDatabase getwritableDatabase(int database) {
        SQLiteDatabase sqLiteDatabase = database == READABLE_DATABASE ?
                (helper.getReadableDatabase()) : (helper.getWritableDatabase());
        return sqLiteDatabase;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_INSTALL_APPS);
        db.execSQL(CREATE_TABLE_TO_THUMBNAIL);
        db.execSQL(CREATE_TABLE_TO_DELETE_CHAT);
        db.execSQL(CREATE_TABLE_CONTACT);
        db.execSQL(CREATE_CALL_TABLE);
        db.execSQL(CREATE_CHAT_DIALOG_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_INSTALL_APPS);
    }
}
