package com.filippoints.model;

/**
 * Created by hlib on 6/21/18.
 */

public class Person {
    private int pk;
    private String first_name;
    private String last_name;
    private int points;

    public Person() {
    }

    public int getPk() {
        return pk;
    }

    public void setPk(int pk) {
        this.pk = pk;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    @Override
    public String toString() {
        return "Person{" +
                "pk=" + pk +
                ", first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", points=" + points +
                '}';
    }
}
