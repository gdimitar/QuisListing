package com.quislisting.task.impl;

import com.quislisting.task.AbstractGetResultFromPostTask;
import com.quislisting.task.AsyncObjectResponse;
import com.quislisting.task.AsyncSecondObjectResponse;
import com.quislisting.util.JsonObjectPopulator;

import org.json.JSONObject;

public class HttpUpdateUserRequestTask extends AbstractGetResultFromPostTask {

    public AsyncObjectResponse<Integer> delegate;

    public AsyncSecondObjectResponse<Integer> additionalDelegate;

    private final boolean receiveUpdates;

    public HttpUpdateUserRequestTask(final boolean receiveUpdates) {
        this.receiveUpdates = receiveUpdates;
    }

    @Override
    protected Integer doInBackground(final String... params) {
        return super.doInBackground(params);
    }

    @Override
    protected void onPostExecute(final Integer result) {
        additionalDelegate.processSecondFinish(result);
    }

    @Override
    protected JSONObject prepareJsonObject(final String... params) {
        return JsonObjectPopulator.prepareUserJson(params[1], params[2], params[3],
                params[4], receiveUpdates, false, null);
    }

    @Override
    protected boolean includedBearerHeader() {
        return true;
    }

    @Override
    protected String getIdToken(final String... params) {
        return params[5];
    }
}
