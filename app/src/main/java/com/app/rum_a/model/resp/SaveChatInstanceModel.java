package com.app.rum_a.model.resp;

import com.google.gson.annotations.SerializedName;

/**
 * Created by harish on 21/9/18.
 */

public class SaveChatInstanceModel {

    /**
     * Status : 201
     * Message : Chat Saved
     * result : {"ID":0,"Status":1,"Message":"Added"}
     */

    @SerializedName("Status")
    private int Status;
    @SerializedName("Message")
    private String Message;
    @SerializedName("result")
    private ResultBean result;

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

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * ID : 0
         * Status : 1
         * Message : Added
         */

        @SerializedName("ID")
        private int ID;
        @SerializedName("Status")
        private int Status;
        @SerializedName("Message")
        private String Message;

        public int getID() {
            return ID;
        }

        public void setID(int ID) {
            this.ID = ID;
        }

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
    }
}
