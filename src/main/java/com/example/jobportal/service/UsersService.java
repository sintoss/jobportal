package com.example.jobportal.service;


import com.example.jobportal.entity.JobSeekerProfile;
import com.example.jobportal.entity.RecruiterProfile;
import com.example.jobportal.entity.Users;
import com.example.jobportal.repository.JobSeekerProfileRepository;
import com.example.jobportal.repository.RecruiterProfileRepository;
import com.example.jobportal.repository.UsersRepository;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class UsersService {

    private final PasswordEncoder passwordEncoder;
    private final UsersRepository usersRepository;
    private final JobSeekerProfileRepository jobSeekerProfileRepository;
    private final RecruiterProfileRepository recruiterProfileRepository;

    public UsersService(PasswordEncoder passwordEncoder, UsersRepository usersRepository, JobSeekerProfileRepository jobSeekerProfileRepository, RecruiterProfileRepository recruiterProfileRepository) {

        this.passwordEncoder = passwordEncoder;
        this.usersRepository = usersRepository;
        this.jobSeekerProfileRepository = jobSeekerProfileRepository;
        this.recruiterProfileRepository = recruiterProfileRepository;

    }

    public Users addNew(Users user) {

        user.setActive(true);
        user.setRegistrationDate(new Date(System.currentTimeMillis()));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Users savedUser = usersRepository.save(user);

        long typeID = user.getUserTypeId().getUserTypeId();

        if (typeID == 1) {
            recruiterProfileRepository.save(new RecruiterProfile(savedUser));
        } else {
            jobSeekerProfileRepository.save(new JobSeekerProfile(savedUser));
        }

        return savedUser;
    }

    public Optional<Users> getUserByEmail(String email) {
        return usersRepository.findByEmail(email);
    }

    public Users findByEmail(String currentUsername) {
        return usersRepository.findByEmail(currentUsername).orElseThrow(() -> new UsernameNotFoundException("User not " +
                "found"));
    }

    public Object getCurrentUserProfile() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String email = authentication.getName();

            Users users = usersRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User Not Found"));
            Long userId = users.getUserId();

            if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("Recruiter")))
                return recruiterProfileRepository.findById(userId).orElse(new RecruiterProfile());
            else
                return jobSeekerProfileRepository.findById(userId).orElse(new JobSeekerProfile());


        }

        return null;
    }

    public Users getCurrentUser() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!(authentication instanceof AnonymousAuthenticationToken)) {

            String email = authentication.getName();

            return usersRepository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("User Not Found"));
        }
        return null;
    }
}
