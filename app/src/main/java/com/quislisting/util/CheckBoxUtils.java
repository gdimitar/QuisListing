package com.quislisting.util;

import android.support.v7.app.AppCompatActivity;
import android.widget.CheckBox;
import android.widget.LinearLayout;

public class CheckBoxUtils {

    public CheckBoxUtils() {

    }

    public static CheckBox createAppCompatButton(final AppCompatActivity activity,
                                                 final int width, final int height,
                                                 final int weight, final int id) {
        final LinearLayout.LayoutParams checkBoxLayoutParams = new LinearLayout.LayoutParams(width, height);
        checkBoxLayoutParams.weight = weight;

        final CheckBox checkBox = new CheckBox(activity);
        checkBox.setLayoutParams(checkBoxLayoutParams);
        checkBox.setId(id);

        return checkBox;
    }
}
