package com.reports.dto.pages;

import com.reports.dto.CurrencyDto;
import com.reports.dto.OperationDto;

import java.util.List;

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
