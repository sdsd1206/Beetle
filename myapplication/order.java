package com.example.jack.myapplication;

public class order {
    private String name;
    private String count;
    private String total;

    public order() {

    }

    public order(String name, String count, String total) {
        this.name = name;
        this.count = count;
        this.total = total;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}
