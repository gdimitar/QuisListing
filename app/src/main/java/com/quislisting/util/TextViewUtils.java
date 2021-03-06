package com.quislisting.util;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TextViewUtils {

    public TextViewUtils() {

    }

    public static TextView createTextView(final Context context, final int width,
                                          final int height, final int id, final int hint) {
        final LinearLayout.LayoutParams textViewLayoutParams = new LinearLayout.LayoutParams(width, height);

        final TextView textView = new TextView(context);
        textView.setLayoutParams(textViewLayoutParams);
        textView.setId(id);
        textView.setHint(hint);

        return textView;
    }
}
