package com.example.jobportal.controller;


import com.example.jobportal.entity.*;
import com.example.jobportal.service.*;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
public class JobSeekApplyController {

    private final JobPostActivityService jobPostActivityService;
    private final UsersService usersService;
    private final JobSeekerApplyService jobSeekerApplyService;
    private final JobSeekerSaveService jobsSeekerSaveService;

    private final RecruiterProfileService recruiterProfileService;
    private final JobSeekerProfileService jobSeekerProfileService;

    public JobSeekApplyController(JobPostActivityService jobPostActivityService, UsersService usersService, JobSeekerApplyService jobSeekerApplyService, JobSeekerSaveService jobsSeekerSaveService, RecruiterProfileService recruiterProfileService, JobSeekerProfileService jobSeekerProfileService) {
        this.jobPostActivityService = jobPostActivityService;
        this.usersService = usersService;
        this.jobSeekerApplyService = jobSeekerApplyService;
        this.jobsSeekerSaveService = jobsSeekerSaveService;
        this.recruiterProfileService = recruiterProfileService;
        this.jobSeekerProfileService = jobSeekerProfileService;
    }

    @GetMapping("job-details-apply/{id}")
    public String display(@PathVariable("id") long id, Model model) {

        JobPostActivity jobDetails = jobPostActivityService.getOne(id);
        List<JobSeekerApply> jobSeekerApplyList = jobSeekerApplyService.getCandidatesJobs(jobDetails);
        List<JobSeekerSave> jobSeekerSaveList = jobsSeekerSaveService.getCandidatesJob(jobDetails);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("Recruiter"))) {
                RecruiterProfile user = recruiterProfileService.getCurrentRecruiterProfile();
                if (user != null) {
                    model.addAttribute("applyList", jobSeekerApplyList);
                }
            } else {
                JobSeekerProfile user = jobSeekerProfileService.getCurrentSeekerProfile();
                if (user != null) {
                    boolean exists = false;
                    boolean saved = false;
                    for (JobSeekerApply jobSeekerApply : jobSeekerApplyList) {
                        if (jobSeekerApply.getUserId().getUserAccountId() == user.getUserAccountId()) {
                            exists = true;
                            break;
                        }
                    }
                    for (JobSeekerSave jobSeekerSave : jobSeekerSaveList) {
                        if (jobSeekerSave.getUserId().getUserAccountId() == user.getUserAccountId()) {
                            saved = true;
                            break;
                        }
                    }
                    model.addAttribute("alreadyApplied", exists);
                    model.addAttribute("alreadySaved", saved);
                }
            }
        }
        JobSeekerApply jobSeekerApply = new JobSeekerApply();

        model.addAttribute("applyJob", jobSeekerApply);
        model.addAttribute("jobDetails", jobDetails);
        model.addAttribute("user", usersService.getCurrentUserProfile());

        return "job-details";

    }

    @PostMapping("job-details/apply/{id}")
    public String apply(@PathVariable("id") long id, JobSeekerApply jobSeekerApply) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUserName = authentication.getName();
            Users user = usersService.findByEmail(currentUserName);
            Optional<JobSeekerProfile> currentSeeker = jobSeekerProfileService.getOne(user.getUserId());
            JobPostActivity jobPostActivity = jobPostActivityService.getOne(id);
            if (currentSeeker.isPresent() && jobPostActivity != null) {
                jobSeekerApply.setUserId(currentSeeker.get());
                jobSeekerApply.setJob(jobPostActivity);
                jobSeekerApply.setApplyDate(new Date());
            } else {
                throw new RuntimeException("User not found");
            }
            jobSeekerApplyService.addNew(jobSeekerApply);
        }
        return "redirect:/dashboard/";
    }
}
