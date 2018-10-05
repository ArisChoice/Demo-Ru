package com.app.rum_a.model.req;

import java.util.List;

/**
 * Created by harish on 31/8/18.
 */

public class RegisterRequestModel {

    /**
     * NickName : sample string 1
     * firstName : sample string 2
     * lastName : sample string 3
     * userName : sample string 4
     * Email : sample string 5
     * Password : sample string 6
     * lookingType : 1
     * seekingType : 1
     * FacebookId : sample string 7
     * GoogleId : sample string 8
     * Base64String : ["sample string 1","sample string 2"]
     * InstagramProfilePics : ["sample string 1","sample string 2"]
     */

    private String NickName;
    private String firstName;
    private String lastName;
    private String userName;
    private String Email;
    private String Password;
    private int lookingType;
    private int seekingType;
    private String FacebookId;
    private String GoogleId;
    private List<String> Base64String;
    private List<String> InstagramProfilePics;

    public String getNickName() {
        return NickName;
    }

    public void setNickName(String NickName) {
        this.NickName = NickName;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
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

    public String getFacebookId() {
        return FacebookId;
    }

    public void setFacebookId(String FacebookId) {
        this.FacebookId = FacebookId;
    }

    public String getGoogleId() {
        return GoogleId;
    }

    public void setGoogleId(String GoogleId) {
        this.GoogleId = GoogleId;
    }

    public List<String> getBase64String() {
        return Base64String;
    }

    public void setBase64String(List<String> Base64String) {
        this.Base64String = Base64String;
    }

    public List<String> getInstagramProfilePics() {
        return InstagramProfilePics;
    }

    public void setInstagramProfilePics(List<String> InstagramProfilePics) {
        this.InstagramProfilePics = InstagramProfilePics;
    }
}
