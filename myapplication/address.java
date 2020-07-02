package com.example.jack.myapplication;

public class address {
    private String address;
    private String postno;

    public address() {
    }

    public address(String address, String postno) {
        this.address = address;
        this.postno = postno;
    }

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
}
