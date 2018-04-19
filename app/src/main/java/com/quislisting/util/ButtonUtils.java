package com.quislisting.util;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.widget.LinearLayout;

public class ButtonUtils {

    public ButtonUtils() {

    }

    public static AppCompatButton createAppCompatButton(final AppCompatActivity activity,
                                                        final int width, final int height,
                                                        final int id, final int marginTop,
                                                        final int marginBottom, final int text) {
        final LinearLayout.LayoutParams buttonLayoutParams = new LinearLayout.LayoutParams(width, height);
        buttonLayoutParams.topMargin = marginTop;
        buttonLayoutParams.bottomMargin = marginBottom;

        final AppCompatButton appCompatButton = new AppCompatButton(activity);
        appCompatButton.setLayoutParams(buttonLayoutParams);
        appCompatButton.setId(id);
        appCompatButton.setText(text);

        return appCompatButton;
    }
}
