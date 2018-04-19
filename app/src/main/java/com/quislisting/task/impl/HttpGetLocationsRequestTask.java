package com.quislisting.task.impl;

import com.quislisting.dto.LocationDTO;
import com.quislisting.task.AbstractGetCollectionJsonFromRequestTask;
import com.quislisting.task.AsyncSecondCollectionResponse;

import java.util.Collection;

public class HttpGetLocationsRequestTask extends AbstractGetCollectionJsonFromRequestTask<LocationDTO> {

    public AsyncSecondCollectionResponse<LocationDTO> delegate;

    @Override
    protected Collection<LocationDTO> doInBackground(final String... params) {
        return super.doInBackground(params);
    }

    @Override
    protected void onPostExecute(final Collection<LocationDTO> listingLocations) {
        delegate.processSecondFinish(listingLocations);
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
    protected Class<LocationDTO> getDeclaredClass() {
        return LocationDTO.class;
    }

    @Override
    protected boolean modifyJsonData() {
        return false;
    }
}
