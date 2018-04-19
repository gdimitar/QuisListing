package com.quislisting.util;

import android.content.Context;
import android.widget.Spinner;

import com.quislisting.R;
import com.quislisting.adapter.CustomBaseCategoryArrayAdapter;
import com.quislisting.adapter.CustomLocationArrayAdapter;
import com.quislisting.adapter.NothingSelectedSpinnerAdapter;
import com.quislisting.dto.LocationDTO;
import com.quislisting.model.BaseCategory;

import java.util.List;

public class CustomArrayAdapterUtil {

    public CustomArrayAdapterUtil() {

    }

    public static void prepareCustomLocationArrayAdapter(final Context context, final List<LocationDTO> list,
                                                         final int layout, final Spinner spinner) {
        final CustomLocationArrayAdapter<LocationDTO> arrayAdapter =
                new CustomLocationArrayAdapter<>(context, list);
        arrayAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(new NothingSelectedSpinnerAdapter(arrayAdapter, layout, context));
    }

    public static void prepareCustomCategoryArrayAdapter(final Context context, final List<BaseCategory> list,
                                                         final int layout, final Spinner spinner) {
        final CustomBaseCategoryArrayAdapter<BaseCategory> arrayAdapter =
                new CustomBaseCategoryArrayAdapter<>(context, list);
        arrayAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(new NothingSelectedSpinnerAdapter(arrayAdapter, layout, context));
    }
}
