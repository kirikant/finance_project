package com.telegram.dto.pages;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.telegram.dto.CategoryDto;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PageOfCategories {

    private List<CategoryDto> content;
    private Boolean last;

    public List<CategoryDto> getContent() {
        return content;
    }

    public void setContent(List<CategoryDto> content) {
        this.content = content;
    }

    public Boolean getLast() {
        return last;
    }

    public void setLast(Boolean last) {
        this.last = last;
    }
}
