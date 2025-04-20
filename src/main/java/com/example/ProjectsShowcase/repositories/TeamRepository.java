package com.example.ProjectsShowcase.repositories;

import org.springframework.data.repository.CrudRepository;

import com.example.ProjectsShowcase.models.Team;

public interface TeamRepository extends CrudRepository<Team, Long> {
   Team findByTeammates_id(Long userId);
}