package com.app.rum_a.model.resp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by harish on 10/9/18.
 */

public class PropertyListResponseModel implements Serializable {

    /**
     * Status : 201
     * Message : Property List
     * result : [{"PropertyID":28,"FKUserID":21,"name":"Tree house dj","location":null,"description":" This is a test drive of it and I will be in a new a good new a job at a later stage time and I to I will have be able a good time to ","price":50000,"IsDeleted":false,"CreatedDate":"2018-09-10T10:46:29.417","PropertyImageList":[{"ImageURL":"Images/PropertyUpload/63818f3f-2479-4abc-9bc9-fba4ebee5d57cropped559135609.jpg","ImageID":56,"PropertyID":28}],"address":"Daria, Chandigarh, 160102, India","latitude":"30.702393299999994","longitude":"76.8214717","PropertyType":0,"SellingType":2}]
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

    public static class ResultBean implements Serializable {
        /**
         * PropertyID : 28
         * FKUserID : 21
         * name : Tree house dj
         * location : null
         * description :  This is a test drive of it and I will be in a new a good new a job at a later stage time and I to I will have be able a good time to
         * price : 50000
         * IsDeleted : false
         * CreatedDate : 2018-09-10T10:46:29.417
         * PropertyImageList : [{"ImageURL":"Images/PropertyUpload/63818f3f-2479-4abc-9bc9-fba4ebee5d57cropped559135609.jpg","ImageID":56,"PropertyID":28}]
         * address : Daria, Chandigarh, 160102, India
         * latitude : 30.702393299999994
         * longitude : 76.8214717
         * PropertyType : 0
         * SellingType : 2
         */

        @SerializedName("PropertyID")
        private int PropertyID;
        @SerializedName("FKUserID")
        private int FKUserID;
        @SerializedName("name")
        private String name;
        @SerializedName("location")
        private String location;
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

        public String getCurrency() {
            return Currency;
        }

        public void setCurrency(String currency) {
            Currency = currency;
        }

        @SerializedName("Currency")
        private String Currency;
        @SerializedName("latitude")
        private String latitude;
        @SerializedName("longitude")
        private String longitude;

        public String getDistanceType() {
            return DistanceType;
        }

        public void setDistanceType(String distanceType) {
            DistanceType = distanceType;
        }

        @SerializedName("DistanceType")
        private String DistanceType;
        @SerializedName("PropertyType")
        private int PropertyType;
        @SerializedName("SellingType")
        private int SellingType;

        public int getForRentOrBuy() {
            return ForRentOrBuy;
        }

        public void setForRentOrBuy(int forRentOrBuy) {
            ForRentOrBuy = forRentOrBuy;
        }

        @SerializedName("ForRentOrBuy")
        private int ForRentOrBuy;
        @SerializedName("PropertyImageList")
        private List<PropertyImageListBean> PropertyImageList;

        public String getChatDialog() {
            return ChatDialog;
        }

        public void setChatDialog(String chatDialog) {
            ChatDialog = chatDialog;
        }

        @SerializedName("ChatDialog")
        private String ChatDialog;

        public OwnerDetails getOwnerDetails() {
            return ownerDetails;
        }

        public void setOwnerDetails(OwnerDetails ownerDetails) {
            this.ownerDetails = ownerDetails;
        }

        @SerializedName("OwnerDetails")
        private OwnerDetails ownerDetails;

        public double getDistance() {
            return Distance;
        }

        public void setDistance(double distance) {
            Distance = distance;
        }

        @SerializedName("Distance")
        private double Distance;

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

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
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

        public static class PropertyImageListBean implements Serializable {
            /**
             * ImageURL : Images/PropertyUpload/63818f3f-2479-4abc-9bc9-fba4ebee5d57cropped559135609.jpg
             * ImageID : 56
             * PropertyID : 28
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


        public class OwnerDetails implements Serializable {

            @SerializedName("OwnerId")
            @Expose
            private Integer ownerId;

            public Integer getOwnerId() {
                return ownerId;
            }

            public void setOwnerId(Integer ownerId) {
                this.ownerId = ownerId;
            }

            public String getOwnerFirstName() {
                return ownerFirstName;
            }

            public void setOwnerFirstName(String ownerFirstName) {
                this.ownerFirstName = ownerFirstName;
            }

            public String getOwnerLastName() {
                return ownerLastName;
            }

            public void setOwnerLastName(String ownerLastName) {
                this.ownerLastName = ownerLastName;
            }

            public List<OwnerProfilePic> getOwnerProfilePics() {
                return ownerProfilePics;
            }

            public void setOwnerProfilePics(List<OwnerProfilePic> ownerProfilePics) {
                this.ownerProfilePics = ownerProfilePics;
            }

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public String getOwnerCurrency() {
                return ownerCurrency;
            }

            public void setOwnerCurrency(String ownerCurrency) {
                this.ownerCurrency = ownerCurrency;
            }

            @SerializedName("OwnerFirstName")
            @Expose
            private String ownerFirstName;
            @SerializedName("OwnerLastName")
            @Expose
            private String ownerLastName;
            @SerializedName("OwnerProfilePics")
            @Expose
            private List<OwnerProfilePic> ownerProfilePics = null;
            @SerializedName("Address")
            @Expose
            private String address;
            @SerializedName("OwnerCurrency")
            @Expose
            private String ownerCurrency;
            @SerializedName("QbId")
            @Expose
            private String QbId;

            public String getQbId() {
                return QbId;
            }

            public void setQbId(String qbId) {
                QbId = qbId;
            }
        }
    }

    public class OwnerProfilePic implements Serializable {

        @SerializedName("ImageURL")
        @Expose
        private String imageURL;
        @SerializedName("ImageType")
        @Expose
        private String imageType;

        public String getImageURL() {
            return imageURL;
        }

        public void setImageURL(String imageURL) {
            this.imageURL = imageURL;
        }

        public String getImageType() {
            return imageType;
        }

        public void setImageType(String imageType) {
            this.imageType = imageType;
        }

    }
}
