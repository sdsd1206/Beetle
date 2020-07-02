package com.example.jack.myapplication;

public class sorder {
    private String address;
    private String buyer;
    private String name;
    private String pic;
    private String price;
    private String quantity;
    private String shipping;
    private String state;
    private String time;

    private sorder() {

    }

    public sorder(String address, String buyer, String name, String pic, String price, String quantity, String shipping, String state, String time) {
        this.address = address;
        this.buyer = buyer;
        this.name = name;
        this.pic = pic;
        this.price = price;
        this.quantity = quantity;
        this.shipping = shipping;
        this.state = state;
        this.time = time;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBuyer() {
        return buyer;
    }

    public void setBuyer(String buyer) {
        this.buyer = buyer;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getShipping() {
        return shipping;
    }

    public void setShipping(String shipping) {
        this.shipping = shipping;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
