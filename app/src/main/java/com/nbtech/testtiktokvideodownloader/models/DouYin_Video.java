package com.nbtech.testtiktokvideodownloader.models;


import com.fasterxml.jackson.annotation.JsonIgnore;

public class DouYin_Video {

    protected String id = "";
    protected String title = "";
    protected String content = "";
    protected String coverUrl = "";
    protected String url = "";
    protected int width = 0;
    protected int height = 0;

    @JsonIgnore
    public boolean isEmpty()
    {
        return this.id == null || this.id.isEmpty();
    }


    protected String originalUrl = "";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setOriginalUrl(String url) {
        this.originalUrl = url;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    @Override
    public String toString() {
        return "DouYin_Video{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", coverUrl='" + coverUrl + '\'' +
                ", url='" + url + '\'' +
                ", width=" + width +
                ", height=" + height +
                ", originalUrl='" + originalUrl + '\'' +
                '}';
    }
}
