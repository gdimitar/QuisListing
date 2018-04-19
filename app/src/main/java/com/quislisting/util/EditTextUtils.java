package com.quislisting.util;

import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;

public class EditTextUtils {

    public EditTextUtils() {

    }

    public static EditText createEditText(final AppCompatActivity activity, final String hint,
                                          final int width, final int height, final int id,
                                          final int inputType, final boolean isEmailField,
                                          final int maxLength, final int minLines, final int maxLines) {
        final EditText editText = new EditText(activity);
        editText.setId(id);
        editText.setInputType(inputType);
        editText.setHint(hint);
        if (isEmailField) {
            final LinearLayout.LayoutParams linearLayoutParams = new LinearLayout.LayoutParams(width, height, 1f);
            editText.setLayoutParams(linearLayoutParams);
            editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
            editText.setMinLines(minLines);
            editText.setMaxLines(maxLines);
            editText.setSingleLine(false);
            editText.setImeOptions(EditorInfo.IME_FLAG_NO_ENTER_ACTION);
        } else {
            final LinearLayout.LayoutParams editTextLayoutParams = new LinearLayout.LayoutParams(width, height);
            editText.setLayoutParams(editTextLayoutParams);
        }

        return editText;
    }
}
