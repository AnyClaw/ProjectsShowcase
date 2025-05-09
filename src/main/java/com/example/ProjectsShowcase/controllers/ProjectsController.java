package com.example.ProjectsShowcase.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ProjectsShowcase.models.MyUser;
import com.example.ProjectsShowcase.models.ProjectFullInfo;
import com.example.ProjectsShowcase.models.ProjectFullInfo.Status;
import com.example.ProjectsShowcase.models.Team;
import com.example.ProjectsShowcase.repositories.ProjectRepository;
import com.example.ProjectsShowcase.repositories.TeamRepository;
import com.example.ProjectsShowcase.services.MyUserDetailsService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectsController {
    
    private final ProjectRepository projectRepository;
    private final TeamRepository teamRepository;

    @GetMapping("/active")
    public Iterable<ProjectFullInfo> allActiveProjects() {
        return projectRepository.findActiveProjects();
    }

    @GetMapping("/info/{id}")
    public ProjectFullInfo getProjectInfo(@PathVariable Long id) {
        return projectRepository.findById(id).get();
    }

    @PostMapping("/request")
    public String createProjectRequest(@RequestBody ProjectFullInfo project) {
        projectRepository.save(project);
        return "saved";
    }

    @PostMapping("/book/{id}")
    public Map<String, String> bookProject(@PathVariable Long id) {
        MyUser user = MyUserDetailsService.getCurrentUserInfo();
        Team team = teamRepository.findByTeammates_id(user.getId());
        ProjectFullInfo project = projectRepository.findById(id).get();
        Map<String, String> response = new HashMap<>();
        String responseStatus = "Забронированно!";

        if (team != null) {
            if (user.equals(team.getTeamlid()) && team.getCurrentProject() == null) {
                team.setCurrentProject(projectRepository.findById(id).get());
                teamRepository.save(team);

                project.setStatus(Status.ON_WORK);
                projectRepository.save(project);
            }
            else if (team.getCurrentProject() != null)
                responseStatus = "Ошибка! У вас уже есть забронированный проект!";
            else responseStatus = "Ошибка! Вы не являетесь лидером команды!";
        }
        else responseStatus = "Ошибка! Вы не состоите в команде!";
        
        response.put("Ответ", responseStatus);
        return response;
    }
}
