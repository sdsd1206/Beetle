package com.example.jack.myapplication;

public class buyergoods {
    private String name;
    private String price;
    private String seller;
    private String address;
    private String pic;
    private String quantity;
    private String time;
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }


    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


    public buyergoods() {
    }

    public buyergoods(String address, String name, String pic, String price, String quantity, String seller, String status, String time) {
        this.name = name;
        this.price = price;
        this.seller = seller;
        this.address = address;
        this.pic = pic;
        this.quantity = quantity;
        this.time = time;
        this.status = status;
    }


}
