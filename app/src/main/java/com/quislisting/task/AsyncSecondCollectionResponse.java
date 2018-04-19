package com.quislisting.task;

import java.util.Collection;

public interface AsyncSecondCollectionResponse<T> {
    void processSecondFinish(Collection<T> collection);
}
