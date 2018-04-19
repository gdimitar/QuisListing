package com.quislisting.task;

import android.os.AsyncTask;
import android.util.Log;

import com.quislisting.task.handler.HttpHandler;

import java.io.IOException;
import java.net.URL;

public abstract class AbstractGetJsonFromRequestTask extends AsyncTask<String, Void, Object> {

    private static final String TAG = AbstractGetJsonFromRequestTask.class.getSimpleName();

    public AsyncObjectResponse<?> delegate;

    @Override
    protected Object doInBackground(final String... params) {
        final HttpHandler httpHandler = new HttpHandler();
        final URL url = httpHandler.createUrl(params[0]);
        try {
            return httpHandler.makeHttpGetRequest(url, getIdToken(params), getParentId());
        } catch (final IOException e) {
            Log.e(TAG, "Problem making the HTTP request.", e);
        }

        return null;
    }

    protected abstract AsyncObjectResponse<?> getDelegate();

    protected abstract Object getResult(Object result);

    protected abstract String getIdToken(String... params);

    protected abstract boolean getParentId();

    protected abstract Class<?> getDeclaredClass();
}
