package com.quislisting.dto;

import com.quislisting.model.StringTranslation;

import java.util.List;

public class ListingFieldItemDTO {

    private Long id;
    private String value;
    private List<StringTranslation> translatedValues;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(final String value) {
        this.value = value;
    }

    public List<StringTranslation> getTranslatedValues() {
        return translatedValues;
    }

    public void setTranslatedValues(final List<StringTranslation> translatedValues) {
        this.translatedValues = translatedValues;
    }
}
