package io.javabrains.springbootstarter.course;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.util.function.Function;
import java.util.function.Predicate;




@Service
public class CourseService {
	
	@Autowired
	private CourseRepository courseRepository ;
	private List<Course> courses = new ArrayList< Course >() ; 

//	https://stackoverflow.com/questions/23699371/java-8-distinct-by-property
	public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
	    Map<Object,Boolean> seen = new ConcurrentHashMap<>();
	    return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
	}


	
	public List<Course> getAllCourses(){
		List<Course> courses = new ArrayList<>();
		courseRepository.findAll()
		.forEach(courses::add);
		return courses;
		
	}	

	public Course getCourse(String id ) {
		return courseRepository.findOne(id);
	}

	public void addCourse(Course course) {
		courseRepository.save(course);
	}

	public void updateCourse(String id, Course course) {
		courseRepository.save(course);
	}

	public void deleteCourse(String id) {
		courseRepository.delete(id);
	}

	public List<Course> getDept(List<Course> courses , String dept ) {

		return courses.stream().filter(t -> t.getDept().equals(dept)).collect(Collectors.toList());

	}
	
	public List<Course> getFaculty(List<Course> courses , String faculty ) {

		return courses.stream().filter(t -> t.getInstructor().equals(faculty)).collect(Collectors.toList());

	}
	
	public List<Course> getUniqueDepts( List<Course> courses ) {	

		return courses.stream().filter(distinctByKey(p -> p.getDept())).collect(Collectors.toList());

	}
	
	public List<Course> getUniqueFaculties(  List<Course> courses ) {		

		return courses.stream().filter(distinctByKey(p -> p.getInstructor())).collect(Collectors.toList());

	}
	public void writeTeachingSummary() {
		PrintWriter teachingSummary = null ;
		PrintWriter teachingSummarybyDept = null ;
		try {
			teachingSummary = new PrintWriter("./data/teachingSummary.html", "UTF-8");
			teachingSummarybyDept = new PrintWriter("./data/teachingSummarybyDept.html", "UTF-8");
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		teachingSummary.println(getTeachingSummary());
		teachingSummarybyDept.println(getTeachingSummarybyDept());
		
		teachingSummary.close();
		teachingSummarybyDept.close();
		
	}
	
	
	public String getTeachingSummary() {
		List<Course> courses = new ArrayList<>();
		courses = getAllCourses();
		String returnString = "" ;
		List<Course> depts = getUniqueDepts( courses ) ;		
		for( int i=0 ; i<depts.size() ; i++ ) {
			returnString += String.format("<b>Dept: %s</b><br/>", depts.get(i).getDept());
			List<Course> coursesInDept = getDept(courses, depts.get(i).getDept());
			List<Course> facultiesInDept = getUniqueFaculties( coursesInDept );

			for ( int j =0 ; j<facultiesInDept.size(); j++ ) {
				List<Course> coursesTaughtByFaculty = getFaculty(coursesInDept ,
						facultiesInDept.get(j).getInstructor());
				int numberOfSection = 0 ;
				int numberOfStudents = 0 ;
				int studentCreditHours =0;
				double FTE = 0 ;
				
				for (int k = 0 ; k< coursesTaughtByFaculty.size() ; k++ ) {
					numberOfSection++;
					
					try {
					numberOfStudents+=Integer.parseInt(
							coursesTaughtByFaculty.get(k).getAct());
					studentCreditHours+= (int)Double.parseDouble(
							coursesTaughtByFaculty.get(k).getCred())*Integer.parseInt(
									coursesTaughtByFaculty.get(k).getAct());
					} catch(NumberFormatException e) {
						
					}
				}
				
				FTE = (double) studentCreditHours / 15.0 ;
				returnString += String.format("<b>Name</b>: %s, " +
						"<b>#Section</b>: %d, " +
						"<b>#Students</b>: %d, " +
						"<b>#FTE</b>: %.2f, " +
						"<b>#Credit Hours</b>: %d <br/>", 
						facultiesInDept.get(j).getInstructor(), numberOfSection ,
						numberOfStudents, FTE , studentCreditHours
						);
				
			}
		}
		return returnString;
	}

	
	public String getTeachingSummarybyDept() {
		List<Course> courses = new ArrayList<>();
		courses = getAllCourses();
		String returnString = "" ;
//		Get the number of unique departments to loop through		
		List<Course> depts = getUniqueDepts( courses ) ;	
		
		for( int i=0 ; i<depts.size() ; i++ ) {
//			Get the courses on each department.
			List<Course> coursesInDept = getDept(courses, depts.get(i).getDept());


			int numberOfSection = 0 ;
			int numberOfStudents = 0 ;
			int studentCreditHours =0;
			double FTE = 0 ;
//			Loop through each course and collect information of 
			for (int k = 0 ; k< coursesInDept.size() ; k++ ) {
				numberOfSection++;
				
				try {
				numberOfStudents+=Integer.parseInt(
						coursesInDept.get(k).getAct());
				studentCreditHours+= (int)Double.parseDouble(
						coursesInDept.get(k).getCred())*Integer.parseInt(
								coursesInDept.get(k).getAct());
				} catch(NumberFormatException e) {
					
				}
			}
			
			FTE = (double) studentCreditHours / 15.0 ;
			returnString += String.format("<b>Dept</b>: %s, " +
					"<b>#Section</b>: %d, " +
					"<b>#Students</b>: %d, " +
					"<b>#FTE</b>: %.2f, " +
					"<b>#Credit Hours</b>: %d <br/>", 
					depts.get(i).getDept(), numberOfSection ,
					numberOfStudents, FTE , studentCreditHours
					);
				

		}
		return returnString;
	}

	public void readAllCourses() {
		// TODO Auto-generated method stub
		File folder  =  new File("./data") ;
		File[] listOfFiles = folder.listFiles() ;  


		int i_id =0  ;
		String id ;		
		for (int i = 0; i < listOfFiles.length; i++) {
			String dept ="" ;
			String semester="" ;
			String line = "";
			Course holderCourseObject= null;	
			int iftext = listOfFiles[i].getName().indexOf(".txt");
			if(iftext == -1) {
				continue ;
			}
			if (listOfFiles[i].isFile()) {				
	        	String [] entries = listOfFiles[i].getName().split("_");
	        	dept = entries[0] ;
	        	semester =  entries[1].substring(0, entries[1].lastIndexOf('.')) ;	        	
	          }	        

	        try {   	  	

	    	  	Scanner scanner = new Scanner(listOfFiles[i]);
		        while (scanner.hasNextLine()) {
		        	++i_id ;
		        	id = String.valueOf(i_id);
		        	line = scanner.nextLine();	
		        	holderCourseObject = new Course(line,id ,dept,semester) ;
		        	addCourse(holderCourseObject);
		        }
		        scanner.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}    

	  
	      }
		
		
	}




	


}
