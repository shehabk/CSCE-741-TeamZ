# CSCE-741-TeamZ
This is the group project for Team Z, CSCE 741, Fall 2017.
-Allen Bates
-Ahmed Khan
-Zhiyuan Li
-Justin McCreary
-Rick Stroud

Project Scope
=============
The scope of the entire project includes the following Product Backlog Items (BPIs), or "Tasks".

1) Develop Unit Tests and a Test-Suites :
2) Move summary functionality to client side from Server side
3) Use Selenium and Webdriver on server side to login to MySC and move to schedule get webpage containing schedule for given semester and department.
4) Cucumber integration into STS
5) Save data on server side in permanent DB

All tasks except #2, are performed by the server application in the "Master" branch from the Team Z GitHub.
Task #2 is performed by the Angular client which may be downloaded from the "Angular" branch from the Team Z GitHub.

Logically, these tasks can be thought of in the order below:
Task 5 - Configure permanent DB
Task 3 - Use Selenium to scrape MySC
Task 1 - Use JUnit to perform unit testing
Task 4 - Use Cucumber to test web functionality
Task 2 - Move summary functionality from server to Angular client


Preconditions
=============
For task #5 the permanent database MySQL was selected. This requires that MySQL be installed locally, a user 'teamz' be created, and a database named 'teamz' also be created.
This may be accomplished by following the steps below:

#### USING MYSQL DATABASE:(https://www.a2hosting.com/kb/developer-corner/mysql/managing-mysql-databases-and-users-from-the-command-line):
	1) Install MySQL server. (remember the root password)
	2) Do "mysql -u root -p" in the terminal, which will prompt for the password
	   use the password you set while installing.
	3) After this step do "GRANT ALL PRIVILEGES ON *.* TO teamz@'localhost' IDENTIFIED BY 'teamz';"
	   it will create a new user named 'teamz' with password 'teamz'
	4) Quit the mysql terminal
	5) Login as new user with "mysql -u teamz -p", use teamz as password
	6) Create a database named teamz "CREATE DATABASE teamz;"


Populating the Database
=======================
When task #3 is performed the MySC website will be scraped by Selenium to load the entire published course catalog for USC. This is a very time consuming process.

Alternatively, a SQL script has been provided with a varied and robust set of courses has been provided. This script will remove any previously loaded courses and update the permanent database with a known set of approximately 1,500 courses.

It is encouraged that once Selenium has been verified that this script be executed by following the steps below. The JUnit test script are written assuming these courses are present.

##### Populate database with some pre-scraped data:
	1) Download [this sql file](https://github.com/shehabk/CSCE-741-TeamZ/blob/master/data/course_small_n.sql) at 
	your convenient location.
	2) Navigate your terminal to that location using cd.
	3) Login as user with "mysql -u teamz -p", use teamz as password
	4) Do "source course_small_n.sql", this will create the course table and populate it with pre-scraped data from
	CSCE Fall 17, CSCE Fall 18, BMEN Fall 17, ECHE Fall 17, ECIV Fall 17, ELCT Fall 17, EMCH Fall 17, MATH Fall 17.
You are now set up to use MySQL database for this project.


Verifying Selenium
===================
The steps below are to be used to verify task #3, Selenium. Note, to allow POST commands to be submitted to the server application you will need a the tool of your choice, such as Postman.
#### Using Selenium:
	1) Make sure you downloaded the chrome driver and the exePath in loadDriver() method 
	of CourseService class is correct.
	2) Make sure you have setup duo activation on your account. This includes having the
	DUO Security app installed on your mobile device and having the app configured with
	your my.sc.edu account.
	3) Run the project and do GET request to http://localhost:8080/loadDriver , 
	this will load the driver.
	4) The next step is to send a POST request to http://localhost:8080/login, and the body 
	of the request would be of the follwoing format, where username & password hav been set to your USC credentials.

	{
		"username":"username",
		"pass":"password"
	}

	this will use your credential to login to my.sc.edu

	5) To save a single semester of a department to the database you can send 
	a GET request to "http://localhost:8080//saveCourses/{sem}/{subj}" , for example
	"http://localhost:8080//saveCourses/Spring 2018/CSCE" will store all courses of CSCE from 
	Spring 2018 semester.
	
	6) To save all the courses form all deparment and semster, do a GET request to 
	http://localhost:8080//saveAllCourses. This method may get stuck after making storing 
	some department.


Executing Cucumber Tests
========================
#### USING CUCUMBER TESTING:
	1) The cucumber tests are located at src/test folder.
	   src/test/java/cucumberRunner/RunCucumberTest.java is the Runner for cucumber test
	   src/test/java/cucumbersteps/cucumberTestStep.java is the step definition which map the feature file in real code to be excuted
	   src/test/resources/cucumber/cucumber.feature  is the feature file where the test scenarios are written in Gherkin 
	2) There are currently 3 scenarios in cucumber test. 
	   1. A simple test case showing every file and dependencies are in their right place to integrate to STS
	   2. Given the api is running at localhost8080, when the user perform a get request of /loadDriver, the web driver should be loaded successfully
	   3. Given web driver is loaded successfully, when the user perform a post request of their username and pass to the path /login, it should successfully login to my.sc.edu
	3) To run the cucumber test, 
	   1. run the api
	   2. right click at the root folder of the final project, run as maven test, and all the test scenarios will be run automatically
	   3. In order to login to my.sc.edu, before running the cucumber test, you should first edit the .feature file scenario #3 of the username and password part to be your real username and pass, or this step will fail in the                 test.
	4)  You can see the testing result at the console 
	    And a good looking format report located at target/cucumber/index.html 
	    after runing the cucumber test


Executing JUnit Tests
=====================
To run the JUnit test scripts follow the steps below:
1) Run the server application
2) Right click on "src/test/java" and select "Run As", the "JUnit Test"
3) This will execute the JUnit tests and results may be found in the JUnit report window.
4) A total of 19 tests should be executed with 0 errors and 0 fails.


Executing Angular Client
========================
To execute the Angular client an to verify task #2, see the code found in the Angular branch of the Team Z GitHub and the assocaited ReadMe.md found there.
