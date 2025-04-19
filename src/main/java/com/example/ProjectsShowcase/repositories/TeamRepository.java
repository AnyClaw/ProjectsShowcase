package com.example.ProjectsShowcase.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.example.ProjectsShowcase.models.Team;

public interface TeamRepository extends CrudRepository<Team, Long> {
    Optional<Team> findByTeammates_Id(Long id);
}