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
public class RepParDtoToEntity implements Converter<ReportParamDto,ReportParamEntity> {

    private final Mapper mapper;

    public RepParDtoToEntity(Mapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public ReportParamEntity convert(ReportParamDto source) {
        ReportParamEntity reportParamEntity = mapper.map(source, ReportParamEntity.class);
       if (source.getAccounts()!=null) reportParamEntity.setAccounts(source.getAccounts()
                .stream().map(x->{
                   AccountEntity accountEntity = new AccountEntity();
                   accountEntity.setUuid(x);
                   return accountEntity;
               })
                .collect(Collectors.toList()));
        if (source.getCategories()!=null)  reportParamEntity.setCategories(source.getCategories()
                .stream().map(x->{
                    CategoryEntity categoryEntity = new CategoryEntity();
                    categoryEntity.setUuid(x);
                    return categoryEntity;
                })
                .collect(Collectors.toList()));
        return reportParamEntity;
    }
}
