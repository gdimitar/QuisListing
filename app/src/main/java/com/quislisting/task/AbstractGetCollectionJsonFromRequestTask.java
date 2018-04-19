package com.quislisting.task;

import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.quislisting.converter.JsonConverter;
import com.quislisting.task.handler.HttpHandler;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;

public abstract class AbstractGetCollectionJsonFromRequestTask<T> extends AsyncTask<String, Void,
        Collection<T>> {

    private static final String TAG = AbstractGetCollectionJsonFromRequestTask.class.getSimpleName();

    @Override
    protected Collection<T> doInBackground(final String... params) {
        final HttpHandler httpHandler = new HttpHandler();
        final URL url = httpHandler.createUrl(params[0]);
        try {
            final String jsonResponse = httpHandler.makeHttpGetRequest(url, getIdToken(params),
                    getParentId());

            if (TextUtils.isEmpty(jsonResponse)) {
                return null;
            }

            final JsonConverter<T> jsonConverter = new JsonConverter<>();
            return jsonConverter.extractCollectionFromJson(jsonResponse, getDeclaredClass(),
                    modifyJsonData());
        } catch (final IOException e) {
            Log.e(TAG, "Problem making the HTTP request.", e);
        }

        return null;
    }

    protected abstract String getIdToken(String... params);

    protected abstract boolean getParentId();

    protected abstract Class<T> getDeclaredClass();

    protected abstract boolean modifyJsonData();
}
