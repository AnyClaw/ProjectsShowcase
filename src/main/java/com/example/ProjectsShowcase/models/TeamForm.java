package com.example.ProjectsShowcase.models;

import java.util.List;
import lombok.Data;

@Data
public class TeamForm {
    private String name;
    private List<Long> ids;
}