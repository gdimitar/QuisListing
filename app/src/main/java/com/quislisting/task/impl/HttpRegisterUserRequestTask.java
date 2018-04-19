package com.quislisting.task.impl;

import com.quislisting.task.AbstractGetResultFromPostTask;
import com.quislisting.task.AsyncObjectResponse;
import com.quislisting.util.JsonObjectPopulator;

import org.json.JSONObject;

public class HttpRegisterUserRequestTask extends AbstractGetResultFromPostTask {

    public AsyncObjectResponse<Integer> delegate;

    @Override
    protected Integer doInBackground(final String... params) {
        return super.doInBackground(params);
    }

    @Override
    protected JSONObject prepareJsonObject(final String... params) {
        return JsonObjectPopulator.prepareUserJson(params[1], params[2], params[3], params[4],
                false, true, params[5]);
    }

    @Override
    protected boolean includedBearerHeader() {
        return false;
    }

    @Override
    protected String getIdToken(final String... params) {
        return null;
    }
}
