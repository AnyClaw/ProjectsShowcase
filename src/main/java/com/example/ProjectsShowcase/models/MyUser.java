package com.example.ProjectsShowcase.models;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
public class MyUser {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String surname;
    private String patronymic;
    private String phoneNumber;

    @Column(unique = true)
    private String mail; // username
    private String password;
    private String roles;

    @OneToMany(cascade = CascadeType.ALL)
    private List<ProjectFullInfo> projects;

    public void addProject(ProjectFullInfo project) {
        projects.add(project);
    }
}
