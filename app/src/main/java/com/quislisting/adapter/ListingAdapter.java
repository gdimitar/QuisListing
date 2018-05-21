package com.quislisting.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.quislisting.BuildConfig;
import com.quislisting.R;
import com.quislisting.model.BaseListing;
import com.quislisting.model.ListingLocation;
import com.quislisting.util.CollectionUtils;
import com.quislisting.util.StringUtils;

import java.util.List;

public class ListingAdapter extends BaseAdapter {

    private static final String FILE_CONTENT_URL = "/content/files";

    private final Context context;
    private final Resources resources;
    private final List<BaseListing> baseListings;
    private ViewHolder holder;

    public ListingAdapter(final Context context, final Resources resources, final List<BaseListing> baseListings) {
        this.context = context;
        this.resources = resources;
        this.baseListings = baseListings;
    }

    @Override
    public int getCount() {
        return baseListings.size();
    }

    @Override
    public Object getItem(final int position) {
        return baseListings.get(position);
    }

    @Override
    public long getItemId(final int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.fragment_recent_listings, null);
            holder = new ViewHolder();
            holder.listingImage = (ImageView) convertView.findViewById(R.id.listingImage);
            holder.listingTitle = (TextView) convertView.findViewById(R.id.listingTitle);
            holder.listingLocation = (TextView) convertView.findViewById(R.id.listingLocation);
            holder.listingDate = (TextView) convertView.findViewById(R.id.listingDate);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (holder.listingImage != null && baseListings.get(position).getFeaturedAttachment() != null) {
            Glide.with(context).load(BuildConfig.BASE_URL + FILE_CONTENT_URL +
                    baseListings.get(position).getFeaturedAttachment().getSmallImage())
                    .into(holder.listingImage);
        }
        holder.listingTitle.setText(baseListings.get(position).getTitle());
        holder.listingTitle.setTextColor(resources.getColor(R.color.black));
        holder.listingLocation.setText(getListingLocation(baseListings.get(position)
                .getDlLocations()));
        holder.listingLocation.setTextColor(resources.getColor(R.color.black));
        holder.listingDate.setText(StringUtils.convertLongTimestampToString(baseListings.get(position).getCreated()));
        holder.listingDate.setTextColor(resources.getColor(R.color.black));

        return convertView;
    }

    private String getListingLocation(final List<ListingLocation> listingLocations) {
        if (CollectionUtils.isNotEmpty(listingLocations) && listingLocations.get(0) != null) {
            final ListingLocation listingLocation = listingLocations.get(0);
            return listingLocation.getLocation();
        }

        return StringUtils.UNKNOWN_VALUE;
    }

    private static class ViewHolder {
        private ImageView listingImage;
        private TextView listingTitle, listingLocation, listingDate;
    }
}
