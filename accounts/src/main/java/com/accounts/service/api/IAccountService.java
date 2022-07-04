package com.accounts.service.api;

import com.accounts.dto.AccountDto;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface IAccountService {

    void save(AccountDto accountDto,String login);
    Page<AccountDto> getAll(Integer page, Integer size,String token);
    AccountDto get(UUID uuid,String token);
    void update(UUID uuid, Long dtUpdate, AccountDto accountDto,String token);

}
