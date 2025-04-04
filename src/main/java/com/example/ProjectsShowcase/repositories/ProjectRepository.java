package com.example.ProjectsShowcase.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.example.ProjectsShowcase.models.ProjectFullInfo;
import com.example.ProjectsShowcase.models.Project.Status;

public interface ProjectRepository extends CrudRepository<ProjectFullInfo, Long> { 
    @Query("SELECT p FROM Project p WHERE p.status IN ('FREE', 'ON_WORK', 'COMPLETED')")
    Iterable<ProjectFullInfo> findMainPageCards();

    ProjectFullInfo findById(long id);

    @Query("SELECT p FROM Project p WHERE status IN (:statuses)")
    Iterable<ProjectFullInfo> findByStatuses(List<Status> statuses);
}
