package com.harish.prosafe.data.model;

public class Incident {
    private String title;
    private String description;
    private String postedBy;
    private String incidentCategory;

    public Incident() {
    }

    public Incident(String title, String description, String postedBy, String incidentCategory) {
        this.title = title;
        this.description = description;
        this.postedBy = postedBy;
        this.incidentCategory = incidentCategory;
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

    public String getIncidentCategory() {
        return incidentCategory;
    }

    public void setIncidentCategory(String incidentCategory) {
        this.incidentCategory = incidentCategory;
    }
}
