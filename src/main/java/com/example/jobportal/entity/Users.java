package com.example.jobportal.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;

import java.util.Date;

import static jakarta.persistence.GenerationType.*;

@Entity
@Table(name = "users")
public class Users {


    @Id
    @GeneratedValue(strategy = IDENTITY)
    private long userId;

    @Column(unique = true)
    private String email;

    @Column(name = "is_active")
    private Boolean isActive;

    @NotEmpty
    private String password;

    @Column(name = "registration_date")
    private Date registrationDate;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "userTypeId", referencedColumnName = "userTypeId")
    private UsersType userTypeId;


    public Users(long id, String email, Boolean isActive, String password, Date registrationDate, UsersType userTypeId) {
        this.userId = id;
        this.email = email;
        this.isActive = isActive;
        this.password = password;
        this.registrationDate = registrationDate;
        this.userTypeId = userTypeId;
    }

    public Users() {
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long id) {
        this.userId = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public @NotEmpty String getPassword() {
        return password;
    }

    public void setPassword(@NotEmpty String password) {
        this.password = password;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    public UsersType getUserTypeId() {
        return userTypeId;
    }

    public void setUserTypeId(UsersType userTypeId) {
        this.userTypeId = userTypeId;
    }
}
