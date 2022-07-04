package com.reports.dto.pages;

import com.reports.dto.CategoryDto;
import com.reports.dto.OperationDto;

import java.util.List;

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
