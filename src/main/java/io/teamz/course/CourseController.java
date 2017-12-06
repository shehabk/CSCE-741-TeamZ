package io.teamz.course;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;








@RestController
public class CourseController {
	@Autowired
	private CourseService courseService;
	
	
	
	//Loads driver
	@RequestMapping("/loadDriver")
	public String loadDriver(){
		return courseService.loadDriver();
	}
	
	 
	//Gets user's VIP credentials to login to my.sc.edu
	@RequestMapping(method=RequestMethod.POST, value="/login")
	public String login(@RequestBody Account credentials){
		return courseService.login(credentials);

	}
	
	@RequestMapping("/saveCourses/{sem}/{subj}")
	public String saveCourses(@PathVariable String sem, @PathVariable String subj){
		return courseService.saveCourses(sem, subj);

	}
	
	
	
	//Saves all courses of my.sc.edu in H2 database
	@RequestMapping("/saveAllCourses")
	public String saveAllCourses(){
		return courseService.saveAllCourses();
	}
	
	
//	This returns a list of all courses to the web client
	@RequestMapping("/courses")
	public List<Course> getAllCourses() {
		return courseService.getAllCourses();
	}
//	This method reads the files from ./data and
//	stores them into database.
	@RequestMapping("/readFiles")
	public String readAllCourses() {
		courseService.readAllCourses();
		return "Data Loaded Successfully";
	}

//	This method writes the teaching summary to
//	teachingSummary.html and teachingSummarybyDept file in ./data folder
	@RequestMapping("/writeteachingSummary")
	public String writeTeachingSummary() {
		courseService.writeTeachingSummary();
		return "Summary Written to Files in Data Folder";
	}
//	This method returns the teaching summary by department to the web client
	@RequestMapping("/teachingSummarybyDept")
	public String getTeachingSummarybyDept() {
		return courseService.getTeachingSummarybyDept();
		
	}
//	This method returns the teaching summary by courses to the web client
	@RequestMapping("/teachingSummary")
	public String getTeachingSummary() {
		return courseService.getTeachingSummary();
		
	}
	
//	
//	The following methods were copied from the preivous projects
//	I am not sure if they are necessary for our final project
//	
	
	@RequestMapping("/courses/{id}")
	public Course getCourse(@PathVariable String id ) {
		return courseService.getCourse(id);
	}
	
	@CrossOrigin
	@RequestMapping(method=RequestMethod.POST, value= "/courses")
	public void addCourse(@RequestBody Course course) {
		courseService.addCourse(course);
	}
	
	@RequestMapping(method=RequestMethod.PUT, value= "/courses/{id}")
	public void updateCourse(@RequestBody Course course, @PathVariable String id ) {
		courseService.updateCourse(id, course);
	}
	
	@RequestMapping(method=RequestMethod.DELETE, value= "/courses/{id}")
	public void deleteCourse(@PathVariable String id ) {
		courseService.deleteCourse(id);
	}

}