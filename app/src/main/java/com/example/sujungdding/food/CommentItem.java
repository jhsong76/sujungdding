package com.example.sujungdding.food;

public class CommentItem {
    String cmt_no, cmt_writer, cmt_content, cmt_secret, cmt_date;
    public CommentItem(String cmt_no, String cmt_writer, String cmt_content, String cmt_secret, String cmt_date){
        this.cmt_no = cmt_no;
        this.cmt_writer = cmt_writer;
        this.cmt_content = cmt_content;
        this.cmt_secret = cmt_secret;
        this.cmt_date = cmt_date;
    }
    public String getCmt_no() {
        return cmt_no;
    }
    public void setCmt_no(String cmt_no) {
        this.cmt_no = cmt_no;
    }
    public String getCmt_writer() {
        return cmt_writer;
    }
    public void setCmt_writer(String cmt_writer) {
        this.cmt_writer = cmt_writer;
    }
    public String getCmt_content() {
        return cmt_content;
    }
    public void setCmt_content(String cmt_content) {
        this.cmt_content = cmt_content;
    }
    public String getCmt_secret() {
        return cmt_secret;
    }
    public void setCmt_secret(String cmt_secret) {
        this.cmt_secret = cmt_secret;
    }
    public String getCmt_date() {
        return cmt_date;
    }
    public void setCmt_date(String cmt_date) {
        this.cmt_date = cmt_date;
    }
}
