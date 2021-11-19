package com.training.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.training.entity.Video;

@Repository
public interface VideoRepository extends JpaRepository<Video, Long>{

    public <T> T findApplicationTitleBy(Class<T> clazz);
    public <T> T findAllBy(Class<T> clazz);
    public Video findByTitle(String title);
    public List<Video> findByIsActiveAndLessons_id(boolean isActive, String lessons_id);
    public List<Video> findByTitleAndTags_NameIn(String video_title, List<String> tag_names);
}
