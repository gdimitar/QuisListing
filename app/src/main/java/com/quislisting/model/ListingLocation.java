package com.quislisting.model;

public class ListingLocation {

    private ListingLocation parent;
    private String location;
    private Long id;

    public ListingLocation(final String location) {
        this.location = location;
    }

    public ListingLocation getParent() {
        return parent;
    }

    public void setParent(final ListingLocation parent) {
        this.parent = parent;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(final String location) {
        this.location = location;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }
}
