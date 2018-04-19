package com.quislisting.model;

import java.math.BigDecimal;
import java.util.List;

public class BaseListing {

    private Long id;
    private String title;
    private String name;
    private String created;
    private String modified;
    private String languageCode;
    private String sourceLanguageCode;
    private Attachment featuredAttachment;
    private Status status;
    private BigDecimal price;
    private String priceCurrency;
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

    public String getCreated() {
        return created;
    }

    public void setCreated(final String created) {
        this.created = created;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(final String modified) {
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
