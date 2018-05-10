package com.quislisting.model;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;
import java.util.List;

public class BaseListing {

    @SerializedName("id")
    private Long id;
    @SerializedName("title")
    private String title;
    @SerializedName("name")
    private String name;
    @SerializedName("created")
    private Long created;
    @SerializedName("modified")
    private Long modified;
    @SerializedName("languageCode")
    private String languageCode;
    @SerializedName("sourceLanguageCode")
    private String sourceLanguageCode;
    @SerializedName("featuredAttachment")
    private Attachment featuredAttachment;
    @SerializedName("status")
    private Status status;
    @SerializedName("price")
    private BigDecimal price;
    @SerializedName("priceCurrency")
    private String priceCurrency;
    @SerializedName("dlLocations")
    private List<ListingLocation> dlLocations;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Long getCreated() {
        return created;
    }

    public void setCreated(final Long created) {
        this.created = created;
    }

    public Long getModified() {
        return modified;
    }

    public void setModified(final Long modified) {
        this.modified = modified;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(final String languageCode) {
        this.languageCode = languageCode;
    }

    public String getSourceLanguageCode() {
        return sourceLanguageCode;
    }

    public void setSourceLanguageCode(final String sourceLanguageCode) {
        this.sourceLanguageCode = sourceLanguageCode;
    }

    public Attachment getFeaturedAttachment() {
        return featuredAttachment;
    }

    public void setFeaturedAttachment(final Attachment featuredAttachment) {
        this.featuredAttachment = featuredAttachment;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(final Status status) {
        this.status = status;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(final BigDecimal price) {
        this.price = price;
    }

    public String getPriceCurrency() {
        return priceCurrency;
    }

    public void setPriceCurrency(final String priceCurrency) {
        this.priceCurrency = priceCurrency;
    }

    public List<ListingLocation> getDlLocations() {
        return dlLocations;
    }

    public void setDlLocations(final List<ListingLocation> dlLocations) {
        this.dlLocations = dlLocations;
    }

    public enum Status {
        DRAFT,
        PUBLISH_REQUEST,
        PUBLISHED,
        PUBLISH_DISAPPROVED,
        PUBLISH_EXPIRED
    }
}
