package com.example.ProjectsShowcase.models;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC, force = true)
public class Project {

    @Id
    private long id;

    private String name;
    private String department;

    @Enumerated(EnumType.STRING)
    private Status status;

    private String goal;

    public enum Status { 
        FREE, ON_WORK, COMPLETED, ON_VERIFICATION, CANCELED
    }
}
