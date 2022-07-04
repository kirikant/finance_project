package com.classifier.classifier.service.api;

import com.classifier.classifier.dto.CurrencyDto;
import org.springframework.data.domain.Page;

public interface ICurrencyService {
    void  add(CurrencyDto currencyDto);
    Page<CurrencyDto> get(Integer page, Integer size);
}
