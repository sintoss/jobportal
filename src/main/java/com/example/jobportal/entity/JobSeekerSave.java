package com.example.jobportal.entity;


import jakarta.persistence.*;

import java.io.Serializable;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "userId", "job"
        })
})
public class JobSeekerSave implements Serializable {


    @Id
    @GeneratedValue(strategy = IDENTITY)
    private long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "userId", referencedColumnName = "user_account_id")
    private JobSeekerProfile userId;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "job", referencedColumnName = "job_post_id")
    private JobPostActivity job;

    public JobSeekerSave() {
    }

    public JobSeekerSave(JobSeekerProfile userId, long id, JobPostActivity job) {
        this.userId = userId;
        this.id = id;
        this.job = job;
    }

    @Override
    public String toString() {
        return "JobSeekerSave{" +
                "id=" + id +
                ", userId=" + userId +
                ", job=" + job +
                '}';
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public JobSeekerProfile getUserId() {
        return userId;
    }

    public void setUserId(JobSeekerProfile userId) {
        this.userId = userId;
    }

    public JobPostActivity getJob() {
        return job;
    }

    public void setJob(JobPostActivity job) {
        this.job = job;
    }
}
