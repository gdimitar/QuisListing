package com.quislisting.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
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
import android.widget.Toast;

import com.quislisting.R;
import com.quislisting.adapter.NothingSelectedSpinnerAdapter;
import com.quislisting.dto.LocationDTO;
import com.quislisting.model.BaseCategory;
import com.quislisting.retrofit.APIInterface;
import com.quislisting.retrofit.impl.APIClient;
import com.quislisting.util.BaseCategoryFilterUtil;
import com.quislisting.util.ButtonUtils;
import com.quislisting.util.CollectionUtils;
import com.quislisting.util.ConnectionChecker;
import com.quislisting.util.CustomArrayAdapterUtil;
import com.quislisting.util.SearchResultUtil;
import com.quislisting.util.SpinnerUtils;
import com.quislisting.util.StringUtils;
import com.quislisting.util.TextViewUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {

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

    private APIInterface apiInterface;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        ButterKnife.bind(this);

        if (ConnectionChecker.isOnline()) {
            apiInterface = APIClient.getClient().create(APIInterface.class);

            final Call<Collection<BaseCategory>> baseCategoriesCall = apiInterface.getBaseCategories();

            baseCategoriesCall.enqueue(new Callback<Collection<BaseCategory>>() {
                @Override
                public void onResponse(final Call<Collection<BaseCategory>> call,
                                       final Response<Collection<BaseCategory>> response) {
                    if (response.isSuccessful() && CollectionUtils.isNotEmpty(response.body())) {
                        parentCategories = BaseCategoryFilterUtil.filterParentCategories(response.body());
                        categoriesWithParent = BaseCategoryFilterUtil.filterChildCategories(response.body(), null);

                        categorySpinner = SpinnerUtils.createSpinner(getApplicationContext(), R.id.category,
                                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT,
                                getString(R.string.category), 15, 15);
                        searchLayout.addView(categorySpinner);

                        parentCategories.add(0, new BaseCategory(getString(R.string.all)));
                        CustomArrayAdapterUtil.prepareCustomCategoryArrayAdapter(getApplicationContext(), parentCategories,
                                R.layout.category_spinner_row_nothing_selected, categorySpinner);
                        categorySpinner.setOnItemSelectedListener(categoryListener);
                        categorySpinner.setOnTouchListener(categoryOnTouchListener);
                    } else {
                        Toast.makeText(getApplicationContext(), getString(R.string.retrievecategoryerror),
                                Toast.LENGTH_SHORT).show();
                        handleError();
                    }
                }

                @Override
                public void onFailure(final Call<Collection<BaseCategory>> call, final Throwable t) {
                    call.cancel();
                    Toast.makeText(getApplicationContext(), getString(R.string.noconnection),
                            Toast.LENGTH_SHORT).show();
                    handleError();
                }
            });

            final SharedPreferences sharedPreferences = getSharedPreferences("com.quislisting",
                    Context.MODE_PRIVATE);
            language = sharedPreferences.getString("language", "en");
            final Call<Collection<LocationDTO>> getCountriesCall = apiInterface.getLocations(0L, language);

            getCountriesCall.enqueue(new Callback<Collection<LocationDTO>>() {
                @Override
                public void onResponse(final Call<Collection<LocationDTO>> call,
                                       final Response<Collection<LocationDTO>> response) {
                    final boolean apiResponse = response.isSuccessful() && CollectionUtils.isNotEmpty(response.body());
                    if (apiResponse) {
                        handleDropDownMenus(response.body());
                    } else {
                        if (noSearchCriteria == null) {
                            handleError();
                        }
                        Toast.makeText(getApplicationContext(), getString(R.string.retrievelocationerror),
                                Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(final Call<Collection<LocationDTO>> call, final Throwable t) {
                    call.cancel();
                    if (noSearchCriteria == null) {
                        handleError();
                    }
                    Toast.makeText(getApplicationContext(), getString(R.string.retrievelocationerror),
                            Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.noconnection),
                    Toast.LENGTH_SHORT).show();
            handleError();
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

                final Call<Collection<LocationDTO>> getStatesCall = apiInterface.getLocations(country.getId(), language);
                getStatesCall.enqueue(new Callback<Collection<LocationDTO>>() {
                    @Override
                    public void onResponse(final Call<Collection<LocationDTO>> call,
                                           final Response<Collection<LocationDTO>> response) {
                        if (response.isSuccessful() && CollectionUtils.isNotEmpty(response.body())) {
                            final List<LocationDTO> newStates = new ArrayList<>(response.body());
                            newStates.add(0, new LocationDTO(getString(R.string.all)));
                            CustomArrayAdapterUtil.prepareCustomLocationArrayAdapter(getApplicationContext(), newStates,
                                    R.layout.state_spinner_row_nothing_selected, stateSpinner);
                        } else {
                            Toast.makeText(getApplicationContext(), getString(R.string.retrievelocationerror),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(final Call<Collection<LocationDTO>> call, final Throwable t) {
                        call.cancel();
                        Toast.makeText(getApplicationContext(), getString(R.string.retrieveusererror),
                                Toast.LENGTH_SHORT).show();
                    }
                });
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

                final Call<Collection<LocationDTO>> getCitiesCall = apiInterface.getLocations(state.getId(), language);
                getCitiesCall.enqueue(new Callback<Collection<LocationDTO>>() {
                    @Override
                    public void onResponse(final Call<Collection<LocationDTO>> call,
                                           final Response<Collection<LocationDTO>> response) {
                        if (CollectionUtils.isNotEmpty(response.body())) {
                            final List<LocationDTO> newCities = new ArrayList<>(response.body());
                            newCities.add(0, new LocationDTO(getString(R.string.all)));
                            CustomArrayAdapterUtil.prepareCustomLocationArrayAdapter(getApplicationContext(), newCities,
                                    R.layout.city_spinner_row_nothing_selected, citySpinner);
                        } else {
                            Toast.makeText(getApplicationContext(), getString(R.string.retrievelocationerror),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(final Call<Collection<LocationDTO>> call, final Throwable t) {
                        call.cancel();
                        Toast.makeText(getApplicationContext(), getString(R.string.retrieveusererror),
                                Toast.LENGTH_SHORT).show();
                    }
                });
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

    private void handleDropDownMenus(final Collection<LocationDTO> locations) {
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

    private void handleError() {
        noSearchCriteria = TextViewUtils.createTextView(getApplicationContext(), LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, R.id.no_search_criteria, R.string.nosearchcriteriaavailable);
        noSearchCriteria.setText(getString(R.string.nosearchcriteriaavailable));
        noSearchCriteria.setTextSize(16f);
        noSearchCriteria.setGravity(Gravity.CENTER);
        searchLayout.addView(noSearchCriteria);
    }
}
