package com.example.ProjectsShowcase.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ProjectsShowcase.models.MyUser;
import com.example.ProjectsShowcase.models.ProjectFullInfo;
import com.example.ProjectsShowcase.models.Team;
import com.example.ProjectsShowcase.repositories.ProjectRepository;
import com.example.ProjectsShowcase.repositories.TeamRepository;
import com.example.ProjectsShowcase.repositories.UserRepository;
import com.example.ProjectsShowcase.services.MyUserDetailsService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    private final TeamRepository teamRepository;
    private final ProjectRepository projectRepository;
    
    @GetMapping("/info")
    public MyUser getCurrentUserInfo() {
        try {
            return userRepository.findById(MyUserDetailsService.getCurrentUserInfo().getId()).get();
        }
        catch (Exception e) {
            return null;
        }
    }

    @GetMapping("/role")
    public Map<String, String> getRoleInfo() {
        Map<String, String> response = new HashMap<>();
        MyUser user = MyUserDetailsService.getCurrentUserInfo();

        switch (user.getRole()) {
            case "ROLE_STUDENT", "ROLE_ADMIN" -> {
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

    @GetMapping("/affiliation/{projectId}")
    public boolean projectAffiliation(@PathVariable Long projectId) {
        MyUser user = MyUserDetailsService.getCurrentUserInfo();
        ProjectFullInfo project = projectRepository.findById(projectId).get();

        return user.getId() == project.getCustomer().getId();
    }

    @GetMapping("/customer/projects")
    public List<ProjectFullInfo> getProjects() {
        return projectRepository.findByCustomerId(MyUserDetailsService.getCurrentUserInfo().getId());
    }

    @GetMapping("/find/{post}")
    public Map<String, String> findUser(@PathVariable String post) {
        Map<String, String> response = new HashMap<>();
        Optional<MyUser> user = userRepository.findByPost(post);

        if (!user.isEmpty() && !user.get().getId().equals(MyUserDetailsService.getCurrentUserInfo().getId())) {
            response.put("isExist", "true");
            response.put("id", String.valueOf(user.get().getId()));
        }
        else response.put("isExist", "false");

        return response;
    }
}
