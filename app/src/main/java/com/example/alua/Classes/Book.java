package com.example.alua.Classes;

public class Book {

    private String name;
    private String avatar;
    private String author;
    private String content;
    private String genre;

    public Book() {}

    public Book(String name, String author, String content, String genre, String avatar) {
        this.name = name;
        this.author = author;
        this.content = content;
        this.genre = genre;
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
