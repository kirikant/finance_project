package com.telegram.dto.pages;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.telegram.dto.AccountDto;
import com.telegram.dto.ReportDto;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PageOfReports {
    private List<ReportDto> content;
    private Boolean last;

    public List<ReportDto> getContent() {
        return content;
    }

    public void setContent(List<ReportDto> content) {
        this.content = content;
    }

    public Boolean getLast() {
        return last;
    }

    public void setLast(Boolean last) {
        this.last = last;
    }
}
