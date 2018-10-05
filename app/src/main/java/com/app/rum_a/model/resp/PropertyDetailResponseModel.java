package com.app.rum_a.model.resp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by harish on 10/9/18.
 */

public class PropertyDetailResponseModel implements Serializable {

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
    private PropertyListResponseModel.ResultBean result;

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

    public PropertyListResponseModel.ResultBean getResult() {
        return result;
    }

    public void setResult(PropertyListResponseModel.ResultBean result) {
        this.result = result;
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
