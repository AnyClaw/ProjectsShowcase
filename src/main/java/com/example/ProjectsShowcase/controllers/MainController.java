package com.example.ProjectsShowcase.controllers;


import java.util.List;
import java.util.Optional;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.ProjectsShowcase.models.MyUser;
import com.example.ProjectsShowcase.models.ProjectFullInfo;
import com.example.ProjectsShowcase.models.ProjectFullInfo.Status;
import com.example.ProjectsShowcase.repositories.UserRepository;
import com.example.ProjectsShowcase.repositories.ProjectRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class MainController {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    // Полная информация о всех проектах
    // admin only
    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public Iterable<ProjectFullInfo> allProjects() {
        return projectRepository.findAll();
    }

    // Полная информация о всех активных проектах
    @GetMapping("projects/active")
    public Iterable<ProjectFullInfo> allActiveProjects() {
        return projectRepository.findActiveProjects();
    }

    // Полная информация всех проектах с фильтром статусом
    @GetMapping("/projects/active/{statuses}")
    public Iterable<ProjectFullInfo> allProjectsByStatuses(@PathVariable List<Status> statuses) {
        return projectRepository.findByStatuses(statuses);
    }

    // Полная информация об 1 проекте
    // authorized only
    @GetMapping("/project/{id}")
    public Optional<ProjectFullInfo> projectFullInfo(@PathVariable long id) {
        return projectRepository.findById(id);
    }

    @PostMapping("/new/{id}")
    public String newUser(@RequestBody MyUser user, @PathVariable long id) {
        user.setPassword(encoder.encode(user.getPassword()));
        user.addProject(projectRepository.findById(id).get());
        userRepository.save(user);

        return "saved";
    }

    @GetMapping("/users")
    public Iterable<MyUser> getUsers() {
        return userRepository.findAll();
    }
}
