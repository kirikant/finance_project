package mailScheduler.util.convertors;

import mailScheduler.dto.ReportParamDto;
import mailScheduler.entity.AccountEntity;
import mailScheduler.entity.CategoryEntity;
import mailScheduler.entity.ReportParamEntity;
import mailScheduler.util.Mapper;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Component
public class RepParEntityToDto implements Converter<ReportParamEntity, ReportParamDto> {

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
