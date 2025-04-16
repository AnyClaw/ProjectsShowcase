package com.example.ProjectsShowcase.models;

import java.util.List;

import com.example.ProjectsShowcase.models.ProjectFullInfo.Status;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
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
    @OneToOne
    private MyUser teamlid;

    // члены команды
    @OneToMany(cascade = CascadeType.ALL)
    private List<MyUser> teammates;

    // текущий проект
    @OneToOne
    private ProjectFullInfo currentProject;

    // завершённые проекты
    @OneToMany(cascade = CascadeType.ALL)
    private List<ProjectFullInfo> completedProjects;

    // отказанные от работы проекты
    @OneToMany(cascade = CascadeType.ALL)
    private List<ProjectFullInfo> refusedProjects;

    public void addTeammate(MyUser teammate) {
        teammates.add(teammate);
    }

    public void setCurrentProject(ProjectFullInfo project) {
        if (project.getStatus() == Status.FREE) {
            this.currentProject = project;
            currentProject.setStatus(Status.ON_WORK);
        }
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
