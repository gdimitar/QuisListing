package com.quislisting.dto;

import java.util.List;

public class ContentFieldGroupDTO {

    private Long id;
    private String name;
    private String slug;
    private String description;
    private Integer orderNum;
    private List<ContentFieldItemDTO> dlContentFieldItems;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(final String slug) {
        this.slug = slug;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(final Integer orderNum) {
        this.orderNum = orderNum;
    }

    public List<ContentFieldItemDTO> getDlContentFieldItems() {
        return dlContentFieldItems;
    }

    public void setDlContentFieldItems(final List<ContentFieldItemDTO> dlContentFieldItems) {
        this.dlContentFieldItems = dlContentFieldItems;
    }
}
