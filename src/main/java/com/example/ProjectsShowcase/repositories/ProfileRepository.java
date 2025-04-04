package com.example.ProjectsShowcase.repositories;

import org.springframework.data.repository.CrudRepository;

import com.example.ProjectsShowcase.models.Profile;

public interface ProfileRepository extends CrudRepository<Profile, Long> {
    
}
