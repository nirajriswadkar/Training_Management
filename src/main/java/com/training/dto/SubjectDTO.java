package com.training.dto;

import java.util.Set;

public class SubjectDTO {

    private Long id;
    private String title;
	private String description;
    private String name;        
    private Set<CourseDTO> courses;
    
	public Set<CourseDTO> getCourses() {
		return courses;
	}
	public void setCourses(Set<CourseDTO> courses) {
		this.courses = courses;
	}
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


}
