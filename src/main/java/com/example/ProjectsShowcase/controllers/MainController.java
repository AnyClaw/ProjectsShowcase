package com.example.ProjectsShowcase.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.ProjectsShowcase.models.MyUser;
import com.example.ProjectsShowcase.models.ProjectFullInfo;
import com.example.ProjectsShowcase.models.Team;
import com.example.ProjectsShowcase.repositories.UserRepository;
import com.example.ProjectsShowcase.services.MyUserDetailsService;
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

    // Полная информация о всех активных проектах
    @GetMapping("/api/projects/active")
    public Iterable<ProjectFullInfo> allActiveProjects() {
        return projectRepository.findActiveProjects();
    }

    @GetMapping("/api/user/info")
    public MyUser getCurrentUserInfo() {
        try {
            return userRepository.findById(MyUserDetailsService.getCurrentUserInfo().getId()).get();
        }
        catch (Exception e) {
            return null;
        }
    }

    @GetMapping("/api/user/info/{id}")
    public MyUser getUserInfo(@PathVariable Long id) {
        return userRepository.findById(id).get();
    }

    @PostMapping("/api/project/request")
    public String createProjectRequest(@RequestBody ProjectFullInfo project) {
        projectRepository.save(project);
        return "saved";
    }

    @GetMapping("/api/team/getName")
    public Map<String, String> getTeamName() {
        Map<String, String> response = new HashMap<>();
        try {
            Team team = teamRepository.findByTeammates_id(MyUserDetailsService.getCurrentUserInfo().getId());
            response.put("name", team.getName());
        }
        catch (Exception e) {
            response.put("name", "Вы пока не состоите в команде");
        }
        
        return response;
    }

    @GetMapping("/api/team/info")
    public Team getTeamInfo() {
        return teamRepository.findByTeammates_id(MyUserDetailsService.getCurrentUserInfo().getId());
    }

    @GetMapping("/api/team/projects")
    public Map<String, List<ProjectFullInfo>> getTeamProjects() {
        try {
            Map<String, List<ProjectFullInfo>> teamProjects = new HashMap<>();
            Team team = teamRepository.findByTeammates_id(MyUserDetailsService.getCurrentUserInfo().getId());

            teamProjects.put("current", List.of(team.getCurrentProject()));
            teamProjects.put("completed", team.getCompletedProjects());
            teamProjects.put("refused", team.getRefusedProjects());

            return teamProjects;
        }
        catch (Exception e) {
            return null;
        }
    }

    //

    @GetMapping("/get/team/{userId}")
    public Team getTeam(@PathVariable Long userId) {
        return teamRepository.findByTeammates_id(userId);
    }

    @PostMapping("/add/team")
    public String createTeam() {
        MyUser teamlid = userRepository.findById(MyUserDetailsService.getCurrentUserInfo().getId()).get();
        Team team = new Team(null, "Чебуречища", teamlid, new ArrayList<>(), null, new ArrayList<>(), new ArrayList<>());
        team.addTeammate(teamlid);
        teamRepository.save(team);
        return "saved";
    }

    @PostMapping("/add/teammate/{teamlidId}/{teammateId}")
    public String addTeammate(@PathVariable Long teamlidId, @PathVariable Long teammateId) {
        Team team = teamRepository.findByTeammates_id(teamlidId);
        MyUser teammate = userRepository.findById(teammateId).get();
        team.addTeammate(teammate);
        teamRepository.save(team);
        return "added";
    }

    @PostMapping("/add/team/project/{teamId}/{projectId}")
    public String takeProject(@PathVariable Long teamId, @PathVariable Long projectId) {
        Team team = teamRepository.findById(teamId).get();
        ProjectFullInfo project = projectRepository.findById(projectId).get();
        team.setCurrentProject(project);
        teamRepository.save(team);
        return "saved";
    }

    @PostMapping("/team/finish/{teamId}")
    public String finishProject(@PathVariable Long teamId) {
        Team team = teamRepository.findById(teamId).get();
        team.finishProject();
        teamRepository.save(team);
        return "finished";
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
