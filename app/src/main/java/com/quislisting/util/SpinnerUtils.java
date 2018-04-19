package com.quislisting.util;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.Spinner;

public class SpinnerUtils {

    public SpinnerUtils() {

    }

    public static Spinner createSpinner(final Context context, final int id, final int width,
                                        final int height, final String prompt, final int marginTop,
                                        final int marginBottom) {
        final LinearLayout.LayoutParams spinnerLayoutParams = new LinearLayout.LayoutParams(width, height);
        spinnerLayoutParams.topMargin = marginTop;
        spinnerLayoutParams.bottomMargin = marginBottom;

        final Spinner spinner = new Spinner(context);
        spinner.setLayoutParams(spinnerLayoutParams);
        spinner.setPrompt(prompt);

        return spinner;
    }
}
