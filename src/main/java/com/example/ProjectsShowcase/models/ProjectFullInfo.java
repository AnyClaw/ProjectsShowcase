package com.example.ProjectsShowcase.models;

import jakarta.persistence.Column;
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
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
public class ProjectFullInfo {

    @Id
    private Long id;

    private String name;
    private String department;

    @Enumerated(EnumType.STRING)
    private Status status;

    private String goal;

    private String barrier;

    @Column(length = 500)
    private String decisions;

    public enum Status { 
        FREE, ON_WORK, COMPLETED, ON_VERIFICATION, CANCELED
        // свободен, в работе, завершён, на верификации, отказано в верификации
    }
}
