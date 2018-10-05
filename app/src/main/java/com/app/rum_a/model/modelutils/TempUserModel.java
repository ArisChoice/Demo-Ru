package com.app.rum_a.model.modelutils;

/**
 * Created by harish on 21/9/18.
 */

public class TempUserModel {
    public String getUserName() {

        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    String userName;
    String userId;
    String userImage;

}
