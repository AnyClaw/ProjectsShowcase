package com.example.ProjectsShowcase.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.ProjectsShowcase.configurations.MyUserDetails;
import com.example.ProjectsShowcase.models.MyUser;
import com.example.ProjectsShowcase.models.ProjectFullInfo;
import com.example.ProjectsShowcase.models.Team;
import com.example.ProjectsShowcase.repositories.UserRepository;
import com.example.ProjectsShowcase.services.Parser;
import com.example.ProjectsShowcase.repositories.ProjectRepository;
import com.example.ProjectsShowcase.repositories.TeamRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class MainController {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final TeamRepository teamRepository;
    private final PasswordEncoder encoder;
    private final Parser parser;

    // Полная информация о всех проектах
    // admin only
    @GetMapping("/all")
    // @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public Iterable<ProjectFullInfo> allProjects() {
        return projectRepository.findAll();
    }

    // Полная информация о всех активных проектах
    @GetMapping("/api/projects/active")
    public Iterable<ProjectFullInfo> allActiveProjects() {
        return projectRepository.findActiveProjects();
    }

    @GetMapping("/api/projects/user")
    public Iterable<ProjectFullInfo> userProjects() {
        Team team = teamRepository.findByTeammates_Id(getUserInfo().getId()).get();

        return team.getCompletedProjects();
    }

    @GetMapping("/api/user/info")
    public MyUser getUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try {
            MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
            return userDetails.getUser();
        } catch (Exception e) {
            return null;
        }
    }

    @PostMapping("/api/project/request")
    public void createProjectRequest(@RequestBody ProjectFullInfo project) {
        projectRepository.save(project);
    }

    //

    @PostMapping("/add/team")
    public String addTeam(@RequestParam Long teamlidId) {
        Team team = new Team(teamlidId, "Чебуречища", userRepository.findById(teamlidId).get(), 
            new ArrayList<MyUser>(),  null, new ArrayList<ProjectFullInfo>(), new ArrayList<ProjectFullInfo>());

        teamRepository.save(team);
        
        return "saved";
    }

    @PostMapping("/add/user")
    public String addUser(@RequestBody MyUser user) {
        user.setPassword(encoder.encode(user.getPassword()));
        userRepository.save(user);
        return "saved";
    }

    @GetMapping("/user/{id}")
    public MyUser getUser(@PathVariable Long id) {
        return userRepository.findById(id).get();
    }

    @PostMapping("/delete/user/{id}")
    public String deleteUser(@PathVariable Long id) {
        userRepository.delete(userRepository.findById(id).get());
        return "deleted";
    }

    @PostMapping("/delete/project/{id}")
    public String deleteProject(@PathVariable Long id) {
        projectRepository.delete(projectRepository.findById(id).get());
        return "deleted";
    }

    @PostMapping("/load")
    public String load() {
        parser.loadData();
        return "saved";
    }
}
