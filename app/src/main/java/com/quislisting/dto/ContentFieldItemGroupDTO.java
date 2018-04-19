package com.quislisting.dto;

import java.util.List;

public class ContentFieldItemGroupDTO {

    private Long id;
    private String name;
    private String translatedName;
    private String description;
    private Integer orderNum;
    private List<ContentFieldItemDTO> contentFieldItems;

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

    public String getTranslatedName() {
        return translatedName;
    }

    public void setTranslatedName(final String translatedName) {
        this.translatedName = translatedName;
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

    public List<ContentFieldItemDTO> getContentFieldItems() {
        return contentFieldItems;
    }

    public void setContentFieldItems(final List<ContentFieldItemDTO> contentFieldItems) {
        this.contentFieldItems = contentFieldItems;
    }
}
