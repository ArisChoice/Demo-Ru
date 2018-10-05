package com.app.rum_a.model.modelutils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FacebookUser {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @Expose
    private Double timezone;
    @SerializedName("email")
    @Expose
    private String email;
    @Expose
    private Boolean verified;
    @Expose
    private String name;

    public String getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(String birthDay) {
        this.birthDay = birthDay;
    }

    @SerializedName("birthday")
    @Expose
    private String birthDay;
    @Expose
    private String locale;
    @Expose
    private String link;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @Expose
    private String gender;
    @SerializedName("updated_time")
    @Expose
    private String updatedTime;

    /**
     * @return The id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id The id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return The firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @param firstName The first_name
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return The timezone
     */
    public Double getTimezone() {
        return timezone;
    }

    /**
     * @param timezone The timezone
     */
    public void setTimezone(Double timezone) {
        this.timezone = timezone;
    }

    /**
     * @return The email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email The email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return The verified
     */
    public Boolean getVerified() {
        return verified;
    }

    /**
     * @param verified The verified
     */
    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    /**
     * @return The name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return The locale
     */
    public String getLocale() {
        return locale;
    }

    /**
     * @param locale The locale
     */
    public void setLocale(String locale) {
        this.locale = locale;
    }

    /**
     * @return The link
     */
    public String getLink() {
        return link;
    }

    /**
     * @param link The link
     */
    public void setLink(String link) {
        this.link = link;
    }

    /**
     * @return The lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @param lastName The last_name
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * @return The gender
     */
    public String getGender() {
        return gender;
    }

    /**
     * @param gender The gender
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * @return The updatedTime
     */
    public String getUpdatedTime() {
        return updatedTime;
    }

    /**
     * @param updatedTime The updated_time
     */
    public void setUpdatedTime(String updatedTime) {
        this.updatedTime = updatedTime;
    }

}