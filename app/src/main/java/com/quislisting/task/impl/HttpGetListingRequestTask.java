package com.quislisting.task.impl;

import android.text.TextUtils;

import com.quislisting.converter.JsonConverter;
import com.quislisting.model.Listing;
import com.quislisting.task.AbstractGetJsonFromRequestTask;
import com.quislisting.task.AsyncObjectResponse;

public class HttpGetListingRequestTask extends AbstractGetJsonFromRequestTask {

    public AsyncObjectResponse<Listing> delegate;

    @Override
    protected Object doInBackground(final String... params) {
        return getResult(super.doInBackground(params));
    }

    @Override
    protected void onPostExecute(final Object listing) {
        delegate.processFinish((Listing) listing);
    }

    @Override
    protected AsyncObjectResponse<Listing> getDelegate() {
        return delegate;
    }

    @Override
    protected Object getResult(final Object jsonResponse) {
        final String stringResponse = (String) jsonResponse;
        if (TextUtils.isEmpty(stringResponse)) {
            return null;
        }

        final JsonConverter<Listing> jsonConverter = new JsonConverter<>();
        return jsonConverter.extractObjectFromJson(stringResponse, getDeclaredClass(),
                "created", "modified", false);
    }

    @Override
    protected String getIdToken(final String... params) {
        return null;
    }

    @Override
    protected boolean getParentId() {
        return false;
    }

    @Override
    protected Class<Listing> getDeclaredClass() {
        return Listing.class;
    }
}
