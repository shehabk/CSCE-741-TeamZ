package io.teamz.course;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

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

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class CourseServiceTest {

	@Autowired
	private CourseService courseService;
	
	@Test
	public void testGetAllCourses() {
		// With initial test data, a count of 1518 courses should be returned. 
		// However, in case test data changes, just assert that not 0 was returned.
		assertNotEquals("Failure - 0 courses returned", 0, courseService.getAllCourses().size());
	}
	
	@Test
	public void testGetDrMatthewsCourseById() {
		// Verify that amongst all the courses, that Dr Matthews CSCE 741 is listed.
		assertEquals("Failure - Could not find Dr Matthews 741 section", "Manton M. Matthews (P)", courseService.getCourse("201708CSCE741006-1").getInstructor());
	}
	
	@Test
	public void testCoursesByDept() {
		List<Course> courses = new ArrayList<Course>();

		// First course is BMEN 202
		courses.add(courseService.getCourse("201708BMEN202001-1"));
		
		// Second course is CSCE 274
		courses.add(courseService.getCourse("201708CSCE274001-1"));
		
		// Assert that only 1 course was in CSCE
		String dept = "CSCE";
		assertEquals("Failure - Incorrect number of courses in department CSCE", 1, courseService.getDept(courses, dept).size());	
	}
	
	@Test
	public void testGetFaculty() {
		List<Course> courses = new ArrayList<Course>();
		
		// First course is CSCE 741 taught by Dr Matthews
		courses.add(courseService.getCourse("201708CSCE741006-1"));
		
		// Second course is CSCE 740 taught by Dr Gay
		courses.add(courseService.getCourse("201708CSCE740001-1"));
		
		String faculty = "Manton M. Matthews (P)";
		
		// Since Dr Matthews only teaches one of those classes, assert the count = 1
		assertEquals("Test result getFaculty method: ", 1,
				courseService.getFaculty(courses, faculty).size());
	}
	
	@Test
	public void testGetUniqueDepts() {
		List<Course> courses = new ArrayList<Course>();
		
		// First Id is BMEN 101
		courses.add(courseService.getCourse("201708BMEN101001-1"));
		
		// Second Id is CSCE 101
		courses.add(courseService.getCourse("201708CSCE101001-1"));
		
		// Third Id is CSCE 102
		courses.add(courseService.getCourse("201801CSCE102001-1"));
		
		// Fourth Id is also ELCT 101
		courses.add(courseService.getCourse("201708ELCT101001-1"));
		
		// Because 2nd & 3rd course are both taught by CSCE, the unique depts should not be equal
		List<Course> result = courseService.getUniqueDepts(courses);
		assertNotEquals("Test result getUniqueDepts method: ", 
				result.size(), courses.size());
	}
	
	@Test
	public void testGetUniqueFaculties() {
		List<Course> courses = new ArrayList<Course>();
		
		// First Id is BMEN 101
		courses.add(courseService.getCourse("201708BMEN101001-1"));
		
		// Second Id is CSCE 101
		courses.add(courseService.getCourse("201708CSCE101001-1"));
		
		// Third Id is ELCT 101
		courses.add(courseService.getCourse("201708ELCT101001-1"));
		
		// Fourth Id is also ELCT 101
		courses.add(courseService.getCourse("201708ELCT101001-1"));
		
		// Because 3rd & 4th course are the same, they are taught by same faculty, counts should not be the same.
		List<Course> result = courseService.getUniqueFaculties(courses);
		
		assertNotEquals("Test result getUniqueFaculties method: ", 
				result.size(), courses.size());
	}
	
	@Test
	public void testGetTeachingSummary() {
		String result = courseService.getTeachingSummary();
		
		// Verify that calling TeachingSummary does not return an empty list of summaries
		assertTrue("Test result testgetTeachingSummary "
				+ "method: ", !result.isEmpty());
	}
	
	@Test
	public void testGetTeachingSummarybyDept() {
		// Verify that calling TeachingSummary by Dept does not return an empty list of summaries
		String result = courseService.getTeachingSummarybyDept();
		assertTrue("Test result getTeachingSummarybyDept "
				+ "method: ", !result.isEmpty());
	}
}