package com.example.ProjectsShowcase.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.ProjectsShowcase.models.Profile;
import com.example.ProjectsShowcase.models.Project;
import com.example.ProjectsShowcase.models.ProjectFullInfo;
import com.example.ProjectsShowcase.models.Project.Status;
import com.example.ProjectsShowcase.repositories.ProfileRepository;
import com.example.ProjectsShowcase.repositories.ProjectRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class MainController {

    private final ProjectRepository projectRepository;
    private final ProfileRepository profileRepository;

    // Полная информация об 1 проекте
    @GetMapping("/fullInfo")
    public ProjectFullInfo fulInfo(@RequestParam("id") long id) {
        return projectRepository.findById(id);
    }

    // Краткая информация по всем проектам
    @GetMapping("/all")
    public Iterable<Project> getAllCards() {
        return ProjectFullInfo.toListShortProjects(projectRepository.findAll());
    }
 
    // Краткая информация по всем проектам со статусами 'FREE', 'ON_WORK', 'COMPLETED'
    @GetMapping("/mainPage")
    public Iterable<Project> getMainPageCards() {
        return ProjectFullInfo.toListShortProjects(projectRepository.findMainPageCards());
    }

    // Краткая информация по всем проектам со статусами 'FREE', 'ON_WORK', 'COMPLETED' с фильтром по статусам
    @GetMapping("/mainPage/status")
    public Iterable<Project> getCardsByStatus(@RequestParam List<String> statuses) {
        List<Status> statusConditions = statuses.stream().map(Status::valueOf).collect(Collectors.toList());
        return ProjectFullInfo.toListShortProjects(projectRepository.findByStatuses(statusConditions));
    }

    // Все профили
    @GetMapping("/profile")
    public Iterable<Profile> getAllProfiles() {
        return profileRepository.findAll();
    }
}
