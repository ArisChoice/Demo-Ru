package com.app.rum_a.net.firebase;

/**
 * Created by harish on 20/9/17.
 */


//FIREBASE PUSH DATA------------------{receiver_info={"image":"","phone":null,"last_name":"Kumar","id":124,"user_modified_id":"01710302","first_name":"Harish","email":"kingfisher033@gmail.com"}, notification_type=1, sound=1, title=Send Request, vibrate=1, largeIcon=large_icon, sender_info={"image":"http:\/\/132.148.135.156\/~wirelesstrek\/public\/uploads\/profilepics\/1504876101.jpg","phone":"919876543098","last_name":"one","id":197,"user_modified_id":"01710398","first_name":"Gent","email":"geny1@gmail.com"}, message=Gent one  sent you a friend request., small_icon=small_icon}
public class PushSenderModel {
    /**
     * image : http://132.148.135.156/~wirelesstrek/public/uploads/profilepics/1504876101.jpg
     * phone : 919876543098
     * last_name : one
     * id : 197
     * user_modified_id : 01710398
     * first_name : Gent
     * email : geny1@gmail.com
     */

    private String image;
    private String phone;
    private String last_name;
    private int id;
    private String user_modified_id;
    private String first_name;
    private String email;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser_modified_id() {
        return user_modified_id;
    }

    public void setUser_modified_id(String user_modified_id) {
        this.user_modified_id = user_modified_id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
