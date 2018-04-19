package com.quislisting.task.impl;

import com.quislisting.model.Listing;
import com.quislisting.task.AbstractGetCollectionJsonFromRequestTask;
import com.quislisting.task.AsyncCollectionResponse;

import java.util.Collection;

public class HttpGetListingsRequestTask extends AbstractGetCollectionJsonFromRequestTask<Listing> {

    public AsyncCollectionResponse<Listing> delegate;

    @Override
    protected Collection<Listing> doInBackground(final String... params) {
        return super.doInBackground(params);
    }

    @Override
    protected void onPostExecute(final Collection<Listing> listings) {
        delegate.processFinish(listings);
    }

    @Override
    protected String getIdToken(final String... params) {
        return params[1];
    }

    @Override
    protected boolean getParentId() {
        return true;
    }

    @Override
    protected Class<Listing> getDeclaredClass() {
        return Listing.class;
    }

    @Override
    protected boolean modifyJsonData() {
        return true;
    }
}
