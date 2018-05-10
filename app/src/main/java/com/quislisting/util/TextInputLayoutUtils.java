package com.quislisting.util;

import android.content.Context;
import android.support.design.widget.TextInputLayout;

public class TextInputLayoutUtils {

    public TextInputLayoutUtils() {

    }

    public static TextInputLayout createTextInputLayout(final Context context,
                                                        final int width, final int height,
                                                        final int marginTop, final int marginBottom) {
        final TextInputLayout.LayoutParams textInputLayoutLayoutParams =
                new TextInputLayout.LayoutParams(width, height);
        textInputLayoutLayoutParams.topMargin = marginTop;
        textInputLayoutLayoutParams.bottomMargin = marginBottom;

        final TextInputLayout textInputLayout = new TextInputLayout(context);
        textInputLayout.setLayoutParams(textInputLayoutLayoutParams);

        return textInputLayout;
    }
}
