package com.quislisting.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.quislisting.R;
import com.quislisting.adapter.ListingAdapter;
import com.quislisting.model.BaseListing;
import com.quislisting.task.AsyncCollectionResponse;
import com.quislisting.task.RestRouter;
import com.quislisting.task.impl.HttpGetListingResultsRequestTask;
import com.quislisting.util.CollectionUtils;
import com.quislisting.util.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SearchResultActivity extends AppCompatActivity implements AsyncCollectionResponse<BaseListing> {

    private ProgressDialog progressDialog;

    private String searchText;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        setTitle(getString(R.string.searchresultactivitytitle));

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
        final String language = sharedPreferences.getString("language", "en");

        final HttpGetListingResultsRequestTask getBaseListingsRequestTask =
                new HttpGetListingResultsRequestTask();
        getBaseListingsRequestTask.delegate = this;
        getBaseListingsRequestTask.execute(RestRouter.Listing.SEARCH_LISTINGS,
                searchText, searchCategoryText, searchCountryText, searchStateText, searchCityText, language);
    }

    @Override
    public void processFinish(final Collection<BaseListing> baseListings) {
        if (CollectionUtils.isNotEmpty(baseListings)) {
            final List<BaseListing> listingList = new ArrayList<>(baseListings);
            final ListView listView = (ListView) findViewById(R.id.listView);
            final ListingAdapter listingAdapter = new ListingAdapter(this,
                    new ArrayList<>(baseListings));
            listView.setVisibility(View.VISIBLE);
            listView.setAdapter(listingAdapter);

            listView.setOnItemClickListener((parent, view, position, id) -> {
                final Intent intent = new Intent(this,
                        ListingDetailsActivity.class);
                intent.putExtra("listingId", listingList.get(position).getId().toString());
                intent.putExtra("idToken", StringUtils.EMPTY_STRING);
                startActivity(intent);
            });
        } else {
            final ImageView noListingResultsImage = (ImageView) findViewById(R.id.noListingResultsImage);
            noListingResultsImage.setVisibility(View.VISIBLE);

            final TextView noListingResultsText = (TextView) findViewById(R.id.noListingResultsText);
            final String formattedNoListingResultText = String.format(getString(R.string.nolistingresults), searchText);
            noListingResultsText.setText(getSpannedText(formattedNoListingResultText));
            noListingResultsText.setVisibility(View.VISIBLE);
        }

        progressDialog.dismiss();
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
}
