package com.quislisting.model;

import com.google.gson.annotations.SerializedName;

public class Language {

    @SerializedName("code")
    private String code;
    @SerializedName("englishName")
    private String englishName;
    @SerializedName("count")
    private Long count;
}
