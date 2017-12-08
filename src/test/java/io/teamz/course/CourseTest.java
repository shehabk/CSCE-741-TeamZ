package io.teamz.course;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class CourseTest {

	@Autowired
	private Course testCourse;
	
	@Before
	public void setUp() throws Exception {
		// Initialize our testCourse with data from the 741 course
		testCourse = new Course("201708CSCE741001-1","CSCE","201708"," ","25398","CSCE","741","001","COL","3.000","30 - Columbia Full Term","Software Process","MW","02:20 pm-03:35 pm","40","8","32","Manton M. Matthews (P)","08/24-12/08","WMBB 409"," ");
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetId() {
		assertEquals("Failure - getId incorrect", "201708CSCE741001-1", testCourse.getId());
	}
	
	// The various get/set operations do not perform any validation and just assign
	// and return a string value. Nevertheless, verify a handful of them are working.
	// Ideally, every get/set would be tested.
	@Test
	public void testSetId() {
		testCourse.setId("x201708CSCE741001-1");
		assertEquals("Failure - setId incorrect", "x201708CSCE741001-1", testCourse.getId());
	}
	
	@Test
	public void testGetDept() {
		assertEquals("Failure - getDept incorrect", "CSCE", testCourse.getDept());
	}
	
	@Test
	public void testSetDept() {
		testCourse.setDept("xCSCE");
		assertEquals("Failure - setDept incorrect", "xCSCE", testCourse.getDept());
	}
	
	@Test
	public void testGetCrse() {
		assertEquals("Failure - getCrse incorrect", "741", testCourse.getCrse());
	}
	
	@Test
	public void testSetCrse() {
		testCourse.setCrse("x741");
		assertEquals("Failure - setCrse incorrect", "x741", testCourse.getCrse());
	}

	@Test
	public void testGetInstructor() {
		assertEquals("Failure - getInstructor incorrect", "Manton M. Matthews (P)", testCourse.getInstructor());
	}
	
	@Test
	public void testSetInstructor() {
		testCourse.setInstructor("xManton M. Matthews (P)");
		assertEquals("Failure - setInstructor incorrect", "xManton M. Matthews (P)", testCourse.getInstructor());
	}
	
	@Test
	public void testSetAndGetInstructorWithIrishName() {
		testCourse.setInstructor("Apo'strophe Mac Dougle");
		assertEquals("Failure - set/getInstructor incorrect with apostrophe and spaces", "Apo'strophe Mac Dougle", testCourse.getInstructor());
	}
	
	@Test
	public void testVerifyNoCrossFieldContamination() {
		testCourse.setAttribute("My Attribute");
		testCourse.setCap("99");;
		testCourse.setDate("Today");
		testCourse.setCred("0");
		
		// Verify that the setting of on set of fields, does not impact the value in other fields.
		assertEquals("Failure - setId incorrect", "201708CSCE741001-1", testCourse.getId());
		assertEquals("Failure - getDept incorrect", "CSCE", testCourse.getDept());
		assertEquals("Failure - getCrse incorrect", "741", testCourse.getCrse());
		assertEquals("Failure - setInstructor incorrect", "Manton M. Matthews (P)", testCourse.getInstructor());
	}
	
	@Test
	public void testToString() {
		assertNotNull("Failure - toString returned Null", testCourse.toString());
	}
}