package com.classifier.classifier.service;

import com.classifier.classifier.dto.CategoryDto;
import com.classifier.classifier.entity.CategoryEntity;
import com.classifier.classifier.repositories.CategoryRepo;
import com.classifier.classifier.service.api.ICategoryService;
import com.classifier.classifier.utils.Mapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional(readOnly = true)
public class CategoryService implements ICategoryService {

    private final CategoryRepo categoryRepo;
    private final Mapper mapper;

    public CategoryService(CategoryRepo categoryRepo, Mapper mapper) {
        this.categoryRepo = categoryRepo;
        this.mapper = mapper;
    }

    @Transactional
    public  void  add(CategoryDto categoryDto){
        CategoryEntity categoryEntity = mapper.map(categoryDto, CategoryEntity.class);
        categoryEntity.setDtCreate(LocalDateTime.now());
        categoryRepo.save(categoryEntity);
    }

    public Page<CategoryDto> get(Integer page, Integer size){
        return categoryRepo.findAll(PageRequest.of(page,size, Sort.Direction.DESC,"dtCreate"))
                .map((x)->mapper.map(x,CategoryDto.class));
    }
}
