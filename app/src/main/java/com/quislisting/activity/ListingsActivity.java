package com.quislisting.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.quislisting.R;
import com.quislisting.adapter.ListingAdapter;
import com.quislisting.model.Listing;
import com.quislisting.task.AsyncCollectionResponse;
import com.quislisting.task.RestRouter;
import com.quislisting.task.impl.HttpGetListingsRequestTask;
import com.quislisting.util.CollectionUtils;
import com.quislisting.util.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ListingsActivity extends AppCompatActivity implements AsyncCollectionResponse<Listing>,
        View.OnClickListener {

    private String idToken = null;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        idToken = getIntent().getStringExtra("idToken");
        if (StringUtils.isEmpty(idToken)) {
            final Intent intent = new Intent(this, LoginActivity.class);
            intent.putExtra("fromView", "listingsView");
            startActivity(intent);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listings);
        setTitle(getString(R.string.listingsactivitytitle));

        final SharedPreferences sharedPreferences = getSharedPreferences("com.quislisting",
                Context.MODE_PRIVATE);
        final String selectedLanguage = sharedPreferences.getString("language", null);

        if (StringUtils.isNotEmpty(idToken) && StringUtils.isNotEmpty(selectedLanguage)) {
            final HttpGetListingsRequestTask getListingsRequestTask =
                    new HttpGetListingsRequestTask();
            getListingsRequestTask.delegate = this;
            getListingsRequestTask.execute(String.format(RestRouter.Listing.GET_LISTINGS,
                    selectedLanguage), idToken);
        }
    }

    @Override
    public void processFinish(final Collection<Listing> listings) {
        if (CollectionUtils.isNotEmpty(listings)) {
            final List<Listing> listingList = new ArrayList<>(listings);
            final ListView listView = (ListView) findViewById(R.id.listView);
            final ListingAdapter listingAdapter = new ListingAdapter(this,
                    new ArrayList<>(listings));
            listView.setVisibility(View.VISIBLE);
            listView.setAdapter(listingAdapter);

            listView.setOnItemClickListener((parent, view, position, id) -> {
                final Intent intent = new Intent(this,
                        ListingDetailsActivity.class);
                intent.putExtra("listingId", listingList.get(position).getId().toString());
                intent.putExtra("idToken", this.idToken);
                startActivity(intent);
            });
        } else {
            final TextView noListingText = (TextView) findViewById(R.id.noListingsText);
            final TextView addListingText = (TextView) findViewById(R.id.addListingText);

            noListingText.setVisibility(View.VISIBLE);
            addListingText.setVisibility(View.VISIBLE);

            addListingText.setOnClickListener(this);
        }
    }

    @Override
    public void onBackPressed() {
        final Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("idToken", this.idToken);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                final Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("idToken", this.idToken);
                startActivity(intent);
                break;
        }
        return true;
    }

    @Override
    public void onClick(final View view) {
        switch (view.getId()) {
            case R.id.addListingText:
                final Intent intent = new Intent(this, AddListingActivity.class);
                startActivity(intent);
                break;
        }
    }
}
