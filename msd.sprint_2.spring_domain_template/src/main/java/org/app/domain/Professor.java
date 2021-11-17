package org.app.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
public class Professor extends Person {
	@NotEmpty
	private String profDepartment;
	@NotEmpty
	private String profFaculty;

	private String profTitle;
	@Id @GeneratedValue @NotNull
	private Integer profId;
	public String getProfDepartment() {
		return profDepartment;
	}
	public void setProfDepartment(String profDepartment) {
		this.profDepartment = profDepartment;
	}
	public String getProfFaculty() {
		return profFaculty;
	}
	public void setProfFaculty(String profFaculty) {
		this.profFaculty = profFaculty;
	}
	public String getProfTitle() {
		return profTitle;
	}
	public void setProfTitle(String profTitle) {
		this.profTitle = profTitle;
	}
	public Integer getProfId() {
		return profId;
	}
	public void setProfId(Integer profId) {
		this.profId = profId;
	}
	
}
