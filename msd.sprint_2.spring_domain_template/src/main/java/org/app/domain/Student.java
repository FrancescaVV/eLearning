package org.app.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Student extends Person {

	private String studFaculty;
	private String studSpecialization;
	@Id @GeneratedValue
	private String studId;
	public String getStudFaculty() {
		return studFaculty;
	}
	public void setStudFaculty(String studFaculty) {
		this.studFaculty = studFaculty;
	}
	public String getStudSpecialization() {
		return studSpecialization;
	}
	public void setStudSpecialization(String studSpecialization) {
		this.studSpecialization = studSpecialization;
	}
	public String getStudId() {
		return studId;
	}
	public void setStudId(String studId) {
		this.studId = studId;
	}
	
	
}
