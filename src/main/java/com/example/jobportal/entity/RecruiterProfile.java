package com.example.jobportal.entity;


import jakarta.persistence.*;

@Entity
@Table(name = "recruiter_profile")
public class RecruiterProfile {


    @Id
    private long UserAccountId;

    @OneToOne
    @JoinColumn(name = "user_account_id")
    @MapsId
    private Users userId;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;
    private String city;
    private String state;
    private String country;
    private String company;

    @Column(name = "profile_photo", nullable = true)
    private String profilePhoto;

    public RecruiterProfile(long userAccountId, Users userID, String firstName, String lastName, String city, String state, String country, String company, String profilePhoto) {
        UserAccountId = userAccountId;
        this.userId = userID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.city = city;
        this.state = state;
        this.country = country;
        this.company = company;
        this.profilePhoto = profilePhoto;
    }

    public RecruiterProfile() {
    }

    public RecruiterProfile(Users users) {
        this.userId = users;
    }


    @Override
    public String toString() {
        return "RecruiterProfile{" +
                "UserAccountId=" + UserAccountId +
                ", userID=" + userId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", country='" + country + '\'' +
                ", company='" + company + '\'' +
                ", profilePhoto='" + profilePhoto + '\'' +
                '}';
    }

    public long getUserAccountId() {
        return UserAccountId;
    }

    public void setUserAccountId(long userAccountId) {
        UserAccountId = userAccountId;
    }

    public Users getUserId() {
        return userId;
    }

    public void setUserId(Users userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    @Transient
    public String getPhotosImagePath() {
        if (profilePhoto == null) return null;
        return "/photos/recruiter/" + getUserAccountId() + "/" + profilePhoto;
    }
}
