package com.training.entity;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

@Entity
public class Course {

	@Id
    private Long id;
	
	@Column
    private String title;
	private String description;
    private String name;    
    private boolean isActive;
    
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "subject_course", 
    joinColumns = @JoinColumn(name = "course_id", referencedColumnName = "id"), 
    inverseJoinColumns = @JoinColumn(name = "subject_id", referencedColumnName = "id"))
    private Set<Subject> subjects;
    
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "course_lesson", 
    joinColumns = @JoinColumn(name = "course_id", referencedColumnName = "id"), 
    inverseJoinColumns = @JoinColumn(name = "lesson_id", referencedColumnName = "id"))
    private Set<Lesson> lessons;
    
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "user_course", 
    joinColumns = @JoinColumn(name = "course_id", referencedColumnName = "id"), 
    inverseJoinColumns = @JoinColumn(name = "userDetails_id", referencedColumnName = "id"))
    private Set<UserDetails> users;
    
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
	public Set<Subject> getSubjects() {
		return subjects;
	}
	public void setSubjects(Set<Subject> subjects) {
		this.subjects = subjects;
	}
	public Set<Lesson> getLessons() {
		return lessons;
	}
	public void setLessons(Set<Lesson> lessons) {
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
	public Set<UserDetails> getUsers() {
		return users;
	}
	public void setUsers(Set<UserDetails> users) {
		this.users = users;
	}
}
