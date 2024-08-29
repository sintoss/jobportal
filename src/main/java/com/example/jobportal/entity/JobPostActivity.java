package com.example.jobportal.entity;


import jakarta.persistence.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "job_post_activity")
public class JobPostActivity {


    @Id
    @GeneratedValue(strategy = IDENTITY)
    private long jobPostId;

    @ManyToOne
    @JoinColumn(name = "posted_by_id", referencedColumnName = "userId")
    private Users postedById;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "job_location_id", referencedColumnName = "id")
    private JobLocation jobLocationId;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "job_company_id", referencedColumnName = "id")
    private JobCompany jobCompanyId;

    @Transient
    private Boolean isActive;
    @Transient
    private Boolean isSaved;
    @Length(max = 10000)
    private String descriptionOfJob;
    private String jobType;
    private String salary;
    private String remote;
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date postedDate;
    private String jobTitle;

    public JobPostActivity() {
    }

    public JobPostActivity(long jobPostId, Users postedById, JobLocation jobLocation, JobCompany jobCompany, Boolean isActive, Boolean isSaved, String descriptionOfJob, String jobType, String salary, String remote, Date postedDate, String jobTitle) {
        this.jobPostId = jobPostId;
        this.postedById = postedById;
        this.jobLocationId = jobLocation;
        this.jobCompanyId = jobCompany;
        this.isActive = isActive;
        this.isSaved = isSaved;
        this.descriptionOfJob = descriptionOfJob;
        this.jobType = jobType;
        this.salary = salary;
        this.remote = remote;
        this.postedDate = postedDate;
        this.jobTitle = jobTitle;
    }

    public Boolean getIsSaved() {
        return isSaved;
    }

    public void setIsSaved(Boolean saved) {
        isSaved = saved;
    }

    public long getJobPostId() {
        return jobPostId;
    }

    public void setJobPostId(long jobPostId) {
        this.jobPostId = jobPostId;
    }

    public Users getPostedById() {
        return postedById;
    }

    public void setPostedById(Users postedById) {
        this.postedById = postedById;
    }

    public JobLocation getJobLocationId() {
        return jobLocationId;
    }

    public void setJobLocationId(JobLocation jobLocation) {
        this.jobLocationId = jobLocation;
    }

    public JobCompany getJobCompanyId() {
        return jobCompanyId;
    }

    public void setJobCompanyId(JobCompany jobCompany) {
        this.jobCompanyId = jobCompany;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean active) {
        isActive = active;
    }

    public @Length(max = 1000) String getDescriptionOfJob() {
        return descriptionOfJob;
    }

    public void setDescriptionOfJob(@Length(max = 1000) String descriptionOfJob) {
        this.descriptionOfJob = descriptionOfJob;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getRemote() {
        return remote;
    }

    public void setRemote(String remote) {
        this.remote = remote;
    }

    public Date getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(Date postedDate) {
        this.postedDate = postedDate;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    @Override
    public String toString() {
        return "JobPostActivity{" +
                "jobPostId=" + jobPostId +
                ", postedById=" + postedById +
                ", jobLocation=" + jobLocationId +
                ", jobCompany=" + jobCompanyId +
                ", isActive=" + isActive +
                ", descriptionOfJob='" + descriptionOfJob + '\'' +
                ", jobType='" + jobType + '\'' +
                ", salary='" + salary + '\'' +
                ", remote='" + remote + '\'' +
                ", postedDate=" + postedDate +
                ", jobTitle='" + jobTitle + '\'' +
                '}';
    }
}
