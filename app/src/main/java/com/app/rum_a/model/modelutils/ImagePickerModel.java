package com.app.rum_a.model.modelutils;

/**
 * Created by harish on 6/9/18.
 */

public class ImagePickerModel {
    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getImageFrom() {
        return imageFrom;
    }

    public void setImageFrom(String imageFrom) {
        this.imageFrom = imageFrom;
    }

    public boolean getImageCroped() {
        return imageCroped;
    }

    public void setImageCroped(boolean imageCroped) {
        this.imageCroped = imageCroped;
    }

    String imagePath;
    String imageFrom;
    boolean imageCroped;
}
