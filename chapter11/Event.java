package com.example.calendarapp;

public class Event {
    private String title;
    private String description;
    private long dateTime;

    public Event() {
        // 기본 생성자 (Gson용)
    }

    public Event(String title, String description, long dateTime) {
        this.title = title;
        this.description = description;
        this.dateTime = dateTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getDateTime() {
        return dateTime;
    }

    public void setDateTime(long dateTime) {
        this.dateTime = dateTime;
    }
}