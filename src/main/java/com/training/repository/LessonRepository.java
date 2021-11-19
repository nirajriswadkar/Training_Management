package com.training.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.training.entity.Lesson;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long>{

    public <T> T findApplicationTitleBy(Class<T> clazz);
    public <T> T findAllBy(Class<T> clazz);
    public Lesson findByName(String name);
    public List<Lesson> findByIsActiveAndCourses_id(boolean isActive, String course_id);
}