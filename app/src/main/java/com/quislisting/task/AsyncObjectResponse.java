package com.quislisting.task;

public interface AsyncObjectResponse<T> {
    void processFinish(T result);
}
