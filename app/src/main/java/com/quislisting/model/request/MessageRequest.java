package com.quislisting.model.request;

public class MessageRequest {

    private final String senderName;
    private final String senderEmail;
    private final String text;
    private final String languageCode;

    public MessageRequest(final String senderName, final String senderEmail, final String text,
                          final String languageCode) {
        this.senderName = senderName;
        this.senderEmail = senderEmail;
        this.text = text;
        this.languageCode = languageCode;
    }
}
