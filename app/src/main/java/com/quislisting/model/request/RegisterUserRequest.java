package com.quislisting.model.request;

public class RegisterUserRequest extends UpdateUserRequest {

    private final String password;

    public RegisterUserRequest(final String login, final String firstName, final String lastName,
                               final String email, final boolean updates, final String langKey,
                               final String password) {
        super(login, firstName, lastName, email, updates, langKey);
        this.password = password;
    }
}
