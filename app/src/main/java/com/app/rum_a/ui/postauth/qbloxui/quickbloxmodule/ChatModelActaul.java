package com.app.rum_a.ui.postauth.qbloxui.quickbloxmodule;

/**
 * Created by Vivek on 03-06-2017.
 */

public class ChatModelActaul {

    String message;
    String ios_sound;
    String ios_badge;
    ChatModel.Gcm gcm;
    public String type;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getIos_sound() {
        return ios_sound;
    }

    public void setIos_sound(String ios_sound) {
        this.ios_sound = ios_sound;
    }

    public String getIos_badge() {
        return ios_badge;
    }

    public void setIos_badge(String ios_badge) {
        this.ios_badge = ios_badge;
    }

    public ChatModel.Gcm getGcm() {
        return gcm;
    }

    public void setGcm(ChatModel.Gcm gcm) {
        this.gcm = gcm;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public static class Gcm {
        String sender_name;
        String sender_message;
        String sender_id;


        public String getNOTI_ID() {
            return sender_id;
        }


        public void setNOTI_ID(String NOTI_ID) {
            this.sender_id = NOTI_ID;
        }

        public String getNOTI_TITLE() {
            return sender_name;
        }

        public void setNOTI_TITLE(String NOTI_TITLE) {
            this.sender_name = NOTI_TITLE;
        }

        public String getNOTI_MESSAGE() {
            return sender_message;
        }

        public void setNOTI_MESSAGE(String NOTI_MESSAGE) {
            this.sender_message = NOTI_MESSAGE;
        }

    }



}
