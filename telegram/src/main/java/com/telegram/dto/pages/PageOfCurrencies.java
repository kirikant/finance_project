package com.telegram.dto.pages;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.telegram.dto.CurrencyDto;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PageOfCurrencies {

    private List<CurrencyDto> content;
    private Boolean last;

    public List<CurrencyDto> getContent() {
        return content;
    }

    public void setContent(List<CurrencyDto> content) {
        this.content = content;
    }

    public Boolean getLast() {
        return last;
    }

    public void setLast(Boolean last) {
        this.last = last;
    }
}
