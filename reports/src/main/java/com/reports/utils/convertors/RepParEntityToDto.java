package com.reports.utils.convertors;

import com.reports.dto.ReportParamDto;
import com.reports.entity.AccountEntity;
import com.reports.entity.CategoryEntity;
import com.reports.entity.ReportParamEntity;
import com.reports.utils.Mapper;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class RepParEntityToDto implements Converter<ReportParamEntity, ReportParamDto> {

    private final Mapper mapper;

    public RepParEntityToDto(Mapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public ReportParamDto convert(ReportParamEntity source) {
        ReportParamDto reportParamDto = new ReportParamDto();
        reportParamDto.setFromTime(source.getFromTime());
        reportParamDto.setToTime(source.getToTime());
        reportParamDto.setAccounts(source
                .getAccounts().stream()
                .map(AccountEntity::getUuid)
                .collect(Collectors.toList()));
        reportParamDto.setCategories(source
                .getCategories().stream()
                .map(CategoryEntity::getUuid)
                .collect(Collectors.toList()));
        return reportParamDto;
    }
}
