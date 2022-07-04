package com.classifier.classifier.service.api;

import com.classifier.classifier.dto.CategoryDto;
import org.springframework.data.domain.Page;

public interface ICategoryService {
    void  add(CategoryDto categoryDto);
    Page<CategoryDto> get(Integer page, Integer size);
}
