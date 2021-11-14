package org.app.domain.assignment;

import java.util.ArrayList;
import java.util.Date;

import org.app.domain.users.Professor;
import org.app.domain.users.Student;

public class Meetings {
	private String meetingBody;
	private Date meetingStartDate;
	private Date meetingEndDate;
	private ArrayList<Student> meetingMembers = new ArrayList<Student>();
	private Professor mentor;
	private String meetingObs;
	private String meetingTopic;
	private MeetingState meeetingState; 
	
	
	public String getMeetingBody() {
		return meetingBody;
	}
	public void setMeetingBody(String meetingBody) {
		this.meetingBody = meetingBody;
	}
	public Date getMeetingStartDate() {
		return meetingStartDate;
	}
	public void setMeetingStartDate(Date meetingStartDate) {
		this.meetingStartDate = meetingStartDate;
	}
	public Date getMeetingEndDate() {
		return meetingEndDate;
	}
	public void setMeetingEndDate(Date meetingEndDate) {
		this.meetingEndDate = meetingEndDate;
	}
	public ArrayList<Student> getAgMembers() {
		return meetingMembers;
	}
	public void setAgMembers(ArrayList<Student> agMembers) {
		this.meetingMembers = agMembers;
	}
	public Professor getMentor() {
		return mentor;
	}
	public void setMentor(Professor mentor) {
		this.mentor = mentor;
	}
	public String getMeetingObs() {
		return meetingObs;
	}
	public void setMeetingObs(String meetingObs) {
		this.meetingObs = meetingObs;
	}
	public String getMeetingTopic() {
		return meetingTopic;
	}
	public void setMeetingTopic(String meetingTopic) {
		this.meetingTopic = meetingTopic;
	}
	
	public ArrayList<Student> getMeetingMembers() {
		return meetingMembers;
	}
	public void setMeetingMembers(ArrayList<Student> meetingMembers) {
		this.meetingMembers = meetingMembers;
	}
	public MeetingState getMeeetingState() {
		return meeetingState;
	}
	public void setMeeetingState(MeetingState meeetingState) {
		this.meeetingState = meeetingState;
	}
	
	@Override
	public String toString() {
		return "Meetings [meetingBody=" + meetingBody + ", meetingStartDate=" + meetingStartDate + ", meetingEndDate="
				+ meetingEndDate + ", meetingTopic=" + meetingTopic + "]";
	}
	
}