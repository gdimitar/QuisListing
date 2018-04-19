package com.quislisting.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.quislisting.R;
import com.quislisting.adapter.SlidingImageAdapter;
import com.quislisting.model.Attachment;
import com.quislisting.model.Listing;
import com.quislisting.task.AsyncObjectResponse;
import com.quislisting.task.RestRouter;
import com.quislisting.task.impl.HttpGetListingRequestTask;
import com.quislisting.util.CollectionUtils;
import com.quislisting.util.StringUtils;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ListingDetailsActivity extends AppCompatActivity implements AsyncObjectResponse<Listing>,
        View.OnClickListener {

    private ViewPager viewPager;
    private int currentPage = 0;
    private int NUM_PAGES = 0;

    @Bind(R.id.textCategory)
    TextView textCategory;
    @Bind(R.id.textLocation)
    TextView textLocation;
    @Bind(R.id.textPrice)
    TextView textPrice;
    @Bind(R.id.textContact)
    TextView textContact;
    @Bind(R.id.textDescription)
    TextView textDescription;
    @Bind(R.id.editListing)
    TextView editListing;
    @Bind(R.id.sendMessage)
    TextView sendMessage;
    @Bind(R.id.reportListing)
    TextView reportListing;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing_details);
        setTitle(getString(R.string.listingdetailsactivitytitle));

        ButterKnife.bind(this);

        final String listingId = getIntent().getStringExtra("listingId");
        final HttpGetListingRequestTask getListingRequestTask =
                new HttpGetListingRequestTask();
        getListingRequestTask.delegate = this;
        getListingRequestTask.execute(String.format(RestRouter.Listing.GET_LISTING, listingId));

        editListing.setOnClickListener(this);
        sendMessage.setOnClickListener(this);
        reportListing.setOnClickListener(this);
    }

    @Override
    public void processFinish(final Listing listing) {
        if (listing != null) {
            textCategory.setText(CollectionUtils.isNotEmpty(listing.getDlCategories())
                    ? listing.getDlCategories().get(0) : StringUtils.UNKNOWN_VALUE);
            textLocation.setText(CollectionUtils.isNotEmpty(listing.getDlLocations())
                    ? listing.getDlLocations().get(0).getLocation() : StringUtils.UNKNOWN_VALUE);
            textPrice.setText(listing.getPrice() != null
                    ? String.valueOf(listing.getPrice().doubleValue()) : StringUtils.UNKNOWN_VALUE);
            textContact.setText(StringUtils.isNotEmpty(listing.getContactInfo())
                    ? listing.getContactInfo() : StringUtils.UNKNOWN_VALUE);
            textDescription.setText(listing.getContent());

            final List<Attachment> attachments = listing.getAttachments();
            if (CollectionUtils.isNotEmpty(attachments)) {
                initImageSlider(attachments);
            }
        }
    }

    @Override
    public void onBackPressed() {
        final Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("idToken", getIntent().getStringExtra("idToken"));
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                final Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("idToken", getIntent().getStringExtra("idToken"));
                startActivity(intent);
                break;
        }
        return true;
    }

    @Override
    public void onClick(final View view) {
        switch (view.getId()) {
            case R.id.editListing:
                final Intent editListingIntent = new Intent(this,
                        EditListingActivity.class);
                editListingIntent.putExtra("listingId", getIntent().getStringExtra("listingId"));
                startActivity(editListingIntent);
                break;
            case R.id.sendMessage:
                final Intent sendMessageIntent = new Intent(this,
                        SendMessageActivity.class);
                sendMessageIntent.putExtra("idToken", getIntent().getStringExtra("idToken"));
                sendMessageIntent.putExtra("listingId", getIntent().getStringExtra("listingId"));
                startActivity(sendMessageIntent);
                break;
            case R.id.reportListing:
                Toast.makeText(this, "no design yet",
                        Toast.LENGTH_LONG).show();
                break;
        }
    }

    private void initImageSlider(final List<Attachment> images) {
        viewPager = (ViewPager) findViewById(R.id.imagePager);

        final SlidingImageAdapter slidingImageAdapter = new SlidingImageAdapter(this, images);
        viewPager.setAdapter(slidingImageAdapter);

        final CirclePageIndicator indicator = (CirclePageIndicator) findViewById(R.id.imageIndicator);
        indicator.setViewPager(viewPager);

        final float density = getResources().getDisplayMetrics().density;
        indicator.setRadius(5 * density);

        NUM_PAGES = images.size();

        final Handler handler = new Handler();
        final Runnable runnable = () -> {
            if (currentPage == NUM_PAGES) {
                currentPage = 0;
            }
            viewPager.setCurrentItem(currentPage++, true);
        };

        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(final int position) {
                currentPage = position;

            }

            @Override
            public void onPageScrolled(final int pos, final float arg1, final int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(final int pos) {

            }
        });

        indicator.setOnClickListener(v -> {
            final int currentItem = viewPager.getCurrentItem();
            final int totalItems = viewPager.getAdapter().getCount();
            final int nextItem = (currentItem + 1) % totalItems;
            viewPager.setCurrentItem(nextItem);
        });
    }
}
