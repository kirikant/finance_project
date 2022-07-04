package com.accounts.rest;

import com.accounts.dto.AccountDto;
import com.accounts.exception.ValidationChecker;
import com.accounts.exception.ValidationError;
import com.accounts.exception.ValidationException;
import com.accounts.service.AccountService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/account")
public class AccountController {

    private final AccountService accountService;
    private final ValidationChecker validationChecker;

    public AccountController(AccountService accountService, ValidationChecker validationChecker) {
        this.accountService = accountService;
        this.validationChecker = validationChecker;
    }

    @PostMapping
    public ResponseEntity<?> add(@RequestBody AccountDto accountDto,
                                 @RequestHeader("Authorization") String token) throws EntityNotFoundException, ValidationException {
        validationChecker.checkAccountDtoFields(accountDto);
        accountService.save(accountDto,token);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public  ResponseEntity<?> all(@RequestParam Integer page,@RequestParam Integer size,
                                  @RequestHeader("Authorization") String token) throws EntityNotFoundException, ValidationException {
        validationChecker.checkPageParams(page, size);
        Page<AccountDto> allAccounts = accountService.getAll(page, size,token);
        return ResponseEntity.ok(allAccounts);
    }


    @GetMapping("/{uuid}")
    public  ResponseEntity<?> find(@PathVariable UUID uuid,
                                   @RequestHeader("Authorization") String token) throws EntityNotFoundException{
        AccountDto accountDto = accountService.get(uuid,token);
        return ResponseEntity.ok(accountDto);
    }

    @PutMapping("/{uuid}/dt_update/{dt_update}")
    public ResponseEntity<?> update(@PathVariable UUID uuid,
                                    @PathVariable(name = "dt_update") Long dtUpdate,
                                    @RequestBody AccountDto accountDto,
                                    @RequestHeader("Authorization") String token) throws EntityNotFoundException, ValidationException {
        validationChecker.checkAccountDtoFields(accountDto);
        accountService.update(uuid,dtUpdate,accountDto,token);
        return  ResponseEntity.ok().build();
    }

    @PostMapping("/sorted")
    public ResponseEntity<?> sortedAll(@RequestBody List<UUID> accountUuids,
                                       @RequestHeader("Authorization") String token){
        List<AccountDto> filteredAccounts = accountService.getFilteredAccounts(accountUuids, token);
        return ResponseEntity.ok().body(filteredAccounts);
    }

}
