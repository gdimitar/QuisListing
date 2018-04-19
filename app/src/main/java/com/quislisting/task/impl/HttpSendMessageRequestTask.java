package com.quislisting.task.impl;

import com.quislisting.task.AbstractGetResultFromPostTask;
import com.quislisting.task.AsyncObjectResponse;
import com.quislisting.util.JsonObjectPopulator;

import org.json.JSONObject;

public class HttpSendMessageRequestTask extends AbstractGetResultFromPostTask {

    public AsyncObjectResponse<Integer> delegate;

    @Override
    protected Integer doInBackground(final String... params) {
        return super.doInBackground(params);
    }

    @Override
    protected JSONObject prepareJsonObject(final String... params) {
        return JsonObjectPopulator.prepareMessageJson(params[1]);
    }

    @Override
    protected boolean includedBearerHeader() {
        return true;
    }

    @Override
    protected String getIdToken(final String... params) {
        return params[2];
    }
}
