package com.example.ProjectsShowcase.controllers;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.ProjectsShowcase.models.MyUser;
import com.example.ProjectsShowcase.models.ProjectFullInfo;
import com.example.ProjectsShowcase.models.Team;
import com.example.ProjectsShowcase.models.ProjectFullInfo.Status;
import com.example.ProjectsShowcase.repositories.UserRepository;
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

    // Полная информация о всех проектах
    // admin only
    @GetMapping("/all")
    // @PreAuthorize("hasAuthority('ROLE_ADMIN')")
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

    @PostMapping("/new/user")
    public String newUser(@RequestBody MyUser user, @RequestParam(required = false) Long teamId) {
        user.setPassword(encoder.encode(user.getPassword()));

        if (teamId != null){
            Team team = teamRepository.findById(teamId).get();
            team.addTeammate(user);
        }
        
        userRepository.save(user);

        return "saved";
    }

    @PostMapping("/new/team/{teamlidId}")
    public String newTeam(@RequestBody Team team, @PathVariable Long teamlidId, @RequestParam(required = false) List<Long> teammatesIds, 
            @RequestParam(required = false) Long currentProjectId, @RequestParam(required = false) List<Long> completedProjectsIds) {

        team.setTeamlid(userRepository.findById(teamlidId).get());

        if (teammatesIds != null) {
            List<MyUser> teammates = new ArrayList<>();

            for (Long id : teammatesIds) {
                teammates.add(userRepository.findById(id).get());
            }

            team.setTeammates(teammates);
        }

        if (currentProjectId != null) 
            team.setCurrentProject(projectRepository.findById(currentProjectId).get());

        if (completedProjectsIds != null) {
            List<ProjectFullInfo> projects = new ArrayList<>();

            for (Long id : completedProjectsIds) {
                projects.add(projectRepository.findById(id).get());
            }

            team.setCompletedProjects(projects);
        }

        teamRepository.save(team);

        return "saved";
    }

    @PostMapping("/add/teammate")
    public String addTeammate(@RequestParam Long teamId, @RequestParam Long userId) {
        Team team = teamRepository.findById(teamId).get();
        team.addTeammate(userRepository.findById(userId).get());
        teamRepository.save(team);

        return "added";
    }

    @PostMapping("/add/currentProject")
    public String takeProject(@RequestParam Long teamId, @RequestParam Long projectId) {
        Team team = teamRepository.findById(teamId).get();
        team.setCurrentProject(projectRepository.findById(projectId).get());
        teamRepository.save(team);

        return "added";
    }

    @PostMapping("/finishProject")
    public String finishProject(@RequestParam Long teamId) {
        Team team = teamRepository.findById(teamId).get();

        if (team.getCurrentProject() != null) {
            team.finishProject();
            teamRepository.save(team);
            return "finished";
        }

        return "project is no exist";
    }

    @PostMapping("/refuseProject")
    public String refuseProject(@RequestParam Long teamId) {
        Team team = teamRepository.findById(teamId).get();

        if (team.getCurrentProject() != null) {
            team.refuseProject("Захотелось");
            teamRepository.save(team);
            return "refused";
        }

        return "project is no exist";
    }

    @GetMapping("/teams")
    public Iterable<Team> getAllTeams() {
        return teamRepository.findAll();
    }

    @GetMapping("/users")
    public Iterable<MyUser> getUsers() {
        return userRepository.findAll();
    }
}
