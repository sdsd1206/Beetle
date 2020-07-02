package com.example.jack.myapplication;

public class buyerpay {
    private String cardname;
    private String cardno;
    private String exp_day;
    private String safe_no;

    public buyerpay() {

    }

    public buyerpay(String cardname, String cardno, String exp_day, String safe_no) {
        this.cardname = cardname;
        this.cardno = cardno;
        this.exp_day = exp_day;
        this.safe_no = safe_no;
    }

    public String getCardname() {
        return cardname;
    }

    public void setCardname(String cardname) {
        this.cardname = cardname;
    }

    public String getCardno() {
        return cardno;
    }

    public void setCardno(String cardno) {
        this.cardno = cardno;
    }

    public String getExp_day() {
        return exp_day;
    }

    public void setExp_day(String exp_day) {
        this.exp_day = exp_day;
    }

    public String getSafe_no() {
        return safe_no;
    }

    public void setSafe_no(String safe_no) {
        this.safe_no = safe_no;
    }
}
