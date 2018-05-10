package com.quislisting.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Listing extends BaseListing {

    @SerializedName("content")
    private String content;
    @SerializedName("contactInfo")
    private String contactInfo;
    @SerializedName("dlListingFields")
    private List<ListingField> dlListingFields;
    @SerializedName("dlCategories")
    private List<String> dlCategories;
    @SerializedName("attachments")
    private List<Attachment> attachments;

    public String getContent() {
        return content;
    }

    public void setContent(final String content) {
        this.content = content;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(final String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public List<ListingField> getDlListingFields() {
        return dlListingFields;
    }

    public void setDlListingFields(final List<ListingField> dlListingFields) {
        this.dlListingFields = dlListingFields;
    }

    public List<String> getDlCategories() {
        return dlCategories;
    }

    public void setDlCategories(final List<String> dlCategories) {
        this.dlCategories = dlCategories;
    }

    public List<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(final List<Attachment> attachments) {
        this.attachments = attachments;
    }
}
