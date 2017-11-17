package io.teamz.course;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;






@RestController
public class CourseController {
	@Autowired
	private CourseService courseService;
	
	
	@RequestMapping("/courses")
	public List<Course> getAllCourses() {
		return courseService.getAllCourses();
	}
	
	@RequestMapping("/ReadFiles")
	public void readAllCourses() {
		courseService.readAllCourses();
		return;
	}

	
	@RequestMapping("/writeteachingSummary")
	public String writeTeachingSummary() {
		courseService.writeTeachingSummary();
		return "Summary Written to Files in Data Folder";
	}
	
	@RequestMapping("/teachingSummarybyDept")
	public String getTeachingSummarybyDept() {
		return courseService.getTeachingSummarybyDept();
		
	}
	
	@RequestMapping("/teachingSummary")
	public String getTeachingSummary() {
		return courseService.getTeachingSummary();
		
	}
	@RequestMapping("/courses/{id}")
	public Course getCourse(@PathVariable String id ) {
		return courseService.getCourse(id);
	}
	
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