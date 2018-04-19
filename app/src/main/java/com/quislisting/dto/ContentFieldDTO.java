package com.quislisting.dto;

import java.util.List;

public class ContentFieldDTO {

    private Long id;
    private Boolean coreField;
    private Integer orderNum;
    private String name;
    private String translatedName;
    private String slug;
    private String description;
    private String translatedDescription;
    private Type type;
    private String iconImage;
    private Boolean required;
    private Boolean hasConfiguration;
    private Boolean hasSearchConfiguration;
    private Boolean canBeOrdered;
    private Boolean hideName;
    private Boolean onExcerptPage;
    private Boolean onListingPage;
    private Boolean onSearchForm;
    private Boolean onMap;
    private Boolean onAdvancedSearchForm;
    private List<CategoryDTO> categories;
    private String options;
    private String searchOptions;
    private List<ContentFieldItemDTO> contentFieldItems;
    private List<ContentFieldItemGroupDTO> contentFieldItemGroups;
    private ContentFieldGroupDTO contentFieldGroup;
    private Boolean enabled;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Boolean getCoreField() {
        return coreField;
    }

    public void setCoreField(final Boolean coreField) {
        this.coreField = coreField;
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(final Integer orderNum) {
        this.orderNum = orderNum;
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

    public String getTranslatedDescription() {
        return translatedDescription;
    }

    public void setTranslatedDescription(final String translatedDescription) {
        this.translatedDescription = translatedDescription;
    }

    public Type getType() {
        return type;
    }

    public void setType(final Type type) {
        this.type = type;
    }

    public String getIconImage() {
        return iconImage;
    }

    public void setIconImage(final String iconImage) {
        this.iconImage = iconImage;
    }

    public Boolean getRequired() {
        return required;
    }

    public void setRequired(final Boolean required) {
        this.required = required;
    }

    public Boolean getHasConfiguration() {
        return hasConfiguration;
    }

    public void setHasConfiguration(final Boolean hasConfiguration) {
        this.hasConfiguration = hasConfiguration;
    }

    public Boolean getHasSearchConfiguration() {
        return hasSearchConfiguration;
    }

    public void setHasSearchConfiguration(final Boolean hasSearchConfiguration) {
        this.hasSearchConfiguration = hasSearchConfiguration;
    }

    public Boolean getCanBeOrdered() {
        return canBeOrdered;
    }

    public void setCanBeOrdered(final Boolean canBeOrdered) {
        this.canBeOrdered = canBeOrdered;
    }

    public Boolean getHideName() {
        return hideName;
    }

    public void setHideName(final Boolean hideName) {
        this.hideName = hideName;
    }

    public Boolean getOnExcerptPage() {
        return onExcerptPage;
    }

    public void setOnExcerptPage(final Boolean onExcerptPage) {
        this.onExcerptPage = onExcerptPage;
    }

    public Boolean getOnListingPage() {
        return onListingPage;
    }

    public void setOnListingPage(final Boolean onListingPage) {
        this.onListingPage = onListingPage;
    }

    public Boolean getOnSearchForm() {
        return onSearchForm;
    }

    public void setOnSearchForm(final Boolean onSearchForm) {
        this.onSearchForm = onSearchForm;
    }

    public Boolean getOnMap() {
        return onMap;
    }

    public void setOnMap(final Boolean onMap) {
        this.onMap = onMap;
    }

    public Boolean getOnAdvancedSearchForm() {
        return onAdvancedSearchForm;
    }

    public void setOnAdvancedSearchForm(final Boolean onAdvancedSearchForm) {
        this.onAdvancedSearchForm = onAdvancedSearchForm;
    }

    public List<CategoryDTO> getCategories() {
        return categories;
    }

    public void setCategories(final List<CategoryDTO> categories) {
        this.categories = categories;
    }

    public String getOptions() {
        return options;
    }

    public void setOptions(final String options) {
        this.options = options;
    }

    public String getSearchOptions() {
        return searchOptions;
    }

    public void setSearchOptions(final String searchOptions) {
        this.searchOptions = searchOptions;
    }

    public List<ContentFieldItemDTO> getContentFieldItems() {
        return contentFieldItems;
    }

    public void setContentFieldItems(final List<ContentFieldItemDTO> contentFieldItems) {
        this.contentFieldItems = contentFieldItems;
    }

    public List<ContentFieldItemGroupDTO> getContentFieldItemGroups() {
        return contentFieldItemGroups;
    }

    public void setContentFieldItemGroups(final List<ContentFieldItemGroupDTO> contentFieldItemGroups) {
        this.contentFieldItemGroups = contentFieldItemGroups;
    }

    public ContentFieldGroupDTO getContentFieldGroup() {
        return contentFieldGroup;
    }

    public void setContentFieldGroup(final ContentFieldGroupDTO contentFieldGroup) {
        this.contentFieldGroup = contentFieldGroup;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(final Boolean enabled) {
        this.enabled = enabled;
    }

    public enum Type {
        STRING, TEXT_AREA, NUMBER, SELECT, DEPENDENT_SELECT, RADIO, CHECKBOX, CHECKBOX_GROUP, WEBSITE, EMAIL, OPEN_HOURS,
        NUMBER_UNIT
    }
}
