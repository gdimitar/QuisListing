package com.quislisting.task;

import java.util.Collection;

public interface AsyncCollectionResponse<T> {
    void processFinish(Collection<T> collection);
}
