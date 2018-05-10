package com.quislisting.model;

import com.google.gson.annotations.SerializedName;

public class Attachment {

    @SerializedName("smallImage")
    private String smallImage;
    @SerializedName("originalImage")
    private String originalImage;

    public String getSmallImage() {
        return smallImage;
    }

    public void setSmallImage(final String smallImage) {
        this.smallImage = smallImage;
    }

    public String getOriginalImage() {
        return originalImage;
    }

    public void setOriginalImage(final String originalImage) {
        this.originalImage = originalImage;
    }
}