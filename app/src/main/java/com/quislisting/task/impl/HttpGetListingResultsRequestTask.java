package com.quislisting.task.impl;

import android.text.TextUtils;
import android.util.Log;

import com.quislisting.converter.JsonConverter;
import com.quislisting.model.BaseListing;
import com.quislisting.task.AbstractGetCollectionJsonFromRequestTask;
import com.quislisting.task.AsyncCollectionResponse;
import com.quislisting.task.handler.HttpHandler;
import com.quislisting.util.JsonObjectPopulator;

import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;

public class HttpGetListingResultsRequestTask extends AbstractGetCollectionJsonFromRequestTask<BaseListing> {

    private static final String TAG = HttpGetListingResultsRequestTask.class.getSimpleName();

    public AsyncCollectionResponse<BaseListing> delegate;

    @Override
    protected Collection<BaseListing> doInBackground(final String... params) {
        final HttpHandler httpHandler = new HttpHandler();
        final URL url = httpHandler.createUrl(params[0]);
        try {
            final JSONObject jsonObject = JsonObjectPopulator.prepareSearchResultJson(params[1], params[2],
                    params[3], params[4], params[5]);
            final String jsonResponse = httpHandler.makeHttpGetSearchRequest(url, jsonObject, params[6]);

            if (TextUtils.isEmpty(jsonResponse)) {
                return null;
            }

            final JsonConverter<BaseListing> jsonConverter = new JsonConverter<>();
            return jsonConverter.extractCollectionFromJson(jsonResponse, BaseListing.class,
                    modifyJsonData());
        } catch (final IOException e) {
            Log.e(TAG, "Problem making the HTTP request.", e);
        }

        return null;
    }

    @Override
    protected void onPostExecute(final Collection<BaseListing> listings) {
        delegate.processFinish(listings);
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
