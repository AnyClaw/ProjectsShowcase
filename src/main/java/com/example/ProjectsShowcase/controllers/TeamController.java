package com.example.ProjectsShowcase.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ProjectsShowcase.models.MyUser;
import com.example.ProjectsShowcase.models.ProjectFullInfo;
import com.example.ProjectsShowcase.models.Team;
import com.example.ProjectsShowcase.models.TeamForm;
import com.example.ProjectsShowcase.repositories.TeamRepository;
import com.example.ProjectsShowcase.repositories.UserRepository;
import com.example.ProjectsShowcase.services.MyUserDetailsService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/team")
@RequiredArgsConstructor
public class TeamController {

    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    
    @GetMapping("/info")
    public Team getTeamInfo() {
        return teamRepository.findByTeammates_id(MyUserDetailsService.getCurrentUserInfo().getId());
    }

    @GetMapping("/projects")
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

    @PostMapping("/finish")
    public String finishProject() {
        MyUser user = MyUserDetailsService.getCurrentUserInfo();
        Team team = teamRepository.findByTeammates_id(user.getId());
        team.finishProject();
        teamRepository.save(team);
        return "finished";
    }

    @PostMapping("/create")
    public String createTeam(@RequestBody TeamForm teamfForm) {
        MyUser teamlid = userRepository.findById(MyUserDetailsService.getCurrentUserInfo().getId()).get();
        Team team = new Team(null, teamfForm.getName(), teamlid, new ArrayList<>(), 
            null, new ArrayList<>(), new ArrayList<>());

        team.addTeammate(teamlid);
        for (long id: teamfForm.getIds()) {
            team.addTeammate(userRepository.findById(id).get());
        }

        teamRepository.save(team);

        return "saved";
    }

    @GetMapping("/project/{id}")
    public Team getTeamByProject(@PathVariable Long id) {
        return teamRepository.findTeamByCurrentProjectId(id);
    }
}
