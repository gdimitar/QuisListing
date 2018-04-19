package com.quislisting.util;

import com.quislisting.model.BaseCategory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BaseCategoryFilterUtil {

    public BaseCategoryFilterUtil() {

    }

    public static List<BaseCategory> filterParentCategories(final Collection<BaseCategory> categories) {
        final List<BaseCategory> filteredCategories = new ArrayList<>();
        for (final BaseCategory category : categories) {
            if (category.getParentId() == null) {
                filteredCategories.add(category);
            }
        }

        return filteredCategories;
    }

    public static List<BaseCategory> filterChildCategories(final Collection<BaseCategory> categories,
                                                           final Long parentId) {
        final List<BaseCategory> filteredCategories = new ArrayList<>();
        for (final BaseCategory category : categories) {
            if (parentId != null) {
                if (category.getParentId() != null && category.getParentId().equals(parentId)) {
                    filteredCategories.add(category);
                }
            } else if (category.getParentId() != null) {
                filteredCategories.add(category);
            }
        }

        return filteredCategories;
    }
}
