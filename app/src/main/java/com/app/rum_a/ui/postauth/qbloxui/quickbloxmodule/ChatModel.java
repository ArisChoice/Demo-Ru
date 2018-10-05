package com.app.rum_a.ui.postauth.qbloxui.quickbloxmodule;

/**
 * Created by Vivek on 27-03-2017.
 */

public class ChatModel {

    Apn aps;
    Gcm gcm;
    public String type;

    public Apn getAps() {
        return aps;
    }

    public void setAps(Apn aps) {
        this.aps = aps;
    }

    public Gcm getGcm() {
        return gcm;
    }

    public void setGcm(Gcm gcm) {
        this.gcm = gcm;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public static class Apn {
        public String alert;
        public String badge;
        public String sound;

        public String getAlert() {
            return alert;
        }

        public void setAlert(String alert) {
            this.alert = alert;
        }

        public String getBadge() {
            return badge;
        }

        public void setBadge(String badge) {
            this.badge = badge;
        }

        public String getSound() {
            return sound;
        }

        public void setSound(String sound) {
            this.sound = sound;
        }
    }


    public static class Gcm {
        String sender_name;
        String sender_message;
        String sender_id;

        public String getPropertyBody() {
            return propertyBody;
        }

        public void setPropertyBody(String propertyBody) {
            this.propertyBody = propertyBody;
        }

        String propertyBody;

        public String getSenderAppUserId() {
            return senderAppUserId;
        }

        public void setSenderAppUserId(String senderAppUserId) {
            this.senderAppUserId = senderAppUserId;
        }

        String senderAppUserId;

        public String getSenderAppUserImage() {
            return senderAppUserImage;
        }

        public void setSenderAppUserImage(String senderAppUserImage) {
            this.senderAppUserImage = senderAppUserImage;
        }

        String senderAppUserImage;


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
