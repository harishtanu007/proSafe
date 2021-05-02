package com.harish.prosafe.data.model;

public class Incident {
    private String title;
    private String description;
    private String postedBy;

    public Incident() {
    }

    public Incident(String title, String description, String postedBy) {
        this.title = title;
        this.description = description;
        this.postedBy = postedBy;
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

    public String getPostedBy() {
        return postedBy;
    }

    public void setPostedBy(String postedBy) {
        this.postedBy = postedBy;
    }
}
