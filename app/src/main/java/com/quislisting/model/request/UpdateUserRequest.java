package com.quislisting.model.request;

public class UpdateUserRequest {

    private final String login;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final boolean updates;
    private final String langKey;

    public UpdateUserRequest(final String login, final String firstName, final String lastName,
                             final String email, final boolean updates, final String langKey) {
        this.login = login;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.updates = updates;
        this.langKey = langKey;
    }
}
