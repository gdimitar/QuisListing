package com.quislisting.model;

import com.google.gson.annotations.SerializedName;
import com.quislisting.dto.ContentFieldGroupDTO;

import java.util.List;

public class ListingField {

    @SerializedName("id")
    private Long id;
    @SerializedName("type")
    private String type;
    @SerializedName("name")
    private String name;
    @SerializedName("value")
    private String value;
    @SerializedName("items")
    private List<ListingFieldItem> items;
    @SerializedName("dlListingFieldItemGroups")
    private List<ListingFieldItemGroup> dlListingFieldItemGroups;
    @SerializedName("dlContentFieldGroup")
    private ContentFieldGroupDTO dlContentFieldGroup;
}
