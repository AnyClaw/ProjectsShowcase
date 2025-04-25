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
import org.springframework.web.bind.annotation.RequestParam;
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

    @GetMapping("/api/projects/active")
    public Iterable<ProjectFullInfo> allActiveProjects() {
        return projectRepository.findActiveProjects();
    }

    @GetMapping("/api/project/info/{id}")
    public ProjectFullInfo getProjectInfo(@PathVariable Long id) {
        return projectRepository.findById(id).get();
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

    @PostMapping("/api/project/request")
    public String createProjectRequest(@RequestBody ProjectFullInfo project) {
        projectRepository.save(project);
        return "saved";
    }

    @GetMapping("/api/role/info")
    public Map<String, String> getRoleInfo() {
        Map<String, String> response = new HashMap<>();
        MyUser user = MyUserDetailsService.getCurrentUserInfo();

        switch (user.getRole()) {
            case "ROLE_STUDENT" -> {
                Team team = teamRepository.findByTeammates_id(user.getId());
                response.put("role", "Студент");

                if (team != null)
                    response.put("teamName", team.getName());
                else
                    response.put("teamName", "Вы пока не состоите в команде");
            }

            case "ROLE_CUSTOMER" -> {
                response.put("role", "Заказчик");
            }
        }

        response.put("id", String.valueOf(user.getId()));
        
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

    @GetMapping("/api/affiliation/{userId}/{projectId}")
    public boolean projectAffiliation(@PathVariable Long userId, @PathVariable Long projectId) {
        MyUser user = userRepository.findById(userId).get();
        ProjectFullInfo project = projectRepository.findById(projectId).get();

        return user.getId() == project.getCustomer().getId();
    }

    //

    @GetMapping("/api/customer/projects")
    public List<ProjectFullInfo> getProjects() {
        return projectRepository.findByCustomerId(MyUserDetailsService.getCurrentUserInfo().getId());
    }

    @GetMapping("/user/info/{id}")
    public MyUser getUserInfo(@PathVariable Long id) {
        return userRepository.findById(id).get();
    }

    @GetMapping("/get/team/{userId}")
    public Team getTeam(@PathVariable Long userId) {
        return teamRepository.findByTeammates_id(userId);
    }

    @PostMapping("/add/team/{id}/{name}")
    public String createTeam(@PathVariable Long id, @PathVariable String name, 
        @RequestParam(required = false) List<Long> teammatesId) {

        MyUser teamlid = userRepository.findById(id).get();
        Team team = new Team(null, name, teamlid, List.of(teamlid), null, 
            new ArrayList<>(), new ArrayList<>());

        if (teammatesId != null && !teammatesId.isEmpty()) {
            for (Long teammateId : teammatesId) {
                team.addTeammate(userRepository.findById(teammateId).get());
            }
        }

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

    @PostMapping("/delete/team/{id}")
    public String deleteTeam(@PathVariable Long id) {
        teamRepository.delete(teamRepository.findById(id).get());
        return "deleted";
    }

    @PostMapping("/load")
    public String load() {
        parser.loadData();
        return "saved";
    }
}
