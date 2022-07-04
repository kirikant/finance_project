package com.classifier.classifier.service;

import com.classifier.classifier.dto.CurrencyDto;
import com.classifier.classifier.entity.CurrencyEntity;
import com.classifier.classifier.repositories.CurrencyRepo;
import com.classifier.classifier.service.api.ICurrencyService;
import com.classifier.classifier.utils.Mapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional(readOnly = true)
public class CurrencyService implements ICurrencyService {

    private final CurrencyRepo currencyRepo;
    private final Mapper mapper;

    public CurrencyService(CurrencyRepo currencyRepo, Mapper mapper) {
        this.currencyRepo = currencyRepo;
        this.mapper = mapper;
    }

    @Transactional
    public void add(CurrencyDto currencyDto) {
        CurrencyEntity currencyEntity = mapper.map(currencyDto, CurrencyEntity.class);
        currencyEntity.setDtCreate(LocalDateTime.now());
        currencyRepo.save(currencyEntity);
    }

    public Page<CurrencyDto> get(Integer page, Integer size) {
        return currencyRepo.findAll(PageRequest.of(page, size, Sort.Direction.DESC, "dtCreate"))
                .map((x) -> mapper.map(x, CurrencyDto.class));
    }
}
