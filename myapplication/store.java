package com.example.jack.myapplication;

public class store {
    private String name;
    private String pic;
    private String tag;
    public store() {

    }

    public store(String name, String pic, String tag) {
        this.name = name;
        this.pic = pic;
        this.tag = tag;
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

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
