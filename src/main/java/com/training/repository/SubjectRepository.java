package com.training.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.training.entity.Subject;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long>{

    public <T> T findApplicationTitleBy(Class<T> clazz);
    public <T> T findAllBy(Class<T> clazz);
    public Subject findByName(String name);
}