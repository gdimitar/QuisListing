package com.quislisting.task.impl;

import com.quislisting.model.BaseListing;
import com.quislisting.task.AbstractGetCollectionJsonFromRequestTask;
import com.quislisting.task.AsyncCollectionResponse;

import java.util.Collection;

public class HttpGetBaseListingsRequestTask extends AbstractGetCollectionJsonFromRequestTask<BaseListing> {

    public AsyncCollectionResponse<BaseListing> delegate;

    @Override
    protected Collection<BaseListing> doInBackground(final String... params) {
        return super.doInBackground(params);
    }

    @Override
    protected void onPostExecute(final Collection<BaseListing> baseListings) {
        delegate.processFinish(baseListings);
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
    protected Class<BaseListing> getDeclaredClass() {
        return BaseListing.class;
    }

    @Override
    protected boolean modifyJsonData() {
        return true;
    }
}
