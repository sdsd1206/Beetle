package com.example.jack.myapplication;

public class border {
    private String address;
    private String name;
    private String pic;
    private String price;
    private String quantity;
    private String seller;
    private String time;

    public border() {

    }

    public border(String address, String name, String pic, String price, String quantity, String seller, String time) {
        this.address = address;
        this.name = name;
        this.pic = pic;
        this.price = price;
        this.quantity = quantity;
        this.seller = seller;
        this.time = time;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
