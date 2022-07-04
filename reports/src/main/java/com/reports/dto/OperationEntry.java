package com.reports.dto;

public class OperationEntry {

     private OperationDto operationDto;
     private AccountDto accountDto;

    public OperationEntry(OperationDto operationDto, AccountDto accountDto) {
        this.operationDto = operationDto;
        this.accountDto = accountDto;
    }

    public OperationDto getOperationDto() {
        return operationDto;
    }

    public void setOperationDto(OperationDto operationDto) {
        this.operationDto = operationDto;
    }

    public AccountDto getAccountDto() {
        return accountDto;
    }

    public void setAccountDto(AccountDto accountDto) {
        this.accountDto = accountDto;
    }
}
