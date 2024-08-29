package com.example.jobportal.entity;


import jakarta.persistence.*;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "job_location")
public class JobLocation {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private long id;
    private String city;
    private String state;
    private String country;

    public JobLocation() {
    }

    public JobLocation(long id, String city, String state, String country) {
        this.id = id;
        this.city = city;
        this.state = state;
        this.country = country;
    }

    @Override
    public String toString() {
        return "JobLocation{" +
                "id=" + id +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", country='" + country + '\'' +
                '}';
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
