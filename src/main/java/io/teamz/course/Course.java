package io.teamz.course;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Course {
    @Id    
    private String id;
	private String dept;
	private String semester ;	
	private String selection ;
	private String crn ;
	private String subj ;
	private String crse ;
	private String sec ;
	private String cmp ;
	private String cred ;
	private String part_of_term ;
	private String title ;
	private String days ;
	private String time ;
	private String cap ;
	private String act ;
	private String rem ;
	private String instructor ;
	private String date;
	private String location ;
	private String attribute;
	
	public Course() {
		super();
	}
	
	public Course(String id, String dept, String semester, String select, String crn, String subj, String crse,
			String sec, String cmp, String cred, String part_of_term, String title, String days, String time,
			String cap, String act, String rem, String instructor, String date, String location, String attribute) {
		super();
		this.id = id;
		this.dept = dept;
		this.semester = semester;
		this.selection = select;
		this.crn = crn;
		this.subj = subj;
		this.crse = crse;
		this.sec = sec;
		this.cmp = cmp;
		this.cred = cred;
		this.part_of_term = part_of_term;
		this.title = title;
		this.days = days;
		this.time = time;
		this.cap = cap;
		this.act = act;
		this.rem = rem;
		this.instructor = instructor;
		this.date = date;
		this.location = location;
		this.attribute = attribute;
	}

	public Course (String singeLine , String _id , String _dept , String _semester ) {
		super();
		id = _id ;
		dept = _dept ;
		semester = _semester;
		String[] entries = singeLine.split("\t");
		selection  = entries[0].trim() ;
		crn  = entries[1].trim() ;
		subj  = entries[2].trim()  ;
		crse  = entries[3].trim()  ;
		sec  = entries[4].trim()  ;
		cmp  = entries[5].trim()  ;
		cred  = entries[6].trim()  ;
		part_of_term  = entries[7].trim()  ;
		title  = entries[8].trim()  ;
		days  = entries[9].trim()  ;
		time  = entries[10].trim()  ;
		cap  = entries[11].trim()  ;
		act  = entries[12].trim()  ;
		rem  = entries[13].trim()  ;
		instructor  = entries[14].trim()  ;
		date  = entries[15].trim()  ;
		location  = entries[16].trim()  ;
		if( entries.length >=18)
			attribute = entries[17].trim()  ;
		else 
			attribute = "" ;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDept() {
		return dept;
	}

	public void setDept(String dept) {
		this.dept = dept;
	}

	public String getSemester() {
		return semester;
	}

	public void setSemester(String semester) {
		this.semester = semester;
	}

	public String getSelection() {
		return selection;
	}

	public void setSelection(String selection) {
		this.selection = selection;
	}

	public String getCrn() {
		return crn;
	}

	public void setCrn(String crn) {
		this.crn = crn;
	}

	public String getSubj() {
		return subj;
	}

	public void setSubj(String subj) {
		this.subj = subj;
	}

	public String getCrse() {
		return crse;
	}

	public void setCrse(String crse) {
		this.crse = crse;
	}

	public String getSec() {
		return sec;
	}

	public void setSec(String sec) {
		this.sec = sec;
	}

	public String getCmp() {
		return cmp;
	}

	public void setCmp(String cmp) {
		this.cmp = cmp;
	}

	public String getCred() {
		return cred;
	}

	public void setCred(String cred) {
		this.cred = cred;
	}

	public String getPart_of_term() {
		return part_of_term;
	}

	public void setPart_of_term(String part_of_term) {
		this.part_of_term = part_of_term;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDays() {
		return days;
	}

	public void setDays(String days) {
		this.days = days;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getCap() {
		return cap;
	}

	public void setCap(String cap) {
		this.cap = cap;
	}

	public String getAct() {
		return act;
	}

	public void setAct(String act) {
		this.act = act;
	}

	public String getRem() {
		return rem;
	}

	public void setRem(String rem) {
		this.rem = rem;
	}

	public String getInstructor() {
		return instructor;
	}

	public void setInstructor(String instructor) {
		this.instructor = instructor;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getAttribute() {
		return attribute;
	}

	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}


	public String toString(){
		String ret = "";
		ret += " ID: "+ this.getId();
		ret += " Selection: " + this.getSelection();
		ret += " CRN: " + this.getCrn();
		ret += " Subject: " + this.getSubj();
		ret += " CRSE: " + this.getCrse();
		ret += " SEC: " + this.getSec();
		ret += " CMP: " + this.getCmp();
		ret += " Cred: " + this.getCred();
		ret += " Part of Term: " + this.getPart_of_term();
		ret += " Title: " + this.getTitle();
		ret += " Days: " + this.getDays();
		ret += " Time: " + this.getTime();
		ret += " Cap: " + this.getCap();
		ret += " Act: " + this.getAct();
		ret += " Rem: " + this.getRem();
		ret += " Instructor: " + this.getInstructor();
		ret += " Date: " + this.getDate();
		ret += " Location: " + this.getLocation();
		ret += " Attribute: " + this.getAttribute();
		ret += "\n";
		return ret;
	}




}
