package com.example.jobportal.service;


import com.example.jobportal.entity.*;
import com.example.jobportal.repository.JobPostActivityRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class JobPostActivityService {

    private final JobPostActivityRepository jobPostActivityRepository;


    public JobPostActivityService(JobPostActivityRepository jobPostActivityRepository) {
        this.jobPostActivityRepository = jobPostActivityRepository;
    }


    public JobPostActivity addNew(JobPostActivity jobPostActivity) {
        return jobPostActivityRepository.save(jobPostActivity);
    }

    public List<RecruiterJobsDto> getRecruiterJobs(Long id) {

        List<IRecruiterJob> recruiterJobs = jobPostActivityRepository.getRecruiterJobs(id);
        List<RecruiterJobsDto> recruiterJobsDtoList = new ArrayList<>();

        for (IRecruiterJob rec : recruiterJobs) {
            JobLocation location = new JobLocation(rec.getLocationId(), rec.getCity(), rec.getState(), rec.getCountry());
            JobCompany company = new JobCompany(rec.getCompanyId(), rec.getName(), "");
            recruiterJobsDtoList.add(
                    new RecruiterJobsDto(rec.getTotalCandidates(), rec.getJobPostId(), rec.getJobTitle(), location, company)
            );
        }
        return recruiterJobsDtoList;
    }

    public JobPostActivity getOne(long id) {
        return jobPostActivityRepository.findById(id).orElseThrow(() -> new RuntimeException("Job Post Activity Not Found"));
    }

    public void deleteOne(long id) {
        jobPostActivityRepository.deleteById(id);
    }

    public List<JobPostActivity> getAll() {
        return jobPostActivityRepository.findAll();
    }

    public List<JobPostActivity> search(String job, String location, List<String> type, List<String> remote, LocalDate searchDate) {

        return Objects.isNull(searchDate) ? jobPostActivityRepository.searchWithoutDate(job, location, type, remote)
                : jobPostActivityRepository.search(job, location, type, remote, searchDate);
    }
}

