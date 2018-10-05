package com.app.rum_a.model.resp;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by harish on 19/9/18.
 */

public class ChatUsersModelResponse {

    /**
     * Status : 201
     * Message : Chat List
     * result : [{"PropertyId":44,"OwnerId":25,"ChatDialog":"5ba2393ea28f9a3fa9e061eb","SenderId":27,"ReceiverId":25,"ChatTime":153735814,"RecentMessage":"First Message","SenderName":"Atest user","SenderImage":[{"ImageURL":"Images/UsersUpload/DummyUserImage.jpg","ImageType":"A"},{"ImageURL":"Images/UsersUpload/DummyUserImage.jpg","ImageType":"B"},{"ImageURL":"Images/UsersUpload/DummyUserImage.jpg","ImageType":"C"}]}]
     */

    @SerializedName("Status")
    private int Status;
    @SerializedName("Message")
    private String Message;
    @SerializedName("result")
    private List<ResultBean> result;

    public int getStatus() {
        return Status;
    }

    public void setStatus(int Status) {
        this.Status = Status;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String Message) {
        this.Message = Message;
    }

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * PropertyId : 44
         * OwnerId : 25
         * ChatDialog : 5ba2393ea28f9a3fa9e061eb
         * SenderId : 27
         * ReceiverId : 25
         * ChatTime : 153735814
         * RecentMessage : First Message
         * SenderName : Atest user
         * SenderImage : [{"ImageURL":"Images/UsersUpload/DummyUserImage.jpg","ImageType":"A"},{"ImageURL":"Images/UsersUpload/DummyUserImage.jpg","ImageType":"B"},{"ImageURL":"Images/UsersUpload/DummyUserImage.jpg","ImageType":"C"}]
         */

        @SerializedName("PropertyId")
        private int PropertyId;
        @SerializedName("OwnerId")
        private int OwnerId;
        @SerializedName("ChatDialog")
        private String ChatDialog;
        @SerializedName("SenderId")
        private int SenderId;
        @SerializedName("ReceiverId")
        private int ReceiverId;
        @SerializedName("ChatTime")
        private int ChatTime;
        @SerializedName("RecentMessage")
        private String RecentMessage;
        @SerializedName("SenderName")
        private String SenderName;
        @SerializedName("SenderImage")
        private List<SenderImageBean> SenderImage;

        public int getPropertyId() {
            return PropertyId;
        }

        public void setPropertyId(int PropertyId) {
            this.PropertyId = PropertyId;
        }

        public int getOwnerId() {
            return OwnerId;
        }

        public void setOwnerId(int OwnerId) {
            this.OwnerId = OwnerId;
        }

        public String getChatDialog() {
            return ChatDialog;
        }

        public void setChatDialog(String ChatDialog) {
            this.ChatDialog = ChatDialog;
        }

        public int getSenderId() {
            return SenderId;
        }

        public void setSenderId(int SenderId) {
            this.SenderId = SenderId;
        }

        public int getReceiverId() {
            return ReceiverId;
        }

        public void setReceiverId(int ReceiverId) {
            this.ReceiverId = ReceiverId;
        }

        public int getChatTime() {
            return ChatTime;
        }

        public void setChatTime(int ChatTime) {
            this.ChatTime = ChatTime;
        }

        public String getRecentMessage() {
            return RecentMessage;
        }

        public void setRecentMessage(String RecentMessage) {
            this.RecentMessage = RecentMessage;
        }

        public String getSenderName() {
            return SenderName;
        }

        public void setSenderName(String SenderName) {
            this.SenderName = SenderName;
        }

        public List<SenderImageBean> getSenderImage() {
            return SenderImage;
        }

        public void setSenderImage(List<SenderImageBean> SenderImage) {
            this.SenderImage = SenderImage;
        }

        public static class SenderImageBean {
            /**
             * ImageURL : Images/UsersUpload/DummyUserImage.jpg
             * ImageType : A
             */

            @SerializedName("ImageURL")
            private String ImageURL;
            @SerializedName("ImageType")
            private String ImageType;

            public String getImageURL() {
                return ImageURL;
            }

            public void setImageURL(String ImageURL) {
                this.ImageURL = ImageURL;
            }

            public String getImageType() {
                return ImageType;
            }

            public void setImageType(String ImageType) {
                this.ImageType = ImageType;
            }
        }
    }
}
