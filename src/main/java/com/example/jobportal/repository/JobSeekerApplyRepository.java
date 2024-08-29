package com.example.jobportal.repository;


import com.example.jobportal.entity.JobPostActivity;
import com.example.jobportal.entity.JobSeekerApply;
import com.example.jobportal.entity.JobSeekerProfile;
import org.springframework.boot.autoconfigure.batch.BatchProperties;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobSeekerApplyRepository extends JpaRepository<JobSeekerApply, Long> {

    List<JobSeekerApply> findByUserId(JobSeekerProfile id);

    List<JobSeekerApply> findByJob(JobPostActivity job);
}

