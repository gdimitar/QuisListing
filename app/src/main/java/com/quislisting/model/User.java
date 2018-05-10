package com.quislisting.model;

import com.google.gson.annotations.SerializedName;

import java.util.Set;

public class User {

    @SerializedName("id")
    private Long id;
    @SerializedName("login")
    private String login;
    @SerializedName("firstName")
    private String firstName;
    @SerializedName("lastName")
    private String lastName;
    @SerializedName("email")
    private String email;
    @SerializedName("imageUrl")
    private String imageUrl;
    @SerializedName("activated")
    private boolean activated = false;
    @SerializedName("langKey")
    private String langKey;
    @SerializedName("updates")
    private Boolean updates;
    @SerializedName("createdBy")
    private String createdBy;
    @SerializedName("createdDate")
    private String createdDate;
    @SerializedName("lastModifiedBy")
    private String lastModifiedBy;
    @SerializedName("lastModifiedDate")
    private String lastModifiedDate;
    @SerializedName("authorities")
    private Set<String> authorities;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(final String login) {
        this.login = login;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(final String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(final boolean activated) {
        this.activated = activated;
    }

    public String getLangKey() {
        return langKey;
    }

    public void setLangKey(final String langKey) {
        this.langKey = langKey;
    }

    public Boolean getUpdates() {
        return updates;
    }

    public void setUpdates(final Boolean updates) {
        this.updates = updates;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(final String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(final String createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(final String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public String getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(final String lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public Set<String> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(final Set<String> authorities) {
        this.authorities = authorities;
    }
}
