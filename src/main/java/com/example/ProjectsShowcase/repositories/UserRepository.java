package com.example.ProjectsShowcase.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.example.ProjectsShowcase.models.MyUser;

public interface UserRepository extends CrudRepository<MyUser, Long> {
    Optional<MyUser> findByMail(String mail);
    boolean existsByPost(String post);
    Optional<MyUser> findByPost(String post);
}
