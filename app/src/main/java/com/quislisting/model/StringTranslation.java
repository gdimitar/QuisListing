package com.quislisting.model;

import com.google.gson.annotations.SerializedName;

public class StringTranslation {

    @SerializedName("id")
    private Long id;
    @SerializedName("languageCode")
    private String languageCode;
    @SerializedName("value")
    private String value;
}
