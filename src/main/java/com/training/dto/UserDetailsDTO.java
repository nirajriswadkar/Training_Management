package com.training.dto;

import java.util.Set;

public class UserDetailsDTO {

    private Long id;
    private String name;
    private String description;
    private String address;
    private String contact_number;
    private String email;
    private boolean is_instructor = false; 
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
    
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getContact_number() {
		return contact_number;
	}
	public void setContact_number(String contact_number) {
		this.contact_number = contact_number;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public boolean isIs_instructor() {
		return is_instructor;
	}
	public void setIs_instructor(boolean is_instructor) {
		this.is_instructor = is_instructor;
	}
}
