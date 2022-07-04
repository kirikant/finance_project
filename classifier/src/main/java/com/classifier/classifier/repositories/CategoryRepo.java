package com.classifier.classifier.repositories;

import com.classifier.classifier.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CategoryRepo extends JpaRepository<CategoryEntity, UUID> {

}
