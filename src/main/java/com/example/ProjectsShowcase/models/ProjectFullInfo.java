package com.example.ProjectsShowcase.models;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ProjectFullInfo extends Project {
    
    private String barrier;

    @Column(length = 500)
    private String decisions;
    
    public ProjectFullInfo(
        long id, String name, String department, Status status, String goal,
        String barrier, String decisions) {
            
        super(id, name, department, status, goal);
        this.barrier = barrier;
        this.decisions = decisions;
    }

    public Project toShortProject() {
        return new Project(getId(), getName(), getDepartment(), getStatus(), getGoal());
    }

    public static List<Project> toListShortProjects(Iterable<ProjectFullInfo> projectsFullInfo) {
        List<Project> projects = new ArrayList<>();
        
        for (ProjectFullInfo projectFullInfo : projectsFullInfo) {
            projects.add(projectFullInfo.toShortProject());
        }

        return projects;
    }
}
