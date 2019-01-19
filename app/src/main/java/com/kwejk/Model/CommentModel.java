package com.kwejk.Model;

public class CommentModel {

    String author;
    String text;
    String date;
    String avatar;

    public CommentModel(String author, String text, String date, String avatar) {
        this.author = author;
        this.text = text;
        this.date = date;
        this.avatar = avatar;
    }

    public String getAuthor() {
        return author;
    }

    public String getText() {
        return text;
    }

    public String getDate() { return date; }

    public String getAvatar(){ return avatar; }

}