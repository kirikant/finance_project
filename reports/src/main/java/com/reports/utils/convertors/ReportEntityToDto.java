package com.reports.utils.convertors;

import com.reports.dto.ReportDto;
import com.reports.dto.ReportParamDto;
import com.reports.entity.ReportEntity;
import com.reports.entity.ReportParamEntity;
import com.reports.utils.Mapper;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ReportEntityToDto implements Converter<ReportEntity, ReportDto> {

    private final Mapper mapper;
    private  final RepParEntityToDto repParEntityToDto;

    public ReportEntityToDto(Mapper mapper, RepParEntityToDto repParEntityToDto) {
        this.mapper = mapper;
        this.repParEntityToDto = repParEntityToDto;
    }

    @Override
    public ReportDto convert(ReportEntity source) {
        ReportDto reportDto = mapper.map(source, ReportDto.class);
        ReportParamEntity reportParamEntity = source.getReportParamEntity();
        ReportParamDto reportParamDto = repParEntityToDto.convert(reportParamEntity);
        reportDto.setReportParamDto(reportParamDto);
        return reportDto;
    }
}
