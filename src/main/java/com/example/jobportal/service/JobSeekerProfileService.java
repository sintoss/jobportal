package com.example.jobportal.service;


import com.example.jobportal.entity.JobSeekerProfile;
import com.example.jobportal.entity.Users;
import com.example.jobportal.repository.JobSeekerProfileRepository;
import com.example.jobportal.repository.UsersRepository;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class JobSeekerProfileService {


    private final JobSeekerProfileRepository jobSeekerProfileRepository;
    private final UsersRepository usersRepository;

    public JobSeekerProfileService(JobSeekerProfileRepository jobSeekerProfileRepository, UsersRepository usersRepository) {
        this.jobSeekerProfileRepository = jobSeekerProfileRepository;
        this.usersRepository = usersRepository;
    }

    public Optional<JobSeekerProfile> getOne(long userId) {
        return jobSeekerProfileRepository.findById(userId);
    }

    public JobSeekerProfile addNew(JobSeekerProfile jobSeekerProfile) {

        return jobSeekerProfileRepository.save(jobSeekerProfile);
    }

    public JobSeekerProfile getCurrentSeekerProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {

            String currentUserName = authentication.getName();
            Users users = usersRepository.findByEmail(currentUserName).orElseThrow(() -> new UsernameNotFoundException("User not found"));
            Optional<JobSeekerProfile> currentSeekerProfile = getOne(users.getUserId());
            return currentSeekerProfile.orElse(null);
        }
        return null;
    }
}
