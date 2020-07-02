package com.example.jack.myapplication;

import java.io.Serializable;

public class item implements Serializable {
    private String name;
    private String pic;
    private String price;
    private String monthsales;
    private String info;
    private String prove;
    private String tag; //或許能改成list
    private String seller;
    private String uid;
    private  String key;


    public item() {

    }

    public item(String name, String price,String monthsales,String info, String tag, String seller,String uid)
    {
        this.name = name;
        this.pic = pic;
        this.price = price;
        this.monthsales = monthsales;
        this.info = info;
        this.prove = prove;
        this.tag = tag;
        this.seller = seller;
        this.uid = uid;
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

    public String getMonthsales() {return monthsales;}

    public void setMonthsales(String monthsales) {this.monthsales = monthsales;}

    public String getInfo() {return info;}

    public void setInfo(String info) {this.info = info;}

    public String getProve() {return prove;}

    public void setProve(String prove) {this.prove = prove;}

    public String getTag() {return tag;}

    public void setTag(String tag) {this.tag = tag;}

    public String getSeller() {return seller;}

    public void setSeller(String seller) {this.seller = seller;}

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
