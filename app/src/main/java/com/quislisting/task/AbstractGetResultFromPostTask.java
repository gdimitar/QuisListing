package com.quislisting.task;

import android.os.AsyncTask;

import com.quislisting.task.handler.HttpHandler;

import org.json.JSONObject;

public abstract class AbstractGetResultFromPostTask extends AsyncTask<String, Void, Integer> {

    @Override
    protected Integer doInBackground(final String... params) {
        final HttpHandler httpHandler = new HttpHandler();
        final JSONObject jsonObject = prepareJsonObject(params);
        return httpHandler.makeHttpSendPostRequest(params[0], jsonObject, includedBearerHeader(),
                getIdToken(params));
    }

    protected abstract JSONObject prepareJsonObject(String... params);

    protected abstract boolean includedBearerHeader();

    protected abstract String getIdToken(String... params);
}
