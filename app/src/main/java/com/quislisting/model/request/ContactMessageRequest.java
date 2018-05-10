package com.quislisting.model.request;

public class ContactMessageRequest {

    private final String email;
    private final String name;
    private final String subject;
    private final String message;
    private final String languageCode;

    public ContactMessageRequest(final String email, final String name, final String subject,
                                 final String message, final String languageCode) {
        this.email = email;
        this.name = name;
        this.subject = subject;
        this.message = message;
        this.languageCode = languageCode;
    }
}
