package com.quislisting.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.ExpandableDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.quislisting.QuisListingApplication;
import com.quislisting.R;
import com.quislisting.adapter.ListingAdapter;
import com.quislisting.model.BaseListing;
import com.quislisting.model.User;
import com.quislisting.retrofit.APIInterface;
import com.quislisting.retrofit.impl.APIClient;
import com.quislisting.service.SignoutService;
import com.quislisting.util.CollectionUtils;
import com.quislisting.util.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private Drawer result = null;

    private String idToken = null;

    private ProgressDialog progressDialog;

    private APIInterface apiInterface;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        idToken = getIntent().getStringExtra("idToken");

        final SharedPreferences sharedPreferences = getSharedPreferences("com.quislisting",
                Context.MODE_PRIVATE);
        final String defaultLanguage = sharedPreferences.getString("language", null);

        if (StringUtils.isEmpty(defaultLanguage)) {
            setLocale("en");
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(getString(R.string.mainactivitytitle));

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.fetchdata));
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        progressDialog.setCancelable(false);

        final String selectedLanguage = sharedPreferences.getString("language", null);
        if (StringUtils.isNotEmpty(selectedLanguage)) {
            apiInterface = APIClient.getClient().create(APIInterface.class);

            final Call<Collection<BaseListing>> getBaseListingsCall = apiInterface.getBaseListings(selectedLanguage);

            getBaseListingsCall.enqueue(new Callback<Collection<BaseListing>>() {
                @Override
                public void onResponse(final Call<Collection<BaseListing>> call,
                                       final Response<Collection<BaseListing>> response) {
                    if (response.isSuccessful() && CollectionUtils.isNotEmpty(response.body())) {
                        final List<BaseListing> listingList = new ArrayList<>(response.body());
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
                    progressDialog.dismiss();
                }

                @Override
                public void onFailure(final Call<Collection<BaseListing>> call, final Throwable t) {
                    call.cancel();
                    Toast.makeText(getApplicationContext(), getString(R.string.noconnection),
                            Toast.LENGTH_SHORT).show();
                    handleError();
                }
            });
        }

        // create toolbar
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // create the drawer
        result = new DrawerBuilder().withActivity(this).withToolbar(toolbar)
                .withSavedInstance(savedInstanceState).build();

        final Call<User> getUserCall = apiInterface.getUser("Bearer " + idToken);

        getUserCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(final Call<User> call, final Response<User> response) {
                prepareDrawer(result, idToken, response.body());
            }

            @Override
            public void onFailure(final Call<User> call, final Throwable t) {
                call.cancel();
                Toast.makeText(getApplicationContext(), getString(R.string.retrieveusererror),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                final Intent intent = new Intent(this, SearchActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (StringUtils.isNotEmpty(getIntent().getStringExtra("idToken"))) {
            getIntent().removeExtra("idToken");
        }

        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setMessage(getString(R.string.closeapp))
                .setPositiveButton(getString(R.string.closeappyes), (dialog, which) -> finish())
                .setNegativeButton(getString(R.string.closeappno), null)
                .show();
    }

    private void setLocale(final String language) {
        final SharedPreferences sharedPreferences = getSharedPreferences("com.quislisting",
                Context.MODE_PRIVATE);
        sharedPreferences.edit().putString("language", language).apply();

        final Locale myLocale = new Locale(language);
        final Resources res = getResources();
        final DisplayMetrics dm = res.getDisplayMetrics();
        final Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        finish();
        startActivity(getIntent());
    }

    private void prepareDrawer(final Drawer drawer, final String idToken, final User user) {
        if (StringUtils.isEmpty(idToken) || user == null) {
            final PrimaryDrawerItem signinDrawerItem = preparePrimaryDrawerItem(R.string.signin,
                    R.drawable.account, "mainView", LoginActivity.class);
            drawer.addItemAtPosition(signinDrawerItem, 0);
        }

        final SecondaryDrawerItem addListingDrawerItem = prepareSecondaryDrawerItem(R.string.addlisting,
                R.drawable.addlisting, "addListingView", AddListingActivity.class, false);
        final SecondaryDrawerItem listingsDrawerItem = prepareSecondaryDrawerItem(R.string.listings,
                R.drawable.listings, "listingsView", ListingsActivity.class, false);
        final SecondaryDrawerItem messagesDrawerItem = prepareSecondaryDrawerItem(R.string.messagecenter,
                R.drawable.message, "messagesView", MessagesActivity.class, false);
        final ExpandableDrawerItem languageDrawerItem = prepareExpandableDrawerItem(getString(R.string.language),
                R.drawable.language, getString(R.string.english), R.drawable.gb,
                getString(R.string.bulgarian), R.drawable.bg, getString(R.string.romanian),
                R.drawable.ro);

        drawer.addItems(addListingDrawerItem, listingsDrawerItem, messagesDrawerItem,
                languageDrawerItem);

        if (StringUtils.isNotEmpty(idToken) && user != null) {
            ((QuisListingApplication) this.getApplication()).setName(user.getFirstName() +
                    StringUtils.SEPARATOR + user.getLastName());
            ((QuisListingApplication) this.getApplication()).setEmail(user.getLogin());
            final Intent serviceIntent = new Intent(this, SignoutService.class);
            startService(serviceIntent);
            final AccountHeader accountHeader = prepareAccountHeader(user, R.color.black);
            result.setHeader(accountHeader.getView());
            accountHeader.setDrawer(result);

            final SecondaryDrawerItem profileDrawer = prepareSecondaryDrawerItem(R.string.profile,
                    R.drawable.profile, null, ProfileActivity.class, false);

            final SecondaryDrawerItem signoutDrawer = prepareSecondaryDrawerItem(R.string.signout,
                    R.drawable.signout, null, null, true);

            result.addItems(profileDrawer, signoutDrawer);
        }
    }

    private PrimaryDrawerItem preparePrimaryDrawerItem(final int nameId, final int iconId,
                                                       final String fromView, final Class<?> cls) {
        return new PrimaryDrawerItem().withName(nameId).withIcon(iconId)
                .withOnDrawerItemClickListener((view, position, drawerItem) -> {
                    final Intent intent = new Intent(this, cls);
                    intent.putExtra("fromView", fromView);
                    startActivity(intent);
                    return false;
                });
    }

    private SecondaryDrawerItem prepareSecondaryDrawerItem(final int nameId, final int iconId,
                                                           final String fromView, final Class<?> cls,
                                                           final boolean isSignoutDrawer) {
        return new SecondaryDrawerItem().withName(nameId).withIcon(iconId)
                .withOnDrawerItemClickListener((view, position, drawerItem) ->
                {
                    if (isSignoutDrawer) {
                        final Intent intent = getIntent();
                        intent.removeExtra("idToken");
                        finish();
                        startActivity(intent);
                    } else {
                        final Intent intent = new Intent(this, cls);
                        intent.putExtra("fromView", fromView);
                        intent.putExtra("idToken", this.idToken);
                        startActivity(intent);
                    }
                    return false;
                });
    }

    private ExpandableDrawerItem prepareExpandableDrawerItem(final String name, final int iconId,
                                                             final String englishText, final int englishIconId,
                                                             final String bulgarianText, final int bulgarianIconId,
                                                             final String romanianText, final int romanianIconId) {
        return new ExpandableDrawerItem().withName(name).withIcon(iconId).withSelectable(false).withSubItems(
                new SecondaryDrawerItem().withIcon(englishIconId).withName(englishText).withLevel(2)
                        .withOnDrawerItemClickListener((view, position, drawerItem) -> {
                            setLocale("en");
                            return false;
                        }),
                new SecondaryDrawerItem().withIcon(bulgarianIconId).withName(bulgarianText).withLevel(2)
                        .withOnDrawerItemClickListener((view, position, drawerItem) -> {
                            setLocale("bg");
                            return false;
                        }),
                new SecondaryDrawerItem().withIcon(romanianIconId).withName(romanianText).withLevel(2)
                        .withOnDrawerItemClickListener((view, position, drawerItem) -> {
                            setLocale("ro");
                            return false;
                        })
        );
    }

    private AccountHeader prepareAccountHeader(final User user, final int colorId) {
        return new AccountHeaderBuilder()
                .withActivity(this)
                .addProfiles(
                        new ProfileDrawerItem().withName(user.getFirstName() + StringUtils.SEPARATOR
                                + user.getLastName()).withEmail(user.getEmail())
                )
                .withCompactStyle(true)
                .withTextColor(getResources().getColor(colorId))
                .withSelectionListEnabledForSingleProfile(false)
                .build();
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
