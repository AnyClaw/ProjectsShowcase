package com.example.ProjectsShowcase;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class ProjectsShowcaseApplication implements WebMvcConfigurer {

	public static void main(String[] args) {
		SpringApplication.run(ProjectsShowcaseApplication.class, args);
	}

	@Override
	public void addViewControllers(ViewControllerRegistry viewControllerRegistry) {
		viewControllerRegistry.addViewController("/").setViewName("showcase.html");
		viewControllerRegistry.addViewController("/project/request").setViewName("project_request.html");
		viewControllerRegistry.addViewController("/profile").setViewName("profile.html");
		viewControllerRegistry.addViewController("/team").setViewName("team.html");
	}
}
