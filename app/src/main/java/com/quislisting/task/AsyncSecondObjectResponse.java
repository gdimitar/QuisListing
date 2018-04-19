package com.quislisting.task;

public interface AsyncSecondObjectResponse<T> {
    void processSecondFinish(T result);
}
