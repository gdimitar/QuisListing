package com.quislisting.task;

import java.util.List;
import java.util.Map;

public interface AsyncMapResponse<T> {
    void processFinish(Map<T, List<T>> map);
}
