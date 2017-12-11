package io.teamz.course;

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
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.apache.commons.lang3.SystemUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import io.teamz.course.Course;

import java.util.function.Function;
import java.util.function.Predicate;

@Service
public class CourseService {
	
	
	static WebDriver driver;
	@Autowired
	private CourseRepository courseRepository ;
	private List<Course> courses = new ArrayList< Course >() ; 
	
	
	
	public String loadDriver(){
		String ret = "";
		//Save path to chromedriver executable
		/*Download ChromeDriver from:
			https://sites.google.com/a/chromium.org/chromedriver/downloads
		*/
		String exePath = "";
		//If OS is Linux, use Linux executable
		if(SystemUtils.IS_OS_LINUX){
			System.out.println("Linux OS");
			exePath = "./data/linux_chromedriver";
		}
		//Else if OS is Windows, use Windows executable
		else if(SystemUtils.IS_OS_WINDOWS){
			System.out.println("Windows OS");
			exePath = "\\data\\win_chromedriver.exe";
		}
		//Assume OS is MAC_OS
		else{
			System.out.println("MAC OS");
		    exePath ="./data/mac_chromedriver";
		}
		//Set the property of the web driver's chrome driver to the executable's path
		System.setProperty("webdriver.chrome.driver", exePath);
		//Initailize ChromeDriver
		ChromeOptions chromeOptions = new ChromeOptions();
		chromeOptions.addArguments("start-maximized");
		chromeOptions.addArguments("disable-infobars");
		driver = new ChromeDriver(chromeOptions);
		//Go to my.sc.edu
		driver.get("https://my.sc.edu/");
		ret = "Webdriver successfully loaded";
		return ret;
	}
	
	
	//Uses POST request to login to my.sc.edu
	public String login(Account credentials){
		String ret = "";
		
		if(credentials == null || credentials.pass.isEmpty()
				|| credentials.username.isEmpty()){
			return "Must enter VIP Username and Password";
		}else{
			String vipID = credentials.username;
			String vipPass = credentials.pass;
			driver.findElement(By.linkText("Sign in to Self Service Carolina (SSC)")).click();
			
			//Enter credentials
			driver.findElement(By.id("generic-username")).sendKeys(vipID);
			driver.findElement(By.id("generic-password")).sendKeys(vipPass);
			
			//Click 'Login' button
			driver.findElement(By.name("submit")).click();
			//Switch to iFrame
			driver.switchTo().frame("duo_iframe");
			driver.findElement(By.xpath("//button[text()=' Send Me a Push ']")).click();
			
			try{
				//Wait 30sec for user to grant DUO access through mobile device
				driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
				
				//SUCCESSFULLY LOGGED INTO MY.SC
				driver.findElement(By.id("bmenu--P_StuMainMnu___UID1")).click();
				driver.findElement(By.id("bmenu--P_RegMnu___UID1")).click();
				driver.findElement(By.id("contentItem12")).click();
				
				ret = "Successfully logged into my.sc.edu!";
			}catch(NoSuchElementException e){
				e.printStackTrace();
				ret = "NoSuchElementException: Must enter DUO Codes within 30 seconds";
			}
			
			
			return ret;
		}
	
	}
	
	//Saves all of the courses based on the semester and subject/department
	public String saveCourses(String sem, String subj){
		String status = "";
		List<Course> courses = new ArrayList<Course>();
		
		//Select semester
		Select semesterSelect = new Select(driver.findElement(By.id("term_input_id")));
		List<WebElement> semesterOptions = semesterSelect.getOptions();
		boolean semValid = false;
		for (WebElement option : semesterOptions) {
			//See if semester described can be chosen from available semesters
			if(option.getText().contains(sem)){
			    	semValid = true;
			    	sem = option.getAttribute("value");		    
			}	    
		}
		//If the user has chosen a valid semester, continue with program
		if(semValid){
			semesterSelect.selectByValue(sem);
			driver.findElement(By.cssSelector("button[type='submit']")).click();
		}else{
			return "Semester input is invalid"; 
		}
		
		//Select USC-Columbia campus
		Select campus = new Select(driver.findElement(By.id("camp_id")));
		campus.selectByValue("COL");
		
		//Select department/subject
		Select subjSelect = new Select(driver.findElement(By.id("subj_id")));
		boolean validSubj = false;
		List<WebElement> subjOptions = subjSelect.getOptions();
		for (WebElement option : subjOptions) {
			if(option.getAttribute("value").equals(subj)){
				validSubj = true;
			}
		}
		if(validSubj){
			subjSelect.selectByValue(subj);
			driver.findElement(By.cssSelector("button[value='Course Search']")).click();
		}else{
			return "Subject entered is invalid";
		}
		
		courses = scrapeCourses(driver, sem);
		driver.navigate().back();
		driver.navigate().back();
		
//		driver.close();
		status = "Courses successfully saved to database";
		
		return status;
	}
	
	
	//Saves all of the courses in my.sc.edu
	public String saveAllCourses(){
		//Saves all of the courses in my.sc.edu
		//From Spring 2018 to Fall 2013. That's 14 semesters including
		//summer semesters
			long startTime = System.nanoTime();
			String status = "";
			List<Course> courses = new ArrayList<Course>();
			
			//Loop through each semester
			//Skip the first option of the Semester Select; "None"
			Select semesterSelect = new Select(driver.findElement(By.cssSelector("select#term_input_id")));
			//driver.findElement(By.ByCssSelector)
			List<WebElement> semesterOptions = semesterSelect.getOptions();
			for (int i = 1; i < semesterOptions.size(); i++) {
				driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
				semesterSelect = new Select(driver.findElement(By.cssSelector("select#term_input_id")));
				semesterSelect.selectByIndex(i);
				String sem = semesterSelect.getFirstSelectedOption().getText();
				
				//Remove ' (View Only)' from any selection option text
				String remove = " (View only)";
				if(sem.contains(remove)){
					sem = sem.replace(remove, "");		
				}
			
				status = sem; 
				driver.findElement(By.cssSelector("button[type='submit']")).click();
				
				//Wait 5sec for element to load
				//driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
				
				//Select USC-Columbia campus
				Select campus = new Select(driver.findElement(By.cssSelector("select#camp_id")));
				campus.selectByValue("COL");
					
				//Search each subject/department
				Select subjSelect = new Select(driver.findElement(By.cssSelector("select#subj_id")));
				List<WebElement> subjOptions = subjSelect.getOptions();
				for (int j = 0; j < subjOptions.size(); j++) {
					driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);

					subjSelect = new Select(driver.findElement(By.cssSelector("select#subj_id")));
					subjSelect.deselectAll();
					subjSelect.selectByIndex(j);
					
					//status = sem + 
					System.out.println("Reached courses for " + sem + " in the " + 
					subjSelect.getFirstSelectedOption().getText() + " department");
					
					//Find by text
					driver.findElement(By.cssSelector("button[value='Course Search']")).click();
					//Some departments don't have any courses listed. Due to this, only scrape 
					//courses when there are courses listed
					if(driver.findElements(By.xpath("(//button[@value='View Sections'])")).size() != 0){
						//scrapeCourses navigates back to department course listing when completed
						courses.addAll(scrapeCourses(driver, sem));
					}else
					{
						//Don't scrape
					}
					
					driver.navigate().back();
					
					//navigate back to department and campus listing
				}
			    status = "Courses for " + sem + " saved";
			    
			    driver.navigate().back();
			}
			status = "All courses in my.sc.edu saved to database";
			driver.close();
			long endTime = System.nanoTime();
			long duration = (endTime-startTime)/1000000000;
			System.out.println("saveAllCourses took " + Long.toString(duration) + "seconds\n");
			return status;
	}
	
	//Scrapes all courses from a department
	private List<Course> scrapeCourses(WebDriver driver, String sem){
		List<Course> courses = new ArrayList<Course>();
		long startTime = System.nanoTime();
		//Create a a nested while-loop
		//The outer for-loop will iterate through each course of the dept
		//The inner for-loop will iterate through each section of the course
		//The inner for-loop will gather the data of each table row and save it 
		//in a list of Course objects
		
		//Gather all Courses in a Subject/Department
		List<WebElement> courseListings = driver.findElements(By.xpath("(//button[@value='View Sections'])"));	

		//Loop through each course for that department/subject (Ex. CSCE 101 or CSCE 202)
		for(int i = 1; i <= courseListings.size(); i++){
			//String coursePath ="//tbody/tr[td/form/button]["+i+"]";
			//Find and travel to each course page
			driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
			driver.findElement(By.xpath("(//button[@value='View Sections'])["+i+"]")).click();
					
			//Get number of sections in each course
			List<WebElement> sectionListings = driver.findElements(By.xpath("(//table[@class='datadisplaytable']/tbody/tr)"));
			int courseNum = 0;
			//Create a backup in case we need additional information from a course
			Course backup = null;
					
			//Create class object for every section in the course
			//The first two rows of the table are headers. So skip to the third row
			for(int j = 3; j <= sectionListings.size(); j++){
				
				//Initalize Course object
				String sectionPath = "(//table[@class='datadisplaytable']/tbody/tr)["+j+"]";
				List<WebElement> dataCols = driver.findElements(By.xpath(sectionPath +"/td"));
				Course course = new Course();
				
				//If there is a new CRN, we don't need a backup 
				//because that indicates a new section
				//Without setting the implicit wait for the driver, searching for an
				//non-existing element would take minutes
				driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
				if(driver.findElements(By.xpath(sectionPath+"/td[2]/a")).size() != 0){					
					backup = null;
				}
				//If there is no backup, then it's a new section
				if(backup == null){
					courseNum=1;	
					course.setDept(driver.findElement(By.xpath(sectionPath+"//td[3]")).getText());
					course.setSemester(sem);
					course.setSelection(driver.findElement(By.xpath(sectionPath+"//td[1]")).getText());
					course.setCrn(driver.findElement(By.xpath(sectionPath+"//td[2]/a")).getText());
					course.setSubj(driver.findElement(By.xpath(sectionPath+"//td[3]")).getText());
					course.setCrse(driver.findElement(By.xpath(sectionPath+"//td[4]")).getText());
					course.setSec(driver.findElement(By.xpath(sectionPath+"//td[5]")).getText());
					course.setCmp(driver.findElement(By.xpath(sectionPath+"//td[6]")).getText());
					course.setCred(driver.findElement(By.xpath(sectionPath+"//td[7]")).getText());
					course.setPart_of_term(driver.findElement(By.xpath(sectionPath+"//td[8]")).getText());
					course.setTitle(driver.findElement(By.xpath(sectionPath+"//td[9]")).getText());
					//If some courses, the Time column is empty so there are only 17 data columns per course
					//Examples include Spring 2018's AFAM 399 course
					if(dataCols.size() == 17){
						course.setDays(driver.findElement(By.xpath(sectionPath+"//td[10]/abbr")).getText());
						course.setTime("");
						course.setCap(driver.findElement(By.xpath(sectionPath+"//td[11]")).getText());
						course.setAct(driver.findElement(By.xpath(sectionPath+"//td[12]")).getText());
						course.setRem(driver.findElement(By.xpath(sectionPath+"//td[13]")).getText());
						//Most data that defines the instructor is found by the xpath td/abbr
						//However, some data that dfines the instructor is found by the xpath td
						//If the text is in the td element, grab it from there
						if(driver.findElement(By.xpath(sectionPath+"//td[14]")).getText().length() != 0){
							course.setInstructor(driver.findElement(By.xpath(sectionPath+"//td[14]")).getText());

						}
						//Otherwise, grab it from td/abbr
						else{
							course.setInstructor(driver.findElement(By.xpath(sectionPath+"//td[14]/abbr")).getText());
						}
						course.setDate(driver.findElement(By.xpath(sectionPath+"//td[15]")).getText());
						course.setLocation(driver.findElement(By.xpath(sectionPath+"//td[16]")).getText());
						course.setAttribute(driver.findElement(By.xpath(sectionPath+"//td[17]")).getText());				

					}//Most courses have 18 data columns per section
					else{
						course.setDays(driver.findElement(By.xpath(sectionPath+"//td[10]")).getText());
						course.setTime(driver.findElement(By.xpath(sectionPath+"//td[11]")).getText());
						course.setCap(driver.findElement(By.xpath(sectionPath+"//td[12]")).getText());
						course.setAct(driver.findElement(By.xpath(sectionPath+"//td[13]")).getText());
						course.setRem(driver.findElement(By.xpath(sectionPath+"//td[14]")).getText());
						if(driver.findElement(By.xpath(sectionPath+"//td[15]")).getText().length() != 0){
							course.setInstructor(driver.findElement(By.xpath(sectionPath+"//td[15]")).getText());

						}else{
							course.setInstructor(driver.findElement(By.xpath(sectionPath+"//td[15]/abbr")).getText());
						}
						course.setDate(driver.findElement(By.xpath(sectionPath+"//td[16]")).getText());
						course.setLocation(driver.findElement(By.xpath(sectionPath+"//td[17]")).getText());
						course.setAttribute(driver.findElement(By.xpath(sectionPath+"//td[18]")).getText());
					}
				
					String id = sem +course.getSubj() + course.getCrse() + course.getSec() + "-" + Integer.toString(courseNum);	
					
					course.setId(id);
					courses.add(course);
					//Save to H2 DB
					courseRepository.save(course);
					backup = course;
				}
				//If there is a backup, it's a continuation
				//of a section
				else{
					courseNum++;
					course = backup;
					course.setDays(driver.findElement(By.xpath(sectionPath+"//td[10]")).getText());
					course.setTime(driver.findElement(By.xpath(sectionPath+"//td[11]")).getText());
					if(driver.findElement(By.xpath(sectionPath+"//td[15]")).getText().length() != 0){
						course.setInstructor(driver.findElement(By.xpath(sectionPath+"//td[15]")).getText());
					}else{
						course.setInstructor(driver.findElement(By.xpath(sectionPath+"//td[15]/abbr")).getText());
					}
					course.setDate(driver.findElement(By.xpath(sectionPath+"//td[16]")).getText());
					course.setLocation(driver.findElement(By.xpath(sectionPath+"//td[17]")).getText());
					course.setAttribute(driver.findElement(By.xpath(sectionPath+"//td[18]")).getText());
					String id = sem +course.getSubj() + course.getCrse() +course.getSec() +"-" + Integer.toString(courseNum);	
					course.setId(id);
					courses.add(course);
					//Save to DB
					courseRepository.save(course);
				}
				System.out.print("Course " + course.getId() + " has been saved.\n");
			}
			//After each section of a course has been recorded,
			//navigate to the previous page to perform the same operation
			//on the next course
			driver.navigate().back();
		}
		long endTime = System.nanoTime();
		long duration = (endTime - startTime)/1000000000;
		System.out.println("scrapeCourses took " + Long.toString(duration) + " seconds\n");
		return courses;
	}

	
//	I got this method from stack overflow to get distinct items by key.
//	https://stackoverflow.com/questions/23699371/java-8-distinct-by-property
	public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
	    Map<Object,Boolean> seen = new ConcurrentHashMap<>();
	    return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
	}


//	returns all the courses in the database
	public List<Course> getAllCourses(){
		List<Course> courses = new ArrayList<>();
		courseRepository.findAll()
		.forEach(courses::add);
		return courses;
		
	}	
//  get a course by ID
	public Course getCourse(String id ) {
		return courseRepository.findOne(id);
	}

//	add a course to the database.
	public void addCourse(Course course) {
		courseRepository.save(course);
	}
// 	update a course and save the update in database as well.
	public void updateCourse(String id, Course course) {
		courseRepository.save(course);
	}
//	delete a course from database.
	public void deleteCourse(String id) {
		courseRepository.delete(id);
	}

//	This is a helper method, this returns courses from the same department.
	public List<Course> getDept(List<Course> courses , String dept ) {

		return courses.stream().filter(t -> t.getDept().equals(dept)).collect(Collectors.toList());

	}
//	This is a helper method, this return courses taught by a particular faculty.
	public List<Course> getFaculty(List<Course> courses , String faculty ) {

		return courses.stream().filter(t -> t.getInstructor().equals(faculty)).collect(Collectors.toList());

	}
	
//	This method returns the unique departments in the "courses" passed as parameter.
	public List<Course> getUniqueDepts( List<Course> courses ) {	

		return courses.stream().filter(distinctByKey(p -> p.getDept())).collect(Collectors.toList());

	}
//	This method returns the Unique Faculties in courses passed as parameter.
	public List<Course> getUniqueFaculties(  List<Course> courses ) {		

		return courses.stream().filter(distinctByKey(p -> p.getInstructor())).collect(Collectors.toList());

	}
	
//	reads all courses from file and insert them to database
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
		        	id = UUID.randomUUID().toString();
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

	
//	returns the teaching summary by course
	
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

//	Returns the teaching summary by Dept
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


	


}
