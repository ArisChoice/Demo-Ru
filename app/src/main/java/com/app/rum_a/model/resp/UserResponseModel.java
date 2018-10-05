package com.app.rum_a.model.resp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by harish on 4/9/18.
 */

public class UserResponseModel implements Serializable {

    /**
     * Status : 201
     * Message : SignUp Successfully
     * result : {"User":{"UserId":18,"UsertypeId":0,"firstName":"Rum","lastName":"User2","userName":null,"Email":"rumuser2@gmail.com","Password":"jZae727K08KaOmKSgOaGzww/XVqGr/PKEgIMkjrcbJI=","lookingType":1,"seekingType":1,"IsDeleted":false,"DateCreated":"2018-09-04T19:06:15.4787698+05:30","DateModified":"2018-09-04T19:06:15.4787698+05:30","SessionId":"ae5c2dc9-41aa-42ed-be1d-605d0945aaf6","DeviceType":-1,"UniqueDeviceId":null,"DeviceId":null}}
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
         * User : {"UserId":18,"UsertypeId":0,"firstName":"Rum","lastName":"User2","userName":null,"Email":"rumuser2@gmail.com","Password":"jZae727K08KaOmKSgOaGzww/XVqGr/PKEgIMkjrcbJI=","lookingType":1,"seekingType":1,"IsDeleted":false,"DateCreated":"2018-09-04T19:06:15.4787698+05:30","DateModified":"2018-09-04T19:06:15.4787698+05:30","SessionId":"ae5c2dc9-41aa-42ed-be1d-605d0945aaf6","DeviceType":-1,"UniqueDeviceId":null,"DeviceId":null}
         */

        @SerializedName("User")
        private UserBean User;

        public UserBean getUser() {
            return User;
        }

        public void setUser(UserBean User) {
            this.User = User;
        }

        public class ProfilePic {

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

        public class QuickBloxDetails {

            @SerializedName("UserId")
            @Expose
            private Integer userId;
            @SerializedName("QbFirstName")
            @Expose
            private String qbFirstName;
            @SerializedName("QbLastName")
            @Expose
            private String qbLastName;
            @SerializedName("QbId")
            @Expose
            private Integer qbId;
            @SerializedName("QbImage")
            @Expose
            private String qbImage;

            public Integer getUserId() {
                return userId;
            }

            public void setUserId(Integer userId) {
                this.userId = userId;
            }

            public String getQbFirstName() {
                return qbFirstName;
            }

            public void setQbFirstName(String qbFirstName) {
                this.qbFirstName = qbFirstName;
            }

            public String getQbLastName() {
                return qbLastName;
            }

            public void setQbLastName(String qbLastName) {
                this.qbLastName = qbLastName;
            }

            public Integer getQbId() {
                return qbId;
            }

            public void setQbId(Integer qbId) {
                this.qbId = qbId;
            }

            public String getQbImage() {
                return qbImage;
            }

            public void setQbImage(String qbImage) {
                this.qbImage = qbImage;
            }

        }


        public static class UserBean {
            /**
             * UserId : 18
             * UsertypeId : 0
             * firstName : Rum
             * lastName : User2
             * userName : null
             * Email : rumuser2@gmail.com
             * Password : jZae727K08KaOmKSgOaGzww/XVqGr/PKEgIMkjrcbJI=
             * lookingType : 1
             * seekingType : 1
             * IsDeleted : false
             * DateCreated : 2018-09-04T19:06:15.4787698+05:30
             * DateModified : 2018-09-04T19:06:15.4787698+05:30
             * SessionId : ae5c2dc9-41aa-42ed-be1d-605d0945aaf6
             * DeviceType : -1
             * UniqueDeviceId : null
             * DeviceId : null
             */

            @SerializedName("UserId")
            private int UserId;
            @SerializedName("UsertypeId")
            private int UsertypeId;
            @SerializedName("firstName")
            private String firstName;
            @SerializedName("lastName")
            private String lastName;
            @SerializedName("userName")
            private Object userName;
            @SerializedName("Email")
            private String Email;
            @SerializedName("Password")
            private String Password;
            @SerializedName("lookingType")
            private int lookingType;
            @SerializedName("seekingType")
            private int seekingType;
            @SerializedName("IsDeleted")
            private boolean IsDeleted;
            @SerializedName("DateCreated")
            private String DateCreated;
            @SerializedName("DateModified")
            private String DateModified;
            @SerializedName("SessionId")
            private String SessionId;
            @SerializedName("DeviceType")
            private int DeviceType;
            @SerializedName("UniqueDeviceId")
            private Object UniqueDeviceId;
            @SerializedName("DeviceId")
            private Object DeviceId;
            @SerializedName("TotalLikeProperties")
            @Expose
            private Integer totalLikeProperties;
            @SerializedName("TotalDislikeProperties")
            @Expose
            private Integer totalDislikeProperties;

            public String getBudget() {
                return Budget;
            }

            public void setBudget(String budget) {
                Budget = budget;
            }

            @SerializedName("Budget")
            private String Budget;

            public String getAddress() {
                return Address;
            }

            public void setAddress(String address) {
                Address = address;
            }

            public String getCurrency() {
                return Currency;
            }

            public void setCurrency(String currency) {
                Currency = currency;
            }

            @SerializedName("Currency")
            private String Currency;

            @SerializedName("Address")
            private String Address;

            public String getAboutMe() {
                return AboutMe;
            }

            public void setAboutMe(String aboutMe) {
                AboutMe = aboutMe;
            }

            @SerializedName("AboutMe")
            @Expose
            private String AboutMe;
            @SerializedName("ProfilePics")
            @Expose
            private List<ResultBean.ProfilePic> profilePics = null;

            public List<PropertyListResponseModel.ResultBean> getResult() {
                return result;
            }

            public void setResult(List<PropertyListResponseModel.ResultBean> result) {
                this.result = result;
            }

            @SerializedName("UserProperty")
            private List<PropertyListResponseModel.ResultBean> result;
            @SerializedName("QuickBloxDetails")
            @Expose
            private ResultBean.QuickBloxDetails quickBloxDetails;

            public ResultBean.QuickBloxDetails getQuickBloxDetails() {
                return quickBloxDetails;
            }

            public void setQuickBloxDetails(ResultBean.QuickBloxDetails quickBloxDetails) {
                this.quickBloxDetails = quickBloxDetails;
            }

            public Integer getTotalLikeProperties() {
                return totalLikeProperties;
            }

            public void setTotalLikeProperties(Integer totalLikeProperties) {
                this.totalLikeProperties = totalLikeProperties;
            }

            public Integer getTotalDislikeProperties() {
                return totalDislikeProperties;
            }

            public void setTotalDislikeProperties(Integer totalDislikeProperties) {
                this.totalDislikeProperties = totalDislikeProperties;
            }

            public List<ResultBean.ProfilePic> getProfilePics() {
                return profilePics;
            }

            public void setProfilePics(List<ResultBean.ProfilePic> profilePics) {
                this.profilePics = profilePics;
            }

            public int getUserId() {
                return UserId;
            }

            public void setUserId(int UserId) {
                this.UserId = UserId;
            }

            public int getUsertypeId() {
                return UsertypeId;
            }

            public void setUsertypeId(int UsertypeId) {
                this.UsertypeId = UsertypeId;
            }

            public String getFirstName() {
                return firstName;
            }

            public void setFirstName(String firstName) {
                this.firstName = firstName;
            }

            public String getLastName() {
                return lastName;
            }

            public void setLastName(String lastName) {
                this.lastName = lastName;
            }

            public Object getUserName() {
                return userName;
            }

            public void setUserName(Object userName) {
                this.userName = userName;
            }

            public String getEmail() {
                return Email;
            }

            public void setEmail(String Email) {
                this.Email = Email;
            }

            public String getPassword() {
                return Password;
            }

            public void setPassword(String Password) {
                this.Password = Password;
            }

            public int getLookingType() {
                return lookingType;
            }

            public void setLookingType(int lookingType) {
                this.lookingType = lookingType;
            }

            public int getSeekingType() {
                return seekingType;
            }

            public void setSeekingType(int seekingType) {
                this.seekingType = seekingType;
            }

            public boolean isIsDeleted() {
                return IsDeleted;
            }

            public void setIsDeleted(boolean IsDeleted) {
                this.IsDeleted = IsDeleted;
            }

            public String getDateCreated() {
                return DateCreated;
            }

            public void setDateCreated(String DateCreated) {
                this.DateCreated = DateCreated;
            }

            public String getDateModified() {
                return DateModified;
            }

            public void setDateModified(String DateModified) {
                this.DateModified = DateModified;
            }

            public String getSessionId() {
                return SessionId;
            }

            public void setSessionId(String SessionId) {
                this.SessionId = SessionId;
            }

            public int getDeviceType() {
                return DeviceType;
            }

            public void setDeviceType(int DeviceType) {
                this.DeviceType = DeviceType;
            }

            public Object getUniqueDeviceId() {
                return UniqueDeviceId;
            }

            public void setUniqueDeviceId(Object UniqueDeviceId) {
                this.UniqueDeviceId = UniqueDeviceId;
            }

            public Object getDeviceId() {
                return DeviceId;
            }

            public void setDeviceId(Object DeviceId) {
                this.DeviceId = DeviceId;
            }
        }
    }
}
