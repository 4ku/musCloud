package com.example.vadim.muscloud.Entities;
enum AccountType{free,premium,artist,moderator,admin}
enum AccountState{Active,Frozen,Closed}
public class AccountInfo {
    private AccountState accountState;
    private AccountType accountType;
    private String login;
    public AccountInfo(){

    }
}
