package com.quislisting.model;

import com.google.gson.annotations.SerializedName;

import java.sql.Timestamp;

public class Message {

    @SerializedName("id")
    private Long id;
    @SerializedName("sender")
    private BaseUser sender;
    @SerializedName("receiver")
    private BaseUser receiver;
    @SerializedName("listingId")
    private Long listingId;
    @SerializedName("text")
    private String text;
    @SerializedName("created")
    private Timestamp created;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public BaseUser getSender() {
        return sender;
    }

    public void setSender(final BaseUser sender) {
        this.sender = sender;
    }

    public BaseUser getReceiver() {
        return receiver;
    }

    public void setReceiver(final BaseUser receiver) {
        this.receiver = receiver;
    }

    public Long getListingId() {
        return listingId;
    }

    public void setListingId(final Long listingId) {
        this.listingId = listingId;
    }

    public String getText() {
        return text;
    }

    public void setText(final String text) {
        this.text = text;
    }

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(final Timestamp created) {
        this.created = created;
    }
}
