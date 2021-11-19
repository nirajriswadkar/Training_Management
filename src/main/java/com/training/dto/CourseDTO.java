package com.training.dto;

import java.util.Set;

public class CourseDTO {

    private Long id;
    private String title;
	private String description;
    private String name;        
    private Set<SubjectDTO> subjects;    
    private Set<LessonDTO> lessons;
    private boolean isActive;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Set<SubjectDTO> getSubjects() {
		return subjects;
	}
	public void setSubjects(Set<SubjectDTO> subjects) {
		this.subjects = subjects;
	}
	public Set<LessonDTO> getLessons() {
		return lessons;
	}
	public void setLessons(Set<LessonDTO> lessons) {
		this.lessons = lessons;
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
	public boolean isActive() {
		return isActive;
	}
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

}
