package io.teamz.course;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertNotSame;
import java.util.ArrayList;
import java.util.List;

public class CourseServiceTest {

	@Autowired
	private CourseService courseService;
	
	@Test
	public void testgetAllCourses() {
		// Check result from getAllCourses is not null
		assertNotNull("Test result getAllCourses method: ", 
					courseService.getAllCourses());
	}
	
	@Test
	public void testgetgetCourse() {
		// Check result from getCourse is not null
		assertNotNull("Test result getCourse method: ", courseService.getCourse("c1"));
	}
	
	@Test
	public void testgetDept() {
		List<Course> courses = new ArrayList<Course>();
		courses.add(courseService.getCourse("course_id1"));
		courses.add(courseService.getCourse("course_id2"));
		String dept = "dept name";
		assertNotNull("Test result getDept method: ", 
				courseService.getDept(courses, dept));
	}
	
	@Test
	public void testgetFaculty() {
		List<Course> courses = new ArrayList<Course>();
		courses.add(courseService.getCourse("course_id1"));
		courses.add(courseService.getCourse("course_id2"));
		String faculty = "faculty name";
		assertNotNull("Test result getFaculty method: ", 
				courseService.getFaculty(courses, faculty));
	}
	
	@Test
	public void testgetUniqueDepts() {
		List<Course> courses = new ArrayList<Course>();
		courses.add(courseService.getCourse("course_id1"));
		courses.add(courseService.getCourse("course_id2"));
		courses.add(courseService.getCourse("course_id2"));
		courses.add(courseService.getCourse("course_id3"));
		List<Course> result = courseService.getUniqueDepts(courses);
		assertNotEquals("Test result getUniqueDepts method: ", 
				result.size(), courses.size());
	}
	
	@Test
	public void testgetUniqueFaculties() {
		List<Course> courses = new ArrayList<Course>();
		courses.add(courseService.getCourse("course_id1"));
		courses.add(courseService.getCourse("course_id2"));
		courses.add(courseService.getCourse("course_id2"));
		courses.add(courseService.getCourse("course_id3"));
		List<Course> result = courseService.getUniqueFaculties(courses);
		assertNotEquals("Test result getUniqueFaculties method: ", 
				result.size(), courses.size());
	}
	
	@Test
	public void testgetTeachingSummary() {
		String result = courseService.getTeachingSummary();
		assertTrue("Test result testgetTeachingSummary "
				+ "method: ", !result.isEmpty());
	}
	
	@Test
	public void testgetTeachingSummarybyDept() {
		String result = courseService.getTeachingSummarybyDept();
		assertTrue("Test result getTeachingSummarybyDept "
				+ "method: ", !result.isEmpty());
	}
	
}
