package com.example.demo.selenium.integration;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CourseController {
	
	@Autowired
	private CourseService service;
	
	//Loads driver
	@RequestMapping("/loadDriver")
	public String loadDriver(){
		return service.loadDriver();
	}
	
	//Gets user's VIP credentials to login to my.sc.edu
	@RequestMapping(method=RequestMethod.POST, value="/login")
	public String login(@RequestBody Account credentials){
		return service.login(credentials);
	}

	//Returns all of the courses based on the semester and subject/department
	@RequestMapping("/getCourses/{sem}/{subj}")
	public List<Course> getCourses(@PathVariable String sem, @PathVariable String subj){
		return service.getCourses(sem, subj);
	}
	
	//Return all courses on my.sc.edu
	@RequestMapping("/getAllCourses")
	public List<Course> getAllCourses(){
		return service.getAllCourses();
	}
	
	//Saves all of the courses based on the semester and subject/department
	//in H2 database
	@RequestMapping("/saveCourses/{sem}/{subj}")
	public String saveCourses(@PathVariable String sem, @PathVariable String subj){
		return service.saveCourses(sem, subj);
	}
	
	//Saves all courses of my.sc.edu in H2 database
	@RequestMapping("/saveAllCourses")
	public String saveAllCourses(){
		return service.saveAllCourses();
	}
}
