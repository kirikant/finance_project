package com.telegram.dto.pages;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.telegram.dto.AccountDto;
import com.telegram.dto.OperationDto;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PageOfOperations {
    private List<OperationDto> content;
    private Boolean last;

    public List<OperationDto> getContent() {
        return content;
    }

    public void setContent(List<OperationDto> content) {
        this.content = content;
    }

    public Boolean getLast() {
        return last;
    }

    public void setLast(Boolean last) {
        this.last = last;
    }

}
