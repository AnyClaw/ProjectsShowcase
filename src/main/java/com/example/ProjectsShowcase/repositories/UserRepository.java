package com.example.ProjectsShowcase.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.example.ProjectsShowcase.models.MyUser;

public interface UserRepository extends CrudRepository<MyUser, Long> {
    Optional<MyUser> findByName(String username);
}
