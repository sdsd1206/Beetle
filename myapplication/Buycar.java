package com.example.jack.myapplication;

public class Buycar {
    String name;
    String sellername;
    String price;
    String pic;
    String uid;
    String key;

    public Buycar() {

    }

    public Buycar(String name, String sellername, String price, String pic, String uid, String key) {
        this.name = name;
        this.sellername = sellername;
        this.price = price;
        this.pic = pic;
        this.uid = uid;
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSellername() {
        return sellername;
    }

    public void setSellername(String sellername) {
        this.sellername = sellername;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
