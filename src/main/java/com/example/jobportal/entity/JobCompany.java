package com.example.jobportal.entity;


import jakarta.persistence.*;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "job_company")
public class JobCompany {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private long id;
    private String name;
    private String logo;

    public JobCompany() {
    }

    public JobCompany(long id, String name, String logo) {
        this.id = id;
        this.name = name;
        this.logo = logo;
    }

    @Override
    public String toString() {
        return "JobCompany{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", logo='" + logo + '\'' +
                '}';
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }
}
