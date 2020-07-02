package com.example.jack.myapplication;

public class live_room {
    private String prove;
    private String room_name;
    private String room_pic;
    private String tag;
    private String uid;

    public live_room() {

    }

    public live_room(String prove, String room_name, String room_pic, String tag, String uid) {
        this.prove = prove;
        this.room_name = room_name;
        this.room_pic = room_pic;
        this.tag = tag;
        this.uid = uid;
    }

    public String getProve() {
        return prove;
    }

    public void setProve(String prove) {
        this.prove = prove;
    }

    public String getRoom_name() {
        return room_name;
    }

    public void setRoom_name(String room_name) {
        this.room_name = room_name;
    }

    public String getRoom_pic() {
        return room_pic;
    }

    public void setRoom_pic(String room_pic) {
        this.room_pic = room_pic;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
