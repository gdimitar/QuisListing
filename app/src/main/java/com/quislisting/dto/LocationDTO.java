package com.quislisting.dto;

import java.util.List;

public class LocationDTO {

    private Long id;
    private String name;
    private String slug;
    private Long parentId;
    private String description;
    private Long count;
    private String languageCode;
    private String sourceLanguageCode;
    private Long translationGroupId;
    private List<TranslationDTO> translations;

    public LocationDTO(final String name) {
        this.name = name;
    }

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

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(final Long parentId) {
        this.parentId = parentId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(final Long count) {
        this.count = count;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(final String languageCode) {
        this.languageCode = languageCode;
    }

    public String getSourceLanguageCode() {
        return sourceLanguageCode;
    }

    public void setSourceLanguageCode(final String sourceLanguageCode) {
        this.sourceLanguageCode = sourceLanguageCode;
    }

    public Long getTranslationGroupId() {
        return translationGroupId;
    }

    public void setTranslationGroupId(final Long translationGroupId) {
        this.translationGroupId = translationGroupId;
    }

    public List<TranslationDTO> getTranslations() {
        return translations;
    }

    public void setTranslations(final List<TranslationDTO> translations) {
        this.translations = translations;
    }
}
