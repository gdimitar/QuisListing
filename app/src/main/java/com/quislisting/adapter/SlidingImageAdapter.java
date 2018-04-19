package com.quislisting.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.quislisting.BuildConfig;
import com.quislisting.R;
import com.quislisting.model.Attachment;

import java.util.List;

public class SlidingImageAdapter extends PagerAdapter {

    private static final String FILE_CONTENT_URL = "/content/files";

    private final Context context;
    private final List<Attachment> images;
    private LayoutInflater inflater;

    public SlidingImageAdapter(final Context context, final List<Attachment> images) {
        this.context = context;
        this.images = images;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull final ViewGroup view, final int position) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        final View imageLayout = inflater.inflate(R.layout.sliding_images_layout, view, false);

        assert imageLayout != null;
        final ImageView listingPreviewImage = (ImageView) imageLayout.findViewById(R.id.listingPreviewImage);
        Glide.with(context).load(BuildConfig.BASE_URL + FILE_CONTENT_URL +
                images.get(position).getOriginalImage()).into(listingPreviewImage);

        view.addView(imageLayout, 0);

        return imageLayout;
    }

    @Override
    public boolean isViewFromObject(@NonNull final View v, @NonNull final Object obj) {
        return v == obj;
    }

    @Override
    public void destroyItem(@NonNull final ViewGroup container, final int i,
                            @NonNull final Object obj) {
        final ViewPager vp = (ViewPager) container;
        final View view = (View) obj;
        vp.removeView(view);
    }
}
