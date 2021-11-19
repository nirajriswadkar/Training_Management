package com.training.entity;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

@Entity
public class Video {

	@Id
    private Long id;
	
	@Column
    private String title;
    private String description;
    private String link;
    private boolean isActive;
    
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinTable(name = "video_tag", 
    joinColumns = @JoinColumn(name = "video_id", referencedColumnName = "id"), 
    inverseJoinColumns = @JoinColumn(name = "tag_id", referencedColumnName = "id"))
    private Set<Tag> tags;

	@ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "video_lesson", 
    joinColumns = @JoinColumn(name = "video_id", referencedColumnName = "id"), 
    inverseJoinColumns = @JoinColumn(name = "lesson_id", referencedColumnName = "id"))
    private Set<Lesson> lessons;
	
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
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public Set<Lesson> getLessons() {
		return lessons;
	}
	public void setLessons(Set<Lesson> lessons) {
		this.lessons = lessons;
	}
	public Set<Tag> getTags() {
		return tags;
	}
	public void setTags(Set<Tag> tags) {
		this.tags = tags;
	}
	public boolean isActive() {
		return isActive;
	}
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
}
