package com.example.jack.myapplication;

import java.io.Serializable;

public class Order1 implements Serializable { //buy_itemdetail 用 buy_oder_waiting
    private String name;
    private String address;

    private String pic;
    private String price;
    private String quantity;
    private String seller;
    private String time;


    public Order1() {

    }

    public Order1(String name, String address,  String pic,String price,String quantity,
                  String seller,String time) {
        this.name = name;
        this.address = address;
        this.pic = pic;
        this.price = price;
        this.quantity = quantity;
        this.seller = seller;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
//
//    public String getState() {
//        return state;
//    }
//
//    public void setState(String price) {
//        this.state = state;
//    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String buyer) {
        this.seller = seller;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
