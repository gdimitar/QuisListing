package com.quislisting.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.quislisting.R;
import com.quislisting.adapter.ListingAdapter;
import com.quislisting.model.BaseListing;
import com.quislisting.model.Listing;
import com.quislisting.retrofit.APIInterface;
import com.quislisting.retrofit.impl.APIClient;
import com.quislisting.util.CollectionUtils;
import com.quislisting.util.JsonObjectPopulator;
import com.quislisting.util.StringUtils;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchResultActivity extends AppCompatActivity {

    @Bind(R.id.searchResultsView)
    ConstraintLayout searchResultsView;

    private ProgressDialog progressDialog;

    private String searchText;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        setTitle(getString(R.string.searchresultactivitytitle));

        ButterKnife.bind(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.fetchdata));
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        progressDialog.setCancelable(false);

        searchText = getIntent().getStringExtra("searchText");
        final String searchCategoryText = getFieldData("searchCategoryText");
        final String searchCountryText = getFieldData("searchCountryText");
        final String searchStateText = getFieldData("searchStateText");
        final String searchCityText = getFieldData("searchCityText");

        final SharedPreferences sharedPreferences = getSharedPreferences("com.quislisting",
                Context.MODE_PRIVATE);
        //TODO: verify this is working
        final String language = sharedPreferences.getString("language", "en");

        final APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);

        final JSONObject jsonObject = JsonObjectPopulator.prepareSearchResultJson(searchText, searchCategoryText,
                searchCountryText, searchStateText, searchCityText);

        Call<Collection<Listing>> searchListingsCall = null;
        try {
            searchListingsCall = apiInterface.searchListings(URLEncoder.encode(jsonObject.toString(), "UTF-8"),
                    language);
        } catch (final UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        assert searchListingsCall != null;
        searchListingsCall.enqueue(new Callback<Collection<Listing>>() {
            @Override
            public void onResponse(final Call<Collection<Listing>> call, final Response<Collection<Listing>> response) {
                if (response.isSuccessful() && CollectionUtils.isNotEmpty(response.body())) {
                    final List<BaseListing> listingList = new ArrayList<>(response.body());
                    final ListView listView = (ListView) findViewById(R.id.listView);
                    final ListingAdapter listingAdapter = new ListingAdapter(getApplicationContext(),
                            new ArrayList<>(response.body()));
                    listView.setVisibility(View.VISIBLE);
                    listView.setAdapter(listingAdapter);

                    listView.setOnItemClickListener((parent, view, position, id) -> {
                        final Intent intent = new Intent(getApplicationContext(),
                                ListingDetailsActivity.class);
                        intent.putExtra("listingId", listingList.get(position).getId().toString());
                        intent.putExtra("idToken", StringUtils.EMPTY_STRING);
                        startActivity(intent);
                    });
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.nolistingresults),
                            Toast.LENGTH_SHORT).show();
                    handleError();

                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(final Call<Collection<Listing>> call, final Throwable t) {
                call.cancel();
                Toast.makeText(getApplicationContext(), getString(R.string.noconnection),
                        Toast.LENGTH_SHORT).show();
                handleError();
            }
        });
    }

    private String getFieldData(final String fieldName) {
        return StringUtils.isNotEmpty(getIntent().getStringExtra(fieldName))
                ? getIntent().getStringExtra(fieldName) : StringUtils.EMPTY_STRING;
    }

    private Spanned getSpannedText(final String text) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(text, Html.FROM_HTML_MODE_COMPACT);
        } else {
            return Html.fromHtml(text);
        }
    }

    private void handleError() {
        searchResultsView.removeAllViews();
        setContentView(R.layout.empty_layout);

        final TextView noListingResultsText = (TextView) findViewById(R.id.emptyText);
        final String formattedNoListingResultText = String.format(getString(R.string.nolistingresults), searchText);
        noListingResultsText.setText(getSpannedText(formattedNoListingResultText));
    }
}
