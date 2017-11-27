package com.example.demo.selenium.integration;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class CourseService {

	static WebDriver driver;
	
	@Autowired
	private CourseRepository repo;
	
	//Initializes ChromeDriver
	public String loadDriver(){
		String ret = "";
		//Save path to chromedriver executable
		/*Download ChromeDriver from:
			https://sites.google.com/a/chromium.org/chromedriver/downloads
		*/
		String exePath = "C:\\Users\\Allen\\Desktop\\chromedriver_win32\\chromedriver.exe";
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
				driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
				
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

	//Returns all of the courses based on the semester and subject/department
	public List<Course> getCourses(String sem, String subj){
		
		List<Course> courses = new ArrayList<Course>();
				
		
		return courses;
	}

	//Returns all of the courses in my.sc.edu
	public List<Course> getAllCourses(){
		List<Course> courses = new ArrayList<Course>();
		repo.findAll().forEach(courses::add);
		return courses;
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
			driver.findElement(By.id("id____UID7")).click();
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
			driver.findElement(By.id("id____UID5")).click();
		}else{
			return "Subject entered is invalid";
		}
		
		courses = scrapeCourses(driver, sem);
		driver.close();
		status = "Courses successfully saved to database";
		
		return status;
	}
	
	//Saves all of the courses in my.sc.edu
	public String saveAllCourses(){
		String status = "";
		List<Course> courses = new ArrayList<Course>();
		
		//Loop through each semester
		//Skip the first option of the Semester Select; "None"
		Select semesterSelect = new Select(driver.findElement(By.id("term_input_id")));
		List<WebElement> semesterOptions = semesterSelect.getOptions();
		for (int i = 1; i < semesterOptions.size(); i++) {
			System.out.println(i);
			semesterSelect = new Select(driver.findElement(By.id("term_input_id")));
			semesterSelect.selectByIndex(i);
			String sem = semesterSelect.getFirstSelectedOption().getText();
			
			//Remove ' (View Only)' from any selection option text
			String remove = " (View only)";
			if(sem.contains(remove)){
				sem = sem.replace(remove, "");		
			}
		
			driver.findElement(By.id("id____UID7")).click();
			
			
			//Select USC-Columbia campus
			Select campus = new Select(driver.findElement(By.id("camp_id")));
			campus.selectByValue("COL");
				
			//Search each subject/department
			Select subjSelect = new Select(driver.findElement(By.id("subj_id")));
			List<WebElement> subjOptions = subjSelect.getOptions();
			for (int j = 0; j < subjOptions.size(); j++) {
				subjSelect = new Select(driver.findElement(By.id("subj_id")));
				subjSelect.deselectAll();
				subjSelect.selectByIndex(j);
				driver.findElement(By.id("id____UID5")).click();
				//scrapeCourses navigates back to department course listing when completed
				courses.addAll(scrapeCourses(driver, sem));
				//navigate back to department and campus listing
			}
		    status = "Courses for " + sem + " saved";
		    
		    driver.navigate().back();
		}
		status = "All courses in my.sc.edu saved to database";
		return status;
	}
	
	//Scrapes all courses from a department
	private List<Course> scrapeCourses(WebDriver driver, String sem){
		List<Course> courses = new ArrayList<Course>();
		//Create a a nested while-loop
				//The outer for-loop will iterate through each course of the dept
				//The inner for-loop will iterate through each section of the course
				//The inner for-loop will gather the data of each table row and save it 
				//in a list of Course objects
				List<WebElement> courseListings = driver.findElements(By.xpath("//tbody/tr[td/form/button]"));
				List<WebElement> sectionListings;
				

				//Loop through each course for that department
				for(int i = 1; i <= courseListings.size(); i++){
					String coursePath ="//tbody/tr[td/form/button]["+i+"]";
					//Find and travel to each course page
					driver.findElement(By.xpath(coursePath+"//td/form/button")).click();
					
					//Get number of sections in each course
					sectionListings = driver.findElements(By.xpath("//tbody/tr[td/abbr]"));
					//Create a backup in case we need additional information from a course
					Course backup = null;
					
					//Create class object for every section in the course
					for(int j = 1; j <= sectionListings.size(); j++){
						//Initalize Course object
						String sectionPath = "//tbody/tr[td/abbr]["+j+"]";
						Course course = new Course();
						
						//If there is a new CRN, we don't need a backup 
						//because that indicates a new section
						if(driver.findElements(By.xpath(sectionPath+"/td[2]/a")).size() > 0){					
							backup = null;
						}
						//If there is no backup, then it's a new section
						if(backup == null){
							course.setId(UUID.randomUUID().toString());
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
							course.setDays(driver.findElement(By.xpath(sectionPath+"//td[10]")).getText());
							course.setTime(driver.findElement(By.xpath(sectionPath+"//td[11]")).getText());
							course.setCap(driver.findElement(By.xpath(sectionPath+"//td[12]")).getText());
							course.setAct(driver.findElement(By.xpath(sectionPath+"//td[13]")).getText());
							course.setRem(driver.findElement(By.xpath(sectionPath+"//td[14]")).getText());
							course.setInstructor(driver.findElement(By.xpath(sectionPath+"//td[15]/abbr")).getText());
							course.setDate(driver.findElement(By.xpath(sectionPath+"//td[16]")).getText());
							course.setLocation(driver.findElement(By.xpath(sectionPath+"//td[17]")).getText());
							course.setAttribute(driver.findElement(By.xpath(sectionPath+"//td[18]")).getText());				
							
							courses.add(course);
							//Save to H2 DB
							repo.save(course);
							backup = course;
						}
						//If there is a backup, it's a continuation
						//of a section
						else{
							course = backup;
							course.setId(UUID.randomUUID().toString());
							course.setDays(driver.findElement(By.xpath(sectionPath+"//td[10]")).getText());
							course.setTime(driver.findElement(By.xpath(sectionPath+"//td[11]")).getText());
							course.setInstructor(driver.findElement(By.xpath(sectionPath+"//td[15]/abbr")).getText());
							course.setDate(driver.findElement(By.xpath(sectionPath+"//td[16]")).getText());
							course.setLocation(driver.findElement(By.xpath(sectionPath+"//td[17]")).getText());
							course.setAttribute(driver.findElement(By.xpath(sectionPath+"//td[18]")).getText());
							
							courses.add(course);
							//Save to H2 DB
							repo.save(course);
							
						}
						System.out.print(course.toString());
					}
					//After each section of a course has been recorded,
					//navigate to the previous page to perform the same operation
					//on the next course
					driver.navigate().back();
				}
				
		return courses;
	}
}
