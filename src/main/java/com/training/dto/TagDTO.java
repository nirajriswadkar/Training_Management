package com.training.dto;

import java.util.Set;

public class TagDTO {

    private Long id;
    private String name;
    private String description;
    private Set<VideoDTO> videos;
    
    public Set<VideoDTO> getVideos() {
		return videos;
	}
	public void setVideos(Set<VideoDTO> videos) {
		this.videos = videos;
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
	


}
