package com.quislisting.model.request;

public class AuthenticateUserRequest {

    private final String username;
    private final String password;
    private final boolean rememberMe;

    public AuthenticateUserRequest(final String username, final String password,
                                   final boolean rememberMe) {
        this.username = username;
        this.password = password;
        this.rememberMe = rememberMe;
    }
}
