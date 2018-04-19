package com.quislisting.model;

import com.quislisting.dto.ListingFieldItemDTO;

import java.util.List;

public class ListingFieldItemGroup {

    private Long id;
    private String value;
    private List<ListingFieldItemDTO> dlListingFieldItems;

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

    public List<ListingFieldItemDTO> getDlListingFieldItems() {
        return dlListingFieldItems;
    }

    public void setDlListingFieldItems(final List<ListingFieldItemDTO> dlListingFieldItems) {
        this.dlListingFieldItems = dlListingFieldItems;
    }
}
