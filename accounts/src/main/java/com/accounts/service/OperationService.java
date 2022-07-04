package com.accounts.service;

import com.accounts.dto.OperationDto;
import com.accounts.entity.AccountEntity;
import com.accounts.entity.BalanceEntity;
import com.accounts.entity.OperationEntity;
import com.accounts.repositories.*;
import com.accounts.security.JwtTokenProvider;
import com.accounts.service.api.IOperationService;
import com.accounts.utils.Mapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class OperationService implements IOperationService {

    private final OperationRepo operationRepo;
    private final Mapper mapper;
    private final AccountRepo accountRepo;
    private final BalanceRepo balanceRepo;
    private final JwtTokenProvider jwtTokenProvider;

    public OperationService(OperationRepo operationRepo, Mapper mapper,
                            AccountRepo accountRepo, BalanceRepo balanceRepo,
                            JwtTokenProvider jwtTokenProvider) {
        this.operationRepo = operationRepo;
        this.mapper = mapper;
        this.accountRepo = accountRepo;
        this.balanceRepo = balanceRepo;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Transactional
    public void addOperation(UUID uuid, OperationDto operationDto,String token){
        LocalDateTime now = LocalDateTime.now();
        if (operationDto.getDateOperation()==null) operationDto.setDateOperation(now);
        OperationEntity entity = mapper.map(operationDto, OperationEntity.class);

        String username = jwtTokenProvider.getUsername(token);
        AccountEntity accountEntity = accountRepo.findByUuidAndUser(uuid,username)
                .orElseThrow(() -> new EntityNotFoundException("Account with such uuid wasn't found"));
        entity.setAccountEntity(accountEntity);

        BalanceEntity balanceEntity = accountEntity.getBalance();
        balanceEntity.setBalance(balanceEntity.getBalance().add(operationDto.getValue()));
        balanceRepo.saveAndFlush(balanceEntity);

        entity.setDtCreate(now);

        operationRepo.saveAndFlush(entity);
    }

    public Page<OperationDto> getOperations(UUID uuid,Integer page,Integer size,String token){
        String username = jwtTokenProvider.getUsername(token);
        return operationRepo.findAllByAccountEntity_UuidAndAccountEntity_User(uuid, PageRequest
                .of(page, size, Sort.Direction.DESC, "dtCreate"),username)
                 .map(x->mapper.map(x, OperationDto.class));
    }

    public HashMap<String, Object> getOperation(UUID uuid,String token){
        String username = jwtTokenProvider.getUsername(token);
        OperationEntity operationEntity = operationRepo.findByUuidAndAccountEntity_User(uuid,username)
                .orElseThrow(
                () -> new EntityNotFoundException("Operation with such uuid wasn't found"));
        HashMap<String, Object> mapOperation = new HashMap<>();
        mapOperation.put("uuid",operationEntity.getUuid());
        mapOperation.put("dtCreate",operationEntity.getDtCreate());
        mapOperation.put("dtUpdate",operationEntity.getDtUpdate());
        mapOperation.put("description",operationEntity.getDescription());
        mapOperation.put("value",operationEntity.getValue());
        mapOperation.put("dateOperation",operationEntity.getDateOperation());
        mapOperation.put("category",operationEntity.getCategory());
        mapOperation.put("currency",operationEntity.getCurrency());
        mapOperation.put("account_uuid",operationEntity.getAccountEntity().getUuid());
        return mapOperation;
    }

    @Transactional
   public void updateOperation(UUID uuid,UUID uuidOperation,
                               Long dtUpdate,OperationDto operationDto, String token)
            throws EntityNotFoundException {

        OperationEntity operationEntity = operationRepo.findByUuidAndDtUpdate(uuidOperation, LocalDateTime
               .ofInstant(Instant.ofEpochMilli(dtUpdate), ZoneOffset.UTC))
               .orElseThrow(()->new EntityNotFoundException("Operation with such uuid wasn't found"));

       operationEntity.setDescription(operationDto.getDescription());

        String username = jwtTokenProvider.getUsername(token);
        AccountEntity accountEntity = accountRepo.findByUuidAndUser(uuid,username)
                .orElseThrow(() -> new EntityNotFoundException("Account with such uuid wasn't found"));
        BalanceEntity balanceEntity = accountEntity.getBalance();
        balanceEntity.setBalance(balanceEntity.getBalance()
                .subtract(operationEntity.getValue()).add(operationDto.getValue()));

        operationEntity.setValue(operationDto.getValue());
        operationRepo.save(operationEntity);

       operationRepo.saveAndFlush(operationEntity);
   }

   @Transactional
   public void deleteOperation(UUID uuidOperation, String token){
       String username = jwtTokenProvider.getUsername(token);
       OperationEntity operationEntity = operationRepo.findById(uuidOperation)
               .orElseThrow(() -> new EntityNotFoundException("Category with such uuid wasn't found"));
       operationRepo.deleteByUuidAndAccountEntity_User(uuidOperation,username);

       AccountEntity accountEntity = operationEntity.getAccountEntity();
       BalanceEntity balanceEntity = accountEntity.getBalance();
       balanceEntity.setBalance(balanceEntity.getBalance().subtract(operationEntity.getValue()));

       balanceRepo.saveAndFlush(balanceEntity);
   }

   public List<OperationDto> getSortedOperations (UUID accountUuid,List<UUID> categoriesUuid,String token){
       String username = jwtTokenProvider.getUsername(token);
       List<OperationDto> operationDtos = mapper.mapAll(operationRepo
                       .findAllByAccountEntity_UuidAndAccountEntity_UserAndCategoryIn(accountUuid, username, categoriesUuid),
               OperationDto.class);
       if (operationDtos.isEmpty()) throw new EntityNotFoundException("There is no operations");
       return  operationDtos;
   }

   public List<OperationDto> getByDateOperations (UUID accountUuid,List<LocalDateTime> dates,String token){
       String username = jwtTokenProvider.getUsername(token);
       List<OperationDto> operationDtos = mapper.mapAll(operationRepo.findAllByAccountEntity_UuidAndAccountEntity_UserAndDateOperationBetween(
               accountUuid, username, dates.get(0), dates.get(1)
       ), OperationDto.class);
       if (operationDtos.isEmpty()) throw new EntityNotFoundException("There is no operations");
       return operationDtos;
   }

   
}
