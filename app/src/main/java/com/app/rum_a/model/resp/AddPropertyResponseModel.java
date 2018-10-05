package com.app.rum_a.model.resp;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by harish on 7/9/18.
 */

public class AddPropertyResponseModel implements Serializable {

    /**
     * Status : 201
     * Message : Property Added Successfully
     * result : {"PropertyID":26,"FKUserID":5,"name":"Miragr Prop","location":null,"description":"EIR kfj the and the I have a have a look good on the in the the in a new job in the in the the in a couple couple more information on this page and click on the to the UK in the in the the in the the in a new ","price":70000,"IsDeleted":false,"CreatedDate":"2018-09-07T17:56:55.2016163+05:30","address":"DLF Building, Tower B, Level 2, IT Park Rd, Phase - I, Manimajra, Panchkula, Haryana 160101, India","latitude":"30.727952199999997","longitude":"76.846263","PropertyType":2,"SellingType":2,"PropertyImageList":[{"ImageURL":"Images/PropertyUpload/8e5f0dcb-3b1d-434b-9b85-a477989c696acropped410134744.jpg","ImageID":54,"PropertyID":26}]}
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
         * PropertyID : 26
         * FKUserID : 5
         * name : Miragr Prop
         * location : null
         * description : EIR kfj the and the I have a have a look good on the in the the in a new job in the in the the in a couple couple more information on this page and click on the to the UK in the in the the in the the in a new
         * price : 70000
         * IsDeleted : false
         * CreatedDate : 2018-09-07T17:56:55.2016163+05:30
         * address : DLF Building, Tower B, Level 2, IT Park Rd, Phase - I, Manimajra, Panchkula, Haryana 160101, India
         * latitude : 30.727952199999997
         * longitude : 76.846263
         * PropertyType : 2
         * SellingType : 2
         * PropertyImageList : [{"ImageURL":"Images/PropertyUpload/8e5f0dcb-3b1d-434b-9b85-a477989c696acropped410134744.jpg","ImageID":54,"PropertyID":26}]
         */

        @SerializedName("PropertyID")
        private int PropertyID;
        @SerializedName("FKUserID")
        private int FKUserID;
        @SerializedName("name")
        private String name;
        @SerializedName("location")
        private Object location;
        @SerializedName("description")
        private String description;
        @SerializedName("price")
        private int price;
        @SerializedName("IsDeleted")
        private boolean IsDeleted;
        @SerializedName("CreatedDate")
        private String CreatedDate;
        @SerializedName("address")
        private String address;
        @SerializedName("latitude")
        private String latitude;
        @SerializedName("longitude")
        private String longitude;
        @SerializedName("PropertyType")
        private int PropertyType;
        @SerializedName("SellingType")
        private int SellingType;
        @SerializedName("PropertyImageList")
        private List<PropertyImageListBean> PropertyImageList;

        public int getPropertyID() {
            return PropertyID;
        }

        public void setPropertyID(int PropertyID) {
            this.PropertyID = PropertyID;
        }

        public int getFKUserID() {
            return FKUserID;
        }

        public void setFKUserID(int FKUserID) {
            this.FKUserID = FKUserID;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Object getLocation() {
            return location;
        }

        public void setLocation(Object location) {
            this.location = location;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }

        public boolean isIsDeleted() {
            return IsDeleted;
        }

        public void setIsDeleted(boolean IsDeleted) {
            this.IsDeleted = IsDeleted;
        }

        public String getCreatedDate() {
            return CreatedDate;
        }

        public void setCreatedDate(String CreatedDate) {
            this.CreatedDate = CreatedDate;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
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

        public int getPropertyType() {
            return PropertyType;
        }

        public void setPropertyType(int PropertyType) {
            this.PropertyType = PropertyType;
        }

        public int getSellingType() {
            return SellingType;
        }

        public void setSellingType(int SellingType) {
            this.SellingType = SellingType;
        }

        public List<PropertyImageListBean> getPropertyImageList() {
            return PropertyImageList;
        }

        public void setPropertyImageList(List<PropertyImageListBean> PropertyImageList) {
            this.PropertyImageList = PropertyImageList;
        }

        public static class PropertyImageListBean {
            /**
             * ImageURL : Images/PropertyUpload/8e5f0dcb-3b1d-434b-9b85-a477989c696acropped410134744.jpg
             * ImageID : 54
             * PropertyID : 26
             */

            @SerializedName("ImageURL")
            private String ImageURL;
            @SerializedName("ImageID")
            private int ImageID;
            @SerializedName("PropertyID")
            private int PropertyID;

            public String getImageURL() {
                return ImageURL;
            }

            public void setImageURL(String ImageURL) {
                this.ImageURL = ImageURL;
            }

            public int getImageID() {
                return ImageID;
            }

            public void setImageID(int ImageID) {
                this.ImageID = ImageID;
            }

            public int getPropertyID() {
                return PropertyID;
            }

            public void setPropertyID(int PropertyID) {
                this.PropertyID = PropertyID;
            }
        }
    }
}
