package com.telegram.dto.pages;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.telegram.dto.AccountDto;
import com.telegram.dto.CategoryDto;

import java.util.List;


@JsonIgnoreProperties(ignoreUnknown = true)
public class PageOfAccounts {
    private List<AccountDto> content;
    private Boolean last;

    public List<AccountDto> getContent() {
        return content;
    }

    public void setContent(List<AccountDto> content) {
        this.content = content;
    }

    public Boolean getLast() {
        return last;
    }

    public void setLast(Boolean last) {
        this.last = last;
    }
}
