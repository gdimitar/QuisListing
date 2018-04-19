package com.quislisting.dto;

import java.util.Set;

public class ContentFieldItemDTO {

    private Long id;
    private String value;
    private String translatedValue;
    private Integer orderNum;
    private ContentFieldDTO dlContentFieldDTO;
    private Long dlContentFieldItemGroupId;
    private ContentFieldItemDTO parent;
    private Set<ContentFieldItemDTO> children;

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

    public String getTranslatedValue() {
        return translatedValue;
    }

    public void setTranslatedValue(final String translatedValue) {
        this.translatedValue = translatedValue;
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(final Integer orderNum) {
        this.orderNum = orderNum;
    }

    public ContentFieldDTO getDlContentFieldDTO() {
        return dlContentFieldDTO;
    }

    public void setDlContentFieldDTO(final ContentFieldDTO dlContentFieldDTO) {
        this.dlContentFieldDTO = dlContentFieldDTO;
    }

    public Long getDlContentFieldItemGroupId() {
        return dlContentFieldItemGroupId;
    }

    public void setDlContentFieldItemGroupId(final Long dlContentFieldItemGroupId) {
        this.dlContentFieldItemGroupId = dlContentFieldItemGroupId;
    }

    public ContentFieldItemDTO getParent() {
        return parent;
    }

    public void setParent(final ContentFieldItemDTO parent) {
        this.parent = parent;
    }

    public Set<ContentFieldItemDTO> getChildren() {
        return children;
    }

    public void setChildren(final Set<ContentFieldItemDTO> children) {
        this.children = children;
    }
}
