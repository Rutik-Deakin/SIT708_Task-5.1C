package com.example.news;

public class NewsItem {
    private String imageResource;
    private String title;
    private String description;
    private String source;

    public NewsItem(String imageResource, String title, String description, String source) {
        this.imageResource = imageResource;
        this.title = title;
        this.description = description;
        this.source = source;
    }
    public String getImageUrl() {
        return imageResource;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getSource() {
        return this.source;
    }
}

