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

import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.quislisting.R;
import com.quislisting.adapter.ListingAdapter;
import com.quislisting.draweritems.OverflowMenuDrawerItem;
import com.quislisting.model.BaseListing;
import com.quislisting.model.User;
import com.quislisting.service.SignoutService;
import com.quislisting.task.AsyncCollectionResponse;
import com.quislisting.task.AsyncObjectResponse;
import com.quislisting.task.RestRouter;
import com.quislisting.task.impl.HttpGetBaseListingsRequestTask;
import com.quislisting.task.impl.HttpGetUserRequestTask;
import com.quislisting.util.CollectionUtils;
import com.quislisting.util.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements AsyncCollectionResponse<BaseListing>,
        AsyncObjectResponse<User>, View.OnClickListener {

    private Drawer result = null;

    private String idToken = null;

    private ProgressDialog progressDialog;

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
            final HttpGetBaseListingsRequestTask getBaseListingsRequestTask =
                    new HttpGetBaseListingsRequestTask();
            getBaseListingsRequestTask.delegate = this;
            getBaseListingsRequestTask.execute(String.format(RestRouter.Listing.GET_RECENT_LISTINGS,
                    selectedLanguage));
        }

        // create toolbar
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // create the drawer
        result = new DrawerBuilder().withActivity(this).withToolbar(toolbar)
                .withSavedInstance(savedInstanceState).build();

        final HttpGetUserRequestTask retrieveUserRequest =
                new HttpGetUserRequestTask();
        retrieveUserRequest.delegate = this;
        retrieveUserRequest.execute(RestRouter.User.GET_USER, idToken);
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
        progressDialog.dismiss();
    }

    @Override
    public void processFinish(final User user) {
        prepareDrawer(result, idToken, user);
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
        final OverflowMenuDrawerItem languageDrawerItem = prepareOverflowMenuDrawerItem(R.string.language,
                R.string.languagedescription, R.menu.fragment_menu, R.drawable.language);

        drawer.addItems(addListingDrawerItem, listingsDrawerItem, messagesDrawerItem,
                languageDrawerItem);

        if (StringUtils.isNotEmpty(idToken) && user != null) {
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

    private OverflowMenuDrawerItem prepareOverflowMenuDrawerItem(final int nameId,
                                                                 final int languageDescriptionId,
                                                                 final int menuId,
                                                                 final int iconId) {
        return new OverflowMenuDrawerItem().withName(nameId).withDescription(languageDescriptionId)
                .withMenu(menuId).withOnMenuItemClickListener(item -> {
                    switch (item.getItemId()) {
                        case R.id.english:
                            setLocale("en");
                            break;
                        case R.id.bulgarian:
                            setLocale("bg");
                            break;
                        case R.id.romanian:
                            setLocale("ro");
                            break;
                        default:
                            setLocale("en");
                            break;
                    }
                    return false;
                }).withIcon(iconId);
    }

    private AccountHeader prepareAccountHeader(final User user, final int colorId) {
        return new AccountHeaderBuilder()
                .withActivity(this)
                .addProfiles(
                        new ProfileDrawerItem().withName(user.getFirstName() + user.getLastName())
                                .withEmail(user.getEmail())
                )
                .withCompactStyle(true)
                .withTextColor(getResources().getColor(colorId))
                .withSelectionListEnabledForSingleProfile(false)
                .build();
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
