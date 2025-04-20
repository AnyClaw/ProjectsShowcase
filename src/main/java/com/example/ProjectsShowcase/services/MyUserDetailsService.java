package com.example.ProjectsShowcase.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.ProjectsShowcase.configurations.MyUserDetails;
import com.example.ProjectsShowcase.models.MyUser;
import com.example.ProjectsShowcase.repositories.UserRepository;

@Service
public class MyUserDetailsService implements UserDetailsService {
    
    @Autowired
    private UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<MyUser> user = repository.findByName(username);
        return user.map(MyUserDetails::new).orElseThrow(() -> new UsernameNotFoundException(username + " not found"));
    }

    public static MyUser getCurrentUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try {
            MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
            return userDetails.getUser();
        } catch (Exception e) {
            return null;
        }
    }
}
