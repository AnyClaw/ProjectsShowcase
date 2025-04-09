package com.example.ProjectsShowcase.models;

import java.util.List;

import com.example.ProjectsShowcase.models.ProjectFullInfo.Status;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Team {

    @Id
    private long id;

    private String name;

    // Лидер команды
    @JoinColumn(name = "teamlid")
    @ManyToOne
    private MyUser teamlid;

    // члены команды
    @OneToMany(cascade = CascadeType.ALL)
    @Column(name = "teammates")
    private List<MyUser> teammates;

    // текущий проект
    @JoinColumn(name = "current_project")
    @ManyToOne
    private ProjectFullInfo currentProject;

    // завершённые проекты
    @OneToMany(cascade = CascadeType.ALL)
    @Column(name = "completed_projects")
    private List<ProjectFullInfo> completedProjects;

    // отказанные от работы проекты
    @OneToMany(cascade = CascadeType.ALL)
    @Column(name = "refused_projects")
    private List<ProjectFullInfo> refusedProjects;

    public void addTeammate(MyUser teammate) {
        teammates.add(teammate);
    }

    public void setCurrentProject(ProjectFullInfo project) {
        if (project.getStatus() == Status.FREE)
            this.currentProject = project;
    }

    public void finishProject() {
        currentProject.setStatus(Status.COMPLETED);
        completedProjects.add(currentProject);
        currentProject = null;
    }

    public void refuseProject(String reason) {
        currentProject.setStatus(Status.FREE);
        refusedProjects.add(currentProject);
        currentProject = null;
    } 
}
