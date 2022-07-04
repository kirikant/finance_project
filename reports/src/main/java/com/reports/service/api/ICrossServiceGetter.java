package com.reports.service.api;

import com.reports.dto.*;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface ICrossServiceGetter {
    List<AccountDto> getSortedAccountDtos(ReportParamDto reportParamDto, String token) throws IOException;
    List<OperationDto> getOperations(AccountDto accountDto, List<UUID> categories, String token) throws IOException;
    List<CategoryDto> getCategoriesDtos(String token) throws IOException;
    List<CurrencyDto> getCurrencyDtos(String token) throws IOException;
}
