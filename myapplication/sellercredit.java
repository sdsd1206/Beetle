package com.example.jack.myapplication;

public class sellercredit {
    private String bank;
    private String branch;
    private String account;
    private String name;

    public sellercredit() {
    }

    public sellercredit(String account, String bank, String branch, String name) {
        this.account = account;
        this.bank = bank;
        this.branch = branch;
        this.name = name;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
