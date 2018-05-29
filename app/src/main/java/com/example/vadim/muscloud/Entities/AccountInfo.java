package com.example.vadim.muscloud.Entities;

public class AccountInfo {
    private AccountState accountState;
    private AccountType accountType;
    private String login;
    public AccountInfo(String login,AccountType acType,AccountState acState){
        this.login=login;
        accountState=acState;
        accountType=acType;
    }
    public String getLogin(){
        return login;
    }
    public AccountType getAccountType(){
        return accountType;
    }
    public AccountState getAccountState(){
        return accountState;
    }
}
