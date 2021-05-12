package com.harish.prosafe.data.model;

import android.os.Parcel;

import java.io.Serializable;

public class Incident implements Serializable {
    private String title;
    private String description;
    private String postedBy;
    private String incidentCategory;
    private long postTime;
    private String address;
    private Coordinates coordinates;

    public Incident() {
    }

    public Incident(String title, String description, String postedBy, String incidentCategory, long postTime, String address, Coordinates coordinates) {
        this.title = title;
        this.description = description;
        this.postedBy = postedBy;
        this.incidentCategory = incidentCategory;
        this.postTime = postTime;
        this.address = address;
        this.coordinates = coordinates;
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

    public long getPostTime() {
        return postTime;
    }

    public void setPostTime(long postTime) {
        this.postTime = postTime;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
