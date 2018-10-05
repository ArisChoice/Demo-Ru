package com.app.rum_a.model.resp;

import com.google.gson.annotations.SerializedName;

/**
 * Created by harish on 11/9/18.
 */

public class UserSettingsModel {

    /**
     * Status : 201
     * Message : User Settings
     * result : {"UserId":27,"address":"","DistanceType":"","MaxLocation":160,"StartRangePrice":0,"EndRangePrice":0,"seekingType":0,"PrivateAccount":0,"PushNotification":0,"latitude":"30.7280327","longitude":"76.8461713"}
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
         * UserId : 27
         * address :
         * DistanceType :
         * MaxLocation : 160
         * StartRangePrice : 0
         * EndRangePrice : 0
         * seekingType : 0
         * PrivateAccount : 0
         * PushNotification : 0
         * latitude : 30.7280327
         * longitude : 76.8461713
         */

        @SerializedName("UserId")
        private int UserId;
        @SerializedName("address")
        private String address;
        @SerializedName("DistanceType")
        private String DistanceType;
        @SerializedName("MaxLocation")
        private int MaxLocation;
        @SerializedName("StartRangePrice")
        private int StartRangePrice;
        @SerializedName("EndRangePrice")
        private int EndRangePrice;
        @SerializedName("seekingType")
        private int seekingType;

        public String getCurrency() {
            return Currency;
        }

        public void setCurrency(String currency) {
            Currency = currency;
        }

        @SerializedName("Currency")
        private String Currency;

        public int getLookingType() {
            return lookingType;
        }

        public void setLookingType(int lookingType) {
            this.lookingType = lookingType;
        }

        @SerializedName("lookingType")
        private int lookingType;
        @SerializedName("PrivateAccount")
        private boolean PrivateAccount;
        @SerializedName("PushNotification")
        private boolean PushNotification;
        @SerializedName("latitude")
        private String latitude;
        @SerializedName("longitude")
        private String longitude;

        public int getUserId() {
            return UserId;
        }

        public void setUserId(int UserId) {
            this.UserId = UserId;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getDistanceType() {
            return DistanceType;
        }

        public void setDistanceType(String DistanceType) {
            this.DistanceType = DistanceType;
        }

        public int getMaxLocation() {
            return MaxLocation;
        }

        public void setMaxLocation(int MaxLocation) {
            this.MaxLocation = MaxLocation;
        }

        public int getStartRangePrice() {
            return StartRangePrice;
        }

        public void setStartRangePrice(int StartRangePrice) {
            this.StartRangePrice = StartRangePrice;
        }

        public int getEndRangePrice() {
            return EndRangePrice;
        }

        public void setEndRangePrice(int EndRangePrice) {
            this.EndRangePrice = EndRangePrice;
        }

        public int getSeekingType() {
            return seekingType;
        }

        public void setSeekingType(int seekingType) {
            this.seekingType = seekingType;
        }

        public boolean getPrivateAccount() {
            return PrivateAccount;
        }

        public void setPrivateAccount(boolean PrivateAccount) {
            this.PrivateAccount = PrivateAccount;
        }

        public boolean getPushNotification() {
            return PushNotification;
        }

        public void setPushNotification(boolean PushNotification) {
            this.PushNotification = PushNotification;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }
    }
}
