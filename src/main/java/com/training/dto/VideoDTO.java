package com.training.dto;

import java.util.Set;

public class VideoDTO {

    private Long id;
    private String title;
    private String description;
    private String link;
    private Set<TagDTO> tags;
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
	public Set<LessonDTO> getLessons() {
		return lessons;
	}
	public void setLessons(Set<LessonDTO> lessons) {
		this.lessons = lessons;
	}
	public Set<TagDTO> getTags() {
		return tags;
	}
	public void setTags(Set<TagDTO> tags) {
		this.tags = tags;
	}
	public boolean isActive() {
		return isActive;
	}
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
}
