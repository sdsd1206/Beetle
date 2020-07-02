package com.example.jack.myapplication;

public class buyeraddress {
    String address;
    String postno;
    String branchname;
    String branchno;
    String store;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostno() {
        return postno;
    }

    public void setPostno(String postno) {
        this.postno = postno;
    }

    public String getBranchname() {
        return branchname;
    }

    public void setBranchname(String branchname) {
        this.branchname = branchname;
    }

    public String getBranchno() {
        return branchno;
    }

    public void setBranchno(String branchno) {
        this.branchno = branchno;
    }

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public buyeraddress() {  }

    public buyeraddress(String address,String postno){
        this.address=address;
        this.postno=postno;
    }

    public buyeraddress(String branchname,String branchno,String store){
        this.branchno=branchno;
        this.branchname=branchname;
        this.store=store;
    }
}
