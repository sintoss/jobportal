package com.example.jobportal.controller;


import com.example.jobportal.entity.RecruiterProfile;
import com.example.jobportal.entity.Users;
import com.example.jobportal.service.RecruiterProfileService;
import com.example.jobportal.service.UsersService;
import com.example.jobportal.util.FileUploadUtil;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Controller
@RequestMapping("/recruiter-profile")
public class RecruiterProfileController {

    private final UsersService usersService;
    private final RecruiterProfileService recruiterProfileService;


    public RecruiterProfileController(UsersService usersService, RecruiterProfileService recruiterProfileService) {
        this.usersService = usersService;
        this.recruiterProfileService = recruiterProfileService;
    }

    @GetMapping("/")
    public String recruiterProfile(Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!(authentication instanceof AnonymousAuthenticationToken)) {

            String username = authentication.getName();
            Users users = usersService.getUserByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
            Optional<RecruiterProfile> recruiterProfile = recruiterProfileService.getOne(users.getUserId());

            recruiterProfile.ifPresent(profile -> model.addAttribute("profile", profile));
        }

        return "recruiter_profile";
    }

    @PostMapping("/addNew")
    public String addNewRecruiterProfile(RecruiterProfile recruiterProfile, @RequestParam("image") MultipartFile multipartFile, Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String username = authentication.getName();
            Users users = usersService.getUserByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
            recruiterProfile.setUserId(users);
            recruiterProfile.setUserAccountId(users.getUserId());
        }

        model.addAttribute("profile", recruiterProfile);

        String fileName = FileUploadUtil.getFileName(multipartFile);
        recruiterProfile.setProfilePhoto(fileName);

        RecruiterProfile savedUser = recruiterProfileService.addNew(recruiterProfile);

        String uploadDir = "photos/recruiter/" + savedUser.getUserAccountId();

        try {
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "redirect:/dashboard/";
    }

}
