package com.example.ProjectsShowcase.controllers;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ProjectsShowcase.models.MyUser;
import com.example.ProjectsShowcase.repositories.UserRepository;
import com.example.ProjectsShowcase.services.Parser;

import lombok.RequiredArgsConstructor;


@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final Parser parser;

    @PostMapping("/add/user")
    public String addUser(@RequestBody MyUser user) {
        user.setPassword(encoder.encode(user.getPassword()));
        user.setDefaultPost();
        userRepository.save(user);
        return "saved";
    }

    @PostMapping("/load")
    public void load() {
        parser.loadData();
    }
}
