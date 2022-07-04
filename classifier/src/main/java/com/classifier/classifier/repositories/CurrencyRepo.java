package com.classifier.classifier.repositories;


import com.classifier.classifier.entity.CurrencyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CurrencyRepo extends JpaRepository<CurrencyEntity, UUID> {

}
