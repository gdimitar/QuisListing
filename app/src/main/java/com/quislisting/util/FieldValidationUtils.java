package com.quislisting.util;

import android.util.Patterns;

public class FieldValidationUtils {

    public FieldValidationUtils() {

    }

    public static String validateFieldValue(final String value, final String errorMessage,
                                            final int fieldLength) {
        if (value.isEmpty() || value.length() > fieldLength) {
            return errorMessage;
        }

        return null;
    }

    public static String validateEmailFieldValue(final String value, final String errorMessage) {
        if (value.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(value).matches()) {
            return errorMessage;
        }

        return null;
    }
}
