package com.accounts.service;

import com.accounts.dto.AccountDto;
import com.accounts.entity.AccountEntity;
import com.accounts.entity.BalanceEntity;
import com.accounts.repositories.AccountRepo;
import com.accounts.security.JwtTokenProvider;
import com.accounts.service.api.IAccountService;
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
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class AccountService implements IAccountService {

    private final AccountRepo accountRepo;
    private final Mapper mapper;
    private final JwtTokenProvider jwtTokenProvider;

    public AccountService(AccountRepo accountRepo,
                          Mapper mapper1, JwtTokenProvider jwtTokenProvider) {
        this.accountRepo = accountRepo;
        this.mapper = mapper1;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Transactional
    public void save(AccountDto accountDto,String token) {
        String username = jwtTokenProvider.getUsername(token);
        AccountEntity account = mapper.map(accountDto, AccountEntity.class);
        LocalDateTime now = LocalDateTime.now();
        account.setDtCreate(now);
        account.setUser(username);
        BalanceEntity balanceEntity = new BalanceEntity();
        balanceEntity.setBalance(BigDecimal.ZERO);
        balanceEntity.setDtCreate(now);
        account.setBalance(balanceEntity);
        accountRepo.save(account);
    }

    public Page<AccountDto> getAll(Integer page, Integer size, String token) throws EntityNotFoundException {
        String username = jwtTokenProvider.getUsername(token);
        Page<AccountEntity> all = accountRepo.findAllByUser(PageRequest.of(page, size
                , Sort.Direction.DESC, "dtCreate"),username);
        return all.map(x->{
            AccountDto accountDto = mapper.map(x, AccountDto.class);
            accountDto.setBalance(x.getBalance().getBalance());
            return accountDto;
        });
    }

    public AccountDto get(UUID uuid, String token) throws EntityNotFoundException {
        String username = jwtTokenProvider.getUsername(token);
        AccountEntity account = accountRepo.findByUuidAndUser(uuid,username)
                .orElseThrow(() -> new EntityNotFoundException("Account with such uuid wasn't found"));
        AccountDto accountDto = mapper.map(account, AccountDto.class);
        accountDto.setBalance(account.getBalance().getBalance());
        return accountDto;
    }

    @Transactional
    public void update(UUID uuid, Long dtUpdate, AccountDto accountDto,String token) throws EntityNotFoundException{
        String username = jwtTokenProvider.getUsername(token);
        AccountEntity account = accountRepo.findByUuidAndDtUpdateAndUser(uuid, Instant.ofEpochMilli(dtUpdate)
                        .atZone(ZoneOffset.UTC)
                       .toLocalDateTime(),
                        username)
                .orElseThrow(() -> new EntityNotFoundException("There is no such account"));

        account.setTitle(accountDto.getTitle());
        account.setDescription(accountDto.getDescription());
        account.setType(accountDto.getType());


        accountRepo.saveAndFlush(account);
    }

    public List<AccountDto> getFilteredAccounts(List<UUID> uuidList, String token){
        String username = jwtTokenProvider.getUsername(token);
        List<AccountDto> accounts = accountRepo.findAllByUuidInAndUser(uuidList, username).stream()
                .map(x -> mapper.map(x, AccountDto.class)).collect(Collectors.toList());
        if (accounts.isEmpty()) throw  new EntityNotFoundException("There is no such accounts");
        return  accounts;
    }

}
