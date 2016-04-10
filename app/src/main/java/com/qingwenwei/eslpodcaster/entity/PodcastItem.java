package com.qingwenwei.eslpodcaster.entity;

import android.os.Parcel;

import java.io.Serializable;

/**
 * Created by qingwenwei on 2016-04-09.
 */
public class PodcastItem implements Serializable {
    public final String title;
    public final String subtitle;
    public final String description;
    public final String pubDate;
    public final String mp3Url;

    public PodcastItem(String title, String subtitle, String description, String pubDate, String mp3Url){
        this.title = title;
        this.subtitle = subtitle;
        this.description = description;
        this.pubDate = pubDate;
        this.mp3Url = mp3Url;
    }

    protected PodcastItem(Parcel in) {
        title = in.readString();
        subtitle = in.readString();
        description = in.readString();
        pubDate = in.readString();
        mp3Url = in.readString();
    }


    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public String getDescription() {
        return description;
    }

    public String getPubDate() {
        return pubDate;
    }

    public String getMp3Url() {
        return mp3Url;
    }

    public String toString(){
        return "@============\n" +
                "[TITLE]: " + title +
                "\n[SUBTITLE]:" + subtitle +
                "\n============\n\n";
    }

}
