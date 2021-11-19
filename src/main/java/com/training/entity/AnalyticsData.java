package com.training.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class AnalyticsData {

	@Id
    private Long id;
	
	@Column
    private Long entityid;
	private String entitytype;
	private Long viewcount;
	
	public Long getViewcount() {
		return viewcount;
	}
	public void setViewcount(Long viewcount) {
		this.viewcount = viewcount;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getEntityid() {
		return entityid;
	}
	public void setEntityid(Long entityid) {
		this.entityid = entityid;
	}
	public String getEntitytype() {
		return entitytype;
	}
	public void setEntitytype(String entitytype) {
		this.entitytype = entitytype;
	}
}
