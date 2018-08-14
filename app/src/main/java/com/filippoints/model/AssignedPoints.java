package com.filippoints.model;


/**
 * Created by hlib on 6/29/18.
 */

public class AssignedPoints {
    private int awarded_points;
    private String description;
    private int person_id;
    private long datestamp;

    public int getAwarded_points() {
        return awarded_points;
    }

    public void setAwarded_points(int awarded_points) {
        this.awarded_points = awarded_points;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPerson_id() {
        return person_id;
    }

    public void setPerson_id(int person_id) {
        this.person_id = person_id;
    }

    public long getDatestamp() {
        return datestamp;
    }

    public void setDatestamp(long datestamp) {
        this.datestamp = datestamp;
    }

    public AssignedPoints() {
    }

    public AssignedPoints(int awarded_points, String description, int person_id, long datestamp) {
        this.awarded_points = awarded_points;
        this.description = description;
        this.person_id = person_id;
        this.datestamp = datestamp;
    }
}
