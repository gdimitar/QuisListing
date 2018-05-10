package com.quislisting.model;

import com.google.gson.annotations.SerializedName;

public class AuthenticationResult {

    @SerializedName("id_token")
    private String id_token;

    public String getId_token() {
        return id_token;
    }

    public void setId_token(final String id_token) {
        this.id_token = id_token;
    }
}
