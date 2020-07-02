package com.example.jack.myapplication;

import java.io.Serializable;

public class Order2 implements Serializable { //for sell_order_store
    private String name;
    private String address;
    private String state;
    private String shipping;
    private String pic;
    private String price;
    private String quantity;
    private String buyer;
    private String time;
    private String uid;//存買家uid節點


    public Order2() {

    }

    public Order2(String name, String address, String state, String shipping, String pic, String price, String quantity,
                  String buyer, String time) {
        this.name = name;
        this.address = address;
        this.state = state;
        this.shipping = shipping;
        this.pic = pic;
        this.price = price;
        this.quantity = quantity;
        this.buyer = buyer;
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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getBuyer() {
        return buyer;
    }

    public void setBuyer(String buyer) {
        this.buyer = buyer;
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

    public String getShipping() {
        return shipping;
    }

    public void setShipping(String shipping) {
        this.shipping = shipping;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }
}
