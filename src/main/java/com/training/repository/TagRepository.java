package com.training.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.training.entity.Tag;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long>{

    public <T> T findApplicationTitleBy(Class<T> clazz);
    public <T> T findAllBy(Class<T> clazz);
    public Tag findByName(String name);
}