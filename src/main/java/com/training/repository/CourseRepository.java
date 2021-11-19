package com.training.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.training.entity.Course;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long>{

    public <T> T findApplicationTitleBy(Class<T> clazz);
    public <T> T findAllBy(Class<T> clazz);
    public Course findByName(String name);
    public List<Course> findByIsActive(boolean isActive);
    public List<Course> findBySubjects_Name(String subject_name);
}