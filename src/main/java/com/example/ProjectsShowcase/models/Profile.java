package com.example.ProjectsShowcase.models;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
public class Profile {
    
    @Id
    private final long id;

    private final String name;
    private final String surname;
    private final String patronymic;
    private final String phoneNumber;
    private final String mail;

    @Enumerated(EnumType.STRING)
    private final Role role;

    @OneToMany(cascade = CascadeType.ALL)
    private final List<ProjectFullInfo> projects;

    public enum Role {
        STUDENT, TEACHER, ADMIN, CUSTOMER
    }
}
