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
public class Tag {

	@Id
    private Long id;
	
	@Column
    private String name;
    private String description;
    
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "video_tag", 
    joinColumns = @JoinColumn(name = "tag_id", referencedColumnName = "id"), 
    inverseJoinColumns = @JoinColumn(name = "video_id", referencedColumnName = "id"))
    private Set<Video> videos;
    
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
	public Set<Video> getVideos() {
		return videos;
	}
	public void setVideos(Set<Video> videos) {
		this.videos = videos;
	}

}
