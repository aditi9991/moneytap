package com.search.moneytap;

public class SearchItem {
    private String title,icon ,genre;


    public SearchItem() {
    }

    public SearchItem(String title, String icon , String genre) {
        this.title = title;
        this.genre = genre;
        this.icon=icon;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String name) {
        this.title = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }
}
