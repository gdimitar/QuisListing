package com.quislisting.task.impl;

import android.text.TextUtils;

import com.quislisting.converter.JsonConverter;
import com.quislisting.model.User;
import com.quislisting.task.AbstractGetJsonFromRequestTask;
import com.quislisting.task.AsyncObjectResponse;

public class HttpGetUserRequestTask extends AbstractGetJsonFromRequestTask {

    public AsyncObjectResponse<User> delegate;

    @Override
    protected Object doInBackground(final String... params) {
        return getResult(super.doInBackground(params));
    }

    @Override
    protected void onPostExecute(final Object user) {
        delegate.processFinish((User) user);
    }

    @Override
    protected AsyncObjectResponse<User> getDelegate() {
        return delegate;
    }

    @Override
    protected Object getResult(final Object jsonResponse) {
        final String stringResponse = (String) jsonResponse;
        if (TextUtils.isEmpty(stringResponse)) {
            return null;
        }

        final JsonConverter<User> jsonConverter = new JsonConverter<>();
        return jsonConverter.extractObjectFromJson(stringResponse, getDeclaredClass(),
                "createdDate", "lastModifiedDate", true);
    }

    @Override
    protected String getIdToken(final String... params) {
        if (params.length == 2) {
            return params[1];
        }

        return null;
    }

    @Override
    protected boolean getParentId() {
        return false;
    }

    @Override
    protected Class<User> getDeclaredClass() {
        return User.class;
    }
}
