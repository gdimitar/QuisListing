package com.quislisting.model;

import com.quislisting.dto.ContentFieldGroupDTO;

import java.util.List;

public class ListingField {

    private Long id;
    private String type;
    private String name;
    private String value;
    private List<ListingFieldItem> items;
    private List<ListingFieldItemGroup> dlListingFieldItemGroups;
    private ContentFieldGroupDTO dlContentFieldGroup;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(final String value) {
        this.value = value;
    }

    public List<ListingFieldItem> getItems() {
        return items;
    }

    public void setItems(final List<ListingFieldItem> items) {
        this.items = items;
    }

    public List<ListingFieldItemGroup> getDlListingFieldItemGroups() {
        return dlListingFieldItemGroups;
    }

    public void setDlListingFieldItemGroups(final List<ListingFieldItemGroup> dlListingFieldItemGroups) {
        this.dlListingFieldItemGroups = dlListingFieldItemGroups;
    }

    public ContentFieldGroupDTO getDlContentFieldGroup() {
        return dlContentFieldGroup;
    }

    public void setDlContentFieldGroup(final ContentFieldGroupDTO dlContentFieldGroup) {
        this.dlContentFieldGroup = dlContentFieldGroup;
    }
}
