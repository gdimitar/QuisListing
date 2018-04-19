package com.quislisting.task.impl;

import android.os.AsyncTask;
import android.text.TextUtils;

import com.quislisting.converter.JsonConverter;
import com.quislisting.model.AuthenticationResult;
import com.quislisting.task.AsyncObjectResponse;
import com.quislisting.task.RestRouter;
import com.quislisting.task.handler.HttpHandler;
import com.quislisting.util.JsonObjectPopulator;

import org.json.JSONObject;

public class HttpAuthenticateUserRequestTask extends AsyncTask<String, Void, AuthenticationResult> {

    public AsyncObjectResponse<AuthenticationResult> delegate;

    @Override
    protected AuthenticationResult doInBackground(final String... params) {
        final HttpHandler httpHandler = new HttpHandler();
        final JSONObject jsonObject = JsonObjectPopulator.prepareUserAuthenticationJson(params[0],
                params[1]);
        final String jsonResponse = httpHandler
                .makeHttpAuthenticateUserPostRequest(RestRouter.User.AUTHENTICATE_USER, jsonObject);

        if (TextUtils.isEmpty(jsonResponse)) {
            return null;
        }

        final JsonConverter<AuthenticationResult> jsonConverter = new JsonConverter<>();
        return jsonConverter.extractObjectFromJson(jsonResponse, AuthenticationResult.class);
    }

    @Override
    protected void onPostExecute(final AuthenticationResult authenticationResult) {
        delegate.processFinish(authenticationResult);
    }
}
