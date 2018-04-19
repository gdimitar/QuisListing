package com.quislisting.util;

import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;

public class TextInputLayoutUtils {

    public TextInputLayoutUtils() {

    }

    public static TextInputLayout createTextInputLayout(final AppCompatActivity activity,
                                                        final int width, final int height,
                                                        final int marginTop, final int marginBottom) {
        final TextInputLayout.LayoutParams textInputLayoutLayoutParams =
                new TextInputLayout.LayoutParams(width, height);
        textInputLayoutLayoutParams.topMargin = marginTop;
        textInputLayoutLayoutParams.bottomMargin = marginBottom;

        final TextInputLayout textInputLayout = new TextInputLayout(activity);
        textInputLayout.setLayoutParams(textInputLayoutLayoutParams);

        return textInputLayout;
    }
}
