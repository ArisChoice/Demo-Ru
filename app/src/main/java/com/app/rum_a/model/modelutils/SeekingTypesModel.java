package com.app.rum_a.model.modelutils;

/**
 * Created by harish on 11/9/18.
 */

public class SeekingTypesModel {
    public int getSeekingId() {
        return seekingId;
    }

    public void setSeekingId(int seekingId) {
        this.seekingId = seekingId;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    int seekingId;

    public String getSeekingName() {
        return seekingName;
    }

    public void setSeekingName(String seekingName) {
        this.seekingName = seekingName;
    }

    String seekingName;
    boolean isSelected;
}
