package com.app.rum_a.ui.postauth.qbloxui.quickbloxmodule;


import android.Manifest;

public interface Consts {

    String SAMPLE_CONFIG_FILE_NAME = "sample_config.json";
    String QB_USER_PASSWORD = "qb_user_password";
    String EXTRA_IS_STARTED_FOR_CALL = "isRunForCall";
    String EXTRA_COMMAND_TO_SERVICE = "command_for_service";
    String EXTRA_QB_USER = "qb_user";
    String EXTRA_PENDING_INTENT = "pending_Intent";
    int COMMAND_LOGIN = 1;
    int COMMAND_NOT_FOUND = 0;
    int COMMAND_LOGOUT = 2;
    String EXTRA_LOGIN_RESULT = "login_result";
    String EXTRA_LOGIN_ERROR_MESSAGE = "login_error_message";
    int EXTRA_LOGIN_RESULT_CODE = 1002;
    String EXTRA_IS_INCOMING_CALL = "conversation_reason";
    //CALL ACTIVITY CLOSE REASONS
    int CALL_ACTIVITY_CLOSE_WIFI_DISABLED = 1001;
    String WIFI_DISABLED = "wifi_disabled";
    int MAX_OPPONENTS_COUNT = 6;
    String[] PERMISSIONS = {Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO};


    interface REQUEST_CODE {

        String EXTRA_DIALOG_ID = "dialogId";
    }

    interface ChatCode {
        String CONTENT_TYPE_PDF = "application/pdf";
        String CONTENT_TYPE_PHOTO = "image";
        String CONTENT_TYPE_VIDEO = "video";
        String CONTENT_SPREAD_SHEET = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
        String CONTENT_TYPE_DOC = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
        String CONTEXT_TYPE_PPT = "application/vnd.openxmlformats-officedocument.presentationml.presentation";
        String CONTENT_TYPE_MS_PPT = "application/vnd.ms-powerpoint";
    }

    public interface ChatValues {
        String EXTRA_GCM_MESSAGE = "message";
        String MESSAGE_SEEN = "message_seen@1q92";
        String CALL_DEAL = "2";
        String CALL_MISSED = "0";
        String CALL_RECEIVED = "1";
        String CALL_TYPE_AUDIO = "0";
        String CALL_TYPE_VIDEO = "1";
        String PROPERTY_SAVE_TO_HISTORY = "save_to_history";
        String SHARE_CONTACT_KEYWORD = "Add to contact";
        String SHARE_STICKER = "sticker";
        String PRIVACY_LIST_NAME = "KissaApp Privacy";
    }

    interface NOTIFICATION_CODE {

        String CUSTOM_MESSAGE = "message";
        String CUSTOM_MESSAGE_TYPE = "message_type";
        String NOTIFICATION_TYPE_MESSAGE = "Chat";
        String NOTIFICATION_TYPE_VIDEO = "Video Call";
        String NOTFIFICATION_TYPE_AUDIO = "Audio Call";
        String APN_SOUND_BLANK = "default";

        String EXTRA_GCM_MESSAGE = "message";
        String EXTRA_GCM_BODY = "messageBody";
    }
}