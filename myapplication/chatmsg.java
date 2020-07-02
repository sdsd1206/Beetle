package com.example.jack.myapplication;

public class chatmsg {
    String who,roomcontext,time,username,youid;
    public chatmsg(String who, String roomcontext, String time, String username, String youid) {
        this.who = who;
        this.roomcontext = roomcontext;
        this.time = time;
        this.username = username;
        this.youid = youid;
    }

    public String getYouid() {
        return youid;
    }

    public void setYouid(String youid) {
        this.youid = youid;
    }

    public chatmsg(){}

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getWho() {
        return who;
    }

    public void setWho(String who) {
        this.who = who;
    }

    public String getRoomcontext() {
        return roomcontext;
    }

    public void setRoomcontext(String roomcontext) {
        this.roomcontext = roomcontext;
    }

}
