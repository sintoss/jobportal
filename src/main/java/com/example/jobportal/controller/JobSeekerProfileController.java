package com.example.jobportal.controller;


import com.example.jobportal.entity.JobSeekerProfile;
import com.example.jobportal.entity.Skills;
import com.example.jobportal.entity.Users;
import com.example.jobportal.service.JobSeekerProfileService;
import com.example.jobportal.service.UsersService;
import com.example.jobportal.util.FileDownloadUtil;
import com.example.jobportal.util.FileUploadUtil;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/job-seeker-profile")
public class JobSeekerProfileController {

    private final JobSeekerProfileService jobSeekerProfileService;
    private final UsersService usersService;

    public JobSeekerProfileController(JobSeekerProfileService jobSeekerProfileService, UsersService usersService) {
        this.jobSeekerProfileService = jobSeekerProfileService;
        this.usersService = usersService;
    }


    @GetMapping("/")
    public String jobSeekerProfile(Model model) {

        JobSeekerProfile jobSeekerProfile = new JobSeekerProfile();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<Skills> skills = new ArrayList<>();

        if (!(authentication instanceof AnonymousAuthenticationToken)) {

            String username = authentication.getName();
            Users users = usersService.getUserByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User Not Found"));
            Optional<JobSeekerProfile> seekerProfile = jobSeekerProfileService.getOne(users.getUserId());

            if (seekerProfile.isPresent()) {
                jobSeekerProfile = seekerProfile.get();
                if (jobSeekerProfile.getSkills().isEmpty()) {
                    skills.add(new Skills());
                    jobSeekerProfile.setSkills(skills);
                }
            }
            model.addAttribute("skills", skills);
            model.addAttribute("profile", jobSeekerProfile);

        }
        return "job-seeker-profile";
    }

    @PostMapping("/addNew")
    public String addNewJobSeekerProfile(JobSeekerProfile jobSeekerProfile, @RequestParam("image") MultipartFile image, @RequestParam("pdf") MultipartFile pdf, Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String username = authentication.getName();
            Users users = usersService.getUserByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User Not Found"));
            jobSeekerProfile.setUserId(users);
            jobSeekerProfile.setUserAccountId(users.getUserId());

        }
        List<Skills> skills = new ArrayList<>();
        model.addAttribute("skills", skills);
        model.addAttribute("profile", jobSeekerProfile);

        for (Skills skill : jobSeekerProfile.getSkills()) {
            skill.setJobSeekerProfile(jobSeekerProfile);
        }

        String imageName = FileUploadUtil.getFileName(image);
        String resumeName = FileUploadUtil.getFileName(pdf);

        jobSeekerProfile.setProfilePhoto(imageName);
        jobSeekerProfile.setResume(resumeName);

        JobSeekerProfile seekerProfile = jobSeekerProfileService.addNew(jobSeekerProfile);

        try {

            String uploadDir = "photos/candidate/" + jobSeekerProfile.getUserAccountId();
            FileUploadUtil.saveFile(uploadDir, imageName, image);
            FileUploadUtil.saveFile(uploadDir, resumeName, pdf);

        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
        return "redirect:/dashboard/";
    }

    @GetMapping("/{id}")
    public String candidateProfile(@PathVariable("id") long id, Model model) {
        Optional<JobSeekerProfile> seekerProfile = jobSeekerProfileService.getOne(id);
        model.addAttribute("profile", seekerProfile);
        return "job-seeker-profile";
    }

    @GetMapping("/downloadResume")
    public ResponseEntity<?> downloadResume(@RequestParam("fileName") String fileName, @RequestParam(value = "userID") String userId) {

        FileDownloadUtil fileDownloadUtil = new FileDownloadUtil();
        Resource resource = null;

        try {
            resource = fileDownloadUtil.getFileAsResource("photos/candidate/" + userId, fileName);
        } catch (IOException ioe) {
            return ResponseEntity.badRequest().build();
        }

        String contentType = "application/octet-stream";
        String headerValue = "attachment; filename=\"" + resource.getFilename() + "\"";
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, headerValue)
                .body(resource);
    }
}
