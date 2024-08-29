package com.example.jobportal.service;

import com.example.jobportal.entity.RecruiterProfile;
import com.example.jobportal.entity.Users;
import com.example.jobportal.repository.RecruiterProfileRepository;
import com.example.jobportal.repository.UsersRepository;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RecruiterProfileService {


    private final RecruiterProfileRepository recruiterProfileRepository;
    private final UsersRepository usersRepository;

    public RecruiterProfileService(RecruiterProfileRepository recruiterProfileRepository, UsersRepository usersRepository) {
        this.recruiterProfileRepository = recruiterProfileRepository;
        this.usersRepository = usersRepository;
    }

    public Optional<RecruiterProfile> getOne(long id) {
        return recruiterProfileRepository.findById(id);
    }

    public RecruiterProfile addNew(RecruiterProfile recruiterProfile) {
        return this.recruiterProfileRepository.save(recruiterProfile);
    }

    public RecruiterProfile getCurrentRecruiterProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUserName = authentication.getName();
            Users users = usersRepository.findByEmail(currentUserName).orElseThrow(() -> new UsernameNotFoundException("User Not Found"));
            Optional<RecruiterProfile> recruiterProfile = getOne(users.getUserId());
            return recruiterProfile.orElse(null);
        }
        return null;

    }

}
