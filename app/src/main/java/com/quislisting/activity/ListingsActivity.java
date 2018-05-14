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
import android.widget.Toast;

import com.quislisting.R;
import com.quislisting.adapter.ListingAdapter;
import com.quislisting.model.Listing;
import com.quislisting.retrofit.APIInterface;
import com.quislisting.retrofit.impl.APIClient;
import com.quislisting.util.CollectionUtils;
import com.quislisting.util.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListingsActivity extends AppCompatActivity implements View.OnClickListener {

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

        final APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);

        final Call<Collection<Listing>> getListingsCall = apiInterface.getListings(selectedLanguage,
                "Bearer " + idToken);

        getListingsCall.enqueue(new Callback<Collection<Listing>>() {
            @Override
            public void onResponse(final Call<Collection<Listing>> call,
                                   final Response<Collection<Listing>> response) {
                if (response.isSuccessful() && CollectionUtils.isNotEmpty(response.body())) {
                    final List<Listing> listingList = new ArrayList<>(response.body());
                    final ListView listView = (ListView) findViewById(R.id.listView);
                    final ListingAdapter listingAdapter = new ListingAdapter(getApplicationContext(),
                            getResources(), new ArrayList<>(response.body()));
                    listView.setVisibility(View.VISIBLE);
                    listView.setAdapter(listingAdapter);

                    listView.setOnItemClickListener((parent, view, position, id) -> {
                        final Intent intent = new Intent(getApplicationContext(),
                                ListingDetailsActivity.class);
                        intent.putExtra("listingId", listingList.get(position).getId().toString());
                        intent.putExtra("idToken", idToken);
                        startActivity(intent);
                    });
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.nolistings),
                            Toast.LENGTH_SHORT).show();
                    handleError();
                }
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

    private void handleError() {
        setContentView(R.layout.empty_listings_layout);

        final TextView addListingText = (TextView) findViewById(R.id.addListingText);
        addListingText.setOnClickListener(view -> {
            final Intent intent = new Intent(getApplicationContext(), AddListingActivity.class);
            startActivity(intent);
        });
    }
}
