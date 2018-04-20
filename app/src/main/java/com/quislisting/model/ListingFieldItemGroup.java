package com.quislisting.model;

import com.google.gson.annotations.SerializedName;
import com.quislisting.dto.ListingFieldItemDTO;

import java.util.List;

public class ListingFieldItemGroup {

    @SerializedName("id")
    private Long id;
    @SerializedName("value")
    private String value;
    @SerializedName("dlListingFieldItems")
    private List<ListingFieldItemDTO> dlListingFieldItems;
}
