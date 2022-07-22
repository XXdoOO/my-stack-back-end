package com.xx.pojo;

import java.sql.Timestamp;

public class Blog {
    private int id;
    private String title;
    private String content;
    private int star;
    private int views;
    private String author;
    private Timestamp time;
    private int comments;

    public Blog() {
    }

    public Blog(int id, String title, String content, int star, int views, String author, Timestamp time, int comments) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.star = star;
        this.views = views;
        this.author = author;
        this.time = time;
        this.comments = comments;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getStar() {
        return star;
    }

    public void setStar(int star) {
        this.star = star;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    @Override
    public String toString() {
        return "Blog{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", star=" + star +
                ", views=" + views +
                ", author='" + author + '\'' +
                ", time=" + time +
                ", comments=" + comments +
                '}';
    }
}
