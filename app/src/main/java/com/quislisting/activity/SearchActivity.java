package com.quislisting.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.quislisting.R;
import com.quislisting.adapter.NothingSelectedSpinnerAdapter;
import com.quislisting.converter.JsonConverter;
import com.quislisting.dto.LocationDTO;
import com.quislisting.model.BaseCategory;
import com.quislisting.task.AsyncCollectionResponse;
import com.quislisting.task.RestRouter;
import com.quislisting.task.handler.HttpHandler;
import com.quislisting.task.impl.HttpGetCategoriesRequestTask;
import com.quislisting.util.BaseCategoryFilterUtil;
import com.quislisting.util.ButtonUtils;
import com.quislisting.util.CollectionUtils;
import com.quislisting.util.CustomArrayAdapterUtil;
import com.quislisting.util.SearchResultUtil;
import com.quislisting.util.SpinnerUtils;
import com.quislisting.util.StringUtils;
import com.quislisting.util.TextViewUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SearchActivity extends AppCompatActivity implements AsyncCollectionResponse<BaseCategory> {

    private static final String TAG = SearchActivity.class.getSimpleName();

    @Bind(R.id.searchLayout)
    LinearLayout searchLayout;
    @Bind(R.id.searchText)
    EditText searchText;

    private TextView noSearchCriteria;

    private List<BaseCategory> parentCategories;
    private List<BaseCategory> categoriesWithParent;
    private final List<LocationDTO> states = new ArrayList<>();
    private final List<LocationDTO> cities = new ArrayList<>();
    private String language;

    private Spinner categorySpinner, countrySpinner, stateSpinner, citySpinner;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        ButterKnife.bind(this);

        final HttpGetCategoriesRequestTask getCategoriesRequestTask =
                new HttpGetCategoriesRequestTask();
        getCategoriesRequestTask.delegate = this;
        getCategoriesRequestTask.execute(RestRouter.Category.GET_ALL_CATEGORIES);
    }

    @Override
    public void processFinish(final Collection<BaseCategory> categories) {
        if (CollectionUtils.isNotEmpty(categories)) {
            parentCategories = BaseCategoryFilterUtil.filterParentCategories(categories);
            categoriesWithParent = BaseCategoryFilterUtil.filterChildCategories(categories, null);

            categorySpinner = SpinnerUtils.createSpinner(this, R.id.category,
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT,
                    getString(R.string.category), 15, 15);
            searchLayout.addView(categorySpinner);

            parentCategories.add(0, new BaseCategory(getString(R.string.all)));
            CustomArrayAdapterUtil.prepareCustomCategoryArrayAdapter(this, parentCategories,
                    R.layout.category_spinner_row_nothing_selected, categorySpinner);
            categorySpinner.setOnItemSelectedListener(categoryListener);
            categorySpinner.setOnTouchListener(categoryOnTouchListener);
        } else {
            noSearchCriteria = TextViewUtils.createTextView(this, LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT, R.id.no_search_criteria, R.string.nosearchcriteriaavailable);
            noSearchCriteria.setText(getString(R.string.nosearchcriteriaavailable));
            noSearchCriteria.setTextSize(16f);
            noSearchCriteria.setGravity(Gravity.CENTER);
            searchLayout.addView(noSearchCriteria);
        }

        final SharedPreferences sharedPreferences = getSharedPreferences("com.quislisting",
                Context.MODE_PRIVATE);
        language = sharedPreferences.getString("language", "en");

        final AsyncTask<String, Void, Collection<LocationDTO>> task = new RetrieveLocationsTask()
                .execute(String.format(RestRouter.Location.GET_ALL_LOCATIONS,
                        0, language));
        final List<LocationDTO> countries;
        try {
            countries = new ArrayList<>(task.get());
            handleDropDownMenus(countries);
        } catch (final InterruptedException e) {
            Log.e(TAG, "InterruptedException during the HTTP request.", e);
        } catch (final ExecutionException e) {
            Log.e(TAG, "ExecutionException during the HTTP request.", e);
        }
    }

    @Override
    public void onBackPressed() {
        final Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                final Intent intentHome = new Intent(this, MainActivity.class);
                startActivity(intentHome);
                break;
        }
        return true;
    }

    private final AdapterView.OnItemSelectedListener categoryListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(final AdapterView<?> parent, final View view, final int position,
                                   final long id) {
            if (position > 0 && position != 1) {
                final BaseCategory parentCategory = (BaseCategory) categorySpinner.getItemAtPosition(position);
                final List<BaseCategory> childCategories = BaseCategoryFilterUtil.filterChildCategories(categoriesWithParent,
                        parentCategory.getId());
                childCategories.add(0, new BaseCategory(getString(R.string.all)));

                final View mView = getLayoutInflater().inflate(R.layout.dialog_spinner, null);

                final Spinner childCategorySpinner = (Spinner) mView.findViewById(R.id.spinner);
                CustomArrayAdapterUtil.prepareCustomCategoryArrayAdapter(parent.getContext(), childCategories,
                        R.layout.category_spinner_row_nothing_selected, childCategorySpinner);

                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(parent.getContext());
                alertDialogBuilder.setView(mView);
                alertDialogBuilder.setPositiveButton(getString(R.string.okdialog), (dialog, which) -> {
                    final BaseCategory expected = (BaseCategory) childCategorySpinner.getSelectedItem();
                    final NothingSelectedSpinnerAdapter adapter = (NothingSelectedSpinnerAdapter) childCategorySpinner.getAdapter();
                    for (int i = 0; i < adapter.getCount(); i++) {
                        final BaseCategory actual = (BaseCategory) adapter.getItem(i);
                        if ((actual != null && StringUtils.isNotEmpty(actual.getName()))
                                && expected.getName().equals(actual.getName())) {
                            final List<BaseCategory> values = new ArrayList<>();
                            values.add(0, new BaseCategory(expected.getName()));
                            CustomArrayAdapterUtil.prepareCustomCategoryArrayAdapter(getApplicationContext(), values,
                                    R.layout.category_spinner_row_nothing_selected, categorySpinner);
                            categorySpinner.setSelection(1);
                        }
                    }
                });
                alertDialogBuilder.setNegativeButton(getString(R.string.canceldialog), (dialog, which) -> dialog.dismiss());
                final AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            } else if (position == 1) {
                categorySpinner.setSelection(1);
            }
        }

        @Override
        public void onNothingSelected(final AdapterView<?> parent) {

        }
    };

    private final View.OnTouchListener categoryOnTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(final View v, final MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                CustomArrayAdapterUtil.prepareCustomCategoryArrayAdapter(getApplicationContext(), parentCategories,
                        R.layout.category_spinner_row_nothing_selected, categorySpinner);
            }

            return false;
        }
    };

    private final AdapterView.OnItemSelectedListener countryListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(final AdapterView<?> parent, final View view, final int position,
                                   final long id) {
            if (position > 0 && position != 1) {
                final LocationDTO country = (LocationDTO) countrySpinner.getItemAtPosition(position);
                final AsyncTask<String, Void, Collection<LocationDTO>> task = new RetrieveLocationsTask()
                        .execute(String.format(RestRouter.Location.GET_ALL_LOCATIONS,
                                country.getId(), language));
                List<LocationDTO> newStates = new ArrayList<>();
                try {
                    newStates = new ArrayList<>(task.get());
                } catch (final InterruptedException e) {
                    Log.e(TAG, "InterruptedException during the HTTP request.", e);
                } catch (final ExecutionException e) {
                    Log.e(TAG, "ExecutionException during the HTTP request.", e);
                }

                newStates.add(0, new LocationDTO(getString(R.string.all)));
                CustomArrayAdapterUtil.prepareCustomLocationArrayAdapter(getApplicationContext(), newStates,
                        R.layout.state_spinner_row_nothing_selected, stateSpinner);
            } else if (position == 1) {
                states.add(new LocationDTO(getString(R.string.all)));
                CustomArrayAdapterUtil.prepareCustomLocationArrayAdapter(getApplicationContext(), states,
                        R.layout.state_spinner_row_nothing_selected, stateSpinner);
                stateSpinner.setSelection(1);
            }
            CustomArrayAdapterUtil.prepareCustomLocationArrayAdapter(getApplicationContext(), cities,
                    R.layout.city_spinner_row_nothing_selected, citySpinner);
            if (position == 1) {
                citySpinner.setSelection(1);
            }
        }

        @Override
        public void onNothingSelected(final AdapterView<?> parent) {

        }
    };

    private final AdapterView.OnItemSelectedListener stateListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(final AdapterView<?> parent, final View view, final int position,
                                   final long id) {
            if (position > 0 && position != 1) {
                final LocationDTO state = (LocationDTO) stateSpinner.getItemAtPosition(position);
                final AsyncTask<String, Void, Collection<LocationDTO>> task = new RetrieveLocationsTask()
                        .execute(String.format(RestRouter.Location.GET_ALL_LOCATIONS,
                                state.getId(), language));
                List<LocationDTO> newCities = new ArrayList<>();
                try {
                    newCities = new ArrayList<>(task.get());
                } catch (final InterruptedException e) {
                    Log.e(TAG, "InterruptedException during the HTTP request.", e);
                } catch (final ExecutionException e) {
                    Log.e(TAG, "ExecutionException during the HTTP request.", e);
                }
                newCities.add(0, new LocationDTO(getString(R.string.all)));
                CustomArrayAdapterUtil.prepareCustomLocationArrayAdapter(getApplicationContext(), newCities,
                        R.layout.city_spinner_row_nothing_selected, citySpinner);
            } else if (position == 1) {
                citySpinner.setSelection(1);
            }
        }

        @Override
        public void onNothingSelected(final AdapterView<?> parent) {

        }
    };

    private final AdapterView.OnItemSelectedListener cityListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(final AdapterView<?> parent, final View view, final int position,
                                   final long id) {
        }

        @Override
        public void onNothingSelected(final AdapterView<?> parent) {

        }
    };

    static class RetrieveLocationsTask extends AsyncTask<String, Void, Collection<LocationDTO>> {

        @Override
        protected Collection<LocationDTO> doInBackground(final String... params) {
            return getChildLocations(params[0]);
        }

        @Override
        protected void onPostExecute(final Collection<LocationDTO> result) {
            super.onPostExecute(result);
        }

        private List<LocationDTO> getChildLocations(final String urlString) {
            final HttpHandler httpHandler = new HttpHandler();
            final URL url = httpHandler.createUrl(urlString);

            try {
                final String jsonResponse = httpHandler.makeHttpGetRequest(url, null, false);

                if (TextUtils.isEmpty(jsonResponse)) {
                    return null;
                }

                final JsonConverter<LocationDTO> jsonConverter = new JsonConverter<>();
                return new ArrayList<>(jsonConverter.extractCollectionFromJson(jsonResponse, LocationDTO.class,
                        false));
            } catch (final IOException e) {
                Log.e(TAG, "Problem making the HTTP request.", e);
            }

            return null;
        }
    }

    private void handleDropDownMenus(final List<LocationDTO> locations) {
        if (CollectionUtils.isNotEmpty(locations)) {
            countrySpinner = SpinnerUtils.createSpinner(this, R.id.country,
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT,
                    getString(R.string.country), 15, 15);
            searchLayout.addView(countrySpinner);

            stateSpinner = SpinnerUtils.createSpinner(this, R.id.state,
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT,
                    getString(R.string.state), 15, 15);
            searchLayout.addView(stateSpinner);

            citySpinner = SpinnerUtils.createSpinner(this, R.id.city,
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT,
                    getString(R.string.city), 15, 15);
            searchLayout.addView(citySpinner);

            final List<LocationDTO> countries = new ArrayList<>(locations);
            countries.add(0, new LocationDTO(getString(R.string.all)));
            CustomArrayAdapterUtil.prepareCustomLocationArrayAdapter(this, countries,
                    R.layout.country_spinner_row_nothing_selected, countrySpinner);

            states.add(0, new LocationDTO(getString(R.string.all)));
            CustomArrayAdapterUtil.prepareCustomLocationArrayAdapter(this, states,
                    R.layout.state_spinner_row_nothing_selected, stateSpinner);

            cities.add(0, new LocationDTO(getString(R.string.all)));
            CustomArrayAdapterUtil.prepareCustomLocationArrayAdapter(this, cities,
                    R.layout.city_spinner_row_nothing_selected, citySpinner);

            countrySpinner.setOnItemSelectedListener(countryListener);
            stateSpinner.setOnItemSelectedListener(stateListener);
            citySpinner.setOnItemSelectedListener(cityListener);
        } else {
            if (noSearchCriteria.getVisibility() != View.VISIBLE) {
                noSearchCriteria = TextViewUtils.createTextView(this, LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT, R.id.no_search_criteria, R.string.nosearchcriteriaavailable);
                noSearchCriteria.setText(getString(R.string.nosearchcriteriaavailable));
                noSearchCriteria.setTextSize(16f);
                noSearchCriteria.setGravity(Gravity.CENTER);
                searchLayout.addView(noSearchCriteria);
            }
        }

        final AppCompatButton searchButton = ButtonUtils.createAppCompatButton(this,
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT,
                R.id.search, 20, 20, R.string.search);
        searchButton.setOnClickListener(v -> {
            final Intent intent = new Intent(getApplicationContext(), SearchResultActivity.class);
            intent.putExtra("searchText", searchText.getText() != null
                    ? searchText.getText().toString() : StringUtils.EMPTY_STRING);
            intent.putExtra("searchCategoryText", new SearchResultUtil<>(BaseCategory.class)
                    .getSelectedData((BaseCategory) categorySpinner.getSelectedItem()));
            intent.putExtra("searchCountryText", new SearchResultUtil<>(LocationDTO.class)
                    .getSelectedData((LocationDTO) countrySpinner.getSelectedItem()));
            intent.putExtra("searchStateText", new SearchResultUtil<>(LocationDTO.class)
                    .getSelectedData((LocationDTO) stateSpinner.getSelectedItem()));
            intent.putExtra("searchCityText", new SearchResultUtil<>(LocationDTO.class)
                    .getSelectedData((LocationDTO) citySpinner.getSelectedItem()));
            startActivity(intent);
        });
        searchLayout.addView(searchButton);
    }
}
