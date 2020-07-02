package com.example.jack.myapplication;

public class user {
    private String face;
    private String mail;
    private String name;
    private String nickname;
    private String phone;
    private String points;
    private String sex;

    public user() {

    }


    public user(String face, String mail, String name, String nickname, String phone, String points, String sex) {
        this.face = face;
        this.mail = mail;
        this.name = name;
        this.nickname = nickname;
        this.phone = phone;
        this.points = points;
        this.sex = sex;
    }

    public String getFace() {
        return face;
    }

    public void setFace(String face) {
        this.face = face;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPoint() {
        return points;
    }

    public void setPoint(String poins) {
        this.points = points;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
}
