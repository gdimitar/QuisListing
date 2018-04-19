package com.quislisting.task.impl;

import com.quislisting.model.Message;
import com.quislisting.task.AbstractGetCollectionJsonFromRequestTask;
import com.quislisting.task.AsyncCollectionResponse;

import java.util.Collection;

public class HttpGetMessagesRequestTask extends AbstractGetCollectionJsonFromRequestTask<Message> {

    public AsyncCollectionResponse<Message> delegate;

    @Override
    protected Collection<Message> doInBackground(final String... params) {
        return super.doInBackground(params);
    }

    @Override
    protected void onPostExecute(final Collection<Message> messages) {
        delegate.processFinish(messages);
    }

    @Override
    protected String getIdToken(final String... params) {
        return params[1];
    }

    @Override
    protected boolean getParentId() {
        return false;
    }

    @Override
    protected Class<Message> getDeclaredClass() {
        return Message.class;
    }

    @Override
    protected boolean modifyJsonData() {
        return false;
    }
}
