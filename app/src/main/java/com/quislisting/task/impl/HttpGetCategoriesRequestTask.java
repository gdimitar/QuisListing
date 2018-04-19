package com.quislisting.task.impl;

import com.quislisting.model.BaseCategory;
import com.quislisting.task.AbstractGetCollectionJsonFromRequestTask;
import com.quislisting.task.AsyncCollectionResponse;

import java.util.Collection;

public class HttpGetCategoriesRequestTask extends AbstractGetCollectionJsonFromRequestTask<BaseCategory> {

    public AsyncCollectionResponse<BaseCategory> delegate;

    @Override
    protected Collection<BaseCategory> doInBackground(final String... params) {
        return super.doInBackground(params);
    }

    @Override
    protected void onPostExecute(final Collection<BaseCategory> categories) {
        delegate.processFinish(categories);
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
    protected Class<BaseCategory> getDeclaredClass() {
        return BaseCategory.class;
    }

    @Override
    protected boolean modifyJsonData() {
        return false;
    }
}
