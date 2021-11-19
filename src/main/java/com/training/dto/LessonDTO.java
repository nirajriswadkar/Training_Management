package com.training.dto;

import java.util.Set;

public class LessonDTO {

    private Long id;
    private String name;
    private String description;
    private Set<CourseDTO> courses;    
    private Set<VideoDTO> videos;
    private boolean isActive;
    
    public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public Set<CourseDTO> getCourses() {
		return courses;
	}
	public void setCourses(Set<CourseDTO> courses) {
		this.courses = courses;
	}
	public Set<VideoDTO> getVideos() {
		return videos;
	}
	public void setVideos(Set<VideoDTO> videos) {
		this.videos = videos;
	}
	public boolean isActive() {
		return isActive;
	}
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
}
