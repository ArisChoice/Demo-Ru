package com.app.rum_a.net.firebase;

import com.google.gson.annotations.SerializedName;

/**
 * Created by harish on 2/2/18.
 */
//{JsonData={"ProfileImage":"http://localhost:49907/Images/UsersUpload/6ff80a1c-c913-4072-a7d8-7c4449b3c0791517568114949image.jpg","Name":"Maninder","UserId":30,"KissaaaNowStatus":false}, ExternalUser=28, userID=30, alert=You and Maninder' Matched each other, badge=0, sound=sound.caf, NotificationType=5}
public class NotificationStructureModel {


    /**
     * ProfileImage :
     * Name : Dom
     * Msg : Haris Liked Your Property 'Dom property '
     * UserId : 40
     * PropertyID : 56
     * OwnerID : 42
     * NotificationType : 1
     */

    @SerializedName("ProfileImage")
    private String ProfileImage;
    @SerializedName("Name")
    private String Name;
    @SerializedName("Msg")
    private String Msg;
    @SerializedName("UserId")
    private int UserId;
    @SerializedName("PropertyID")
    private int PropertyID;
    @SerializedName("OwnerID")
    private int OwnerID;
    @SerializedName("NotificationType")
    private int NotificationType;

    public String getProfileImage() {
        return ProfileImage;
    }

    public void setProfileImage(String ProfileImage) {
        this.ProfileImage = ProfileImage;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getMsg() {
        return Msg;
    }

    public void setMsg(String Msg) {
        this.Msg = Msg;
    }

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int UserId) {
        this.UserId = UserId;
    }

    public int getPropertyID() {
        return PropertyID;
    }

    public void setPropertyID(int PropertyID) {
        this.PropertyID = PropertyID;
    }

    public int getOwnerID() {
        return OwnerID;
    }

    public void setOwnerID(int OwnerID) {
        this.OwnerID = OwnerID;
    }

    public int getNotificationType() {
        return NotificationType;
    }

    public void setNotificationType(int NotificationType) {
        this.NotificationType = NotificationType;
    }
}
