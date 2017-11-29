package io.teamz.course;

public class Account {

	String username;
	String pass;
	
	public Account(){
		
	}
	
	public Account(String username, String pass){
		super();
		this.username = username;
		this.pass = pass;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}
	
	
}
