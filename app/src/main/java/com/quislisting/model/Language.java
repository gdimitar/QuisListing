package com.quislisting.model;

public class Language {

    private String code;
    private String englishName;
    private Long count;

    public String getCode() {
        return code;
    }

    public void setCode(final String code) {
        this.code = code;
    }

    public String getEnglishName() {
        return englishName;
    }

    public void setEnglishName(final String englishName) {
        this.englishName = englishName;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(final Long count) {
        this.count = count;
    }
}
