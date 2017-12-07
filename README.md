# CSCE-741-TeamZ

#### Using Selenium:
	1) Make sure you downloaded the chrome driver and the exePath in loadDriver() method 
	of CourseService class is correct.
	2) Make sure you have setup duo activation on your account. This includes having the
	DUO Security app installed on your mobile device and having the app configured with
	your my.sc.edu account.
	3) Run the project and do GET request to http://localhost:8080/loadDriver , 
	this will load the driver.
	4) The next step is to send a POST request to http://localhost:8080/login, and the body 
	of the request would be of the follwoing format 

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


#### USING MYSQL DATABASE:(https://www.a2hosting.com/kb/developer-corner/mysql/managing-mysql-databases-and-users-from-the-command-line):
	1) Install MySQL server. (remember the root password)
	2) Do "mysql -u root -p" in the terminal, which will prompt for the password
	   use the password you set while installing.
	3) After this step do "GRANT ALL PRIVILEGES ON *.* TO teamz@'localhost' IDENTIFIED BY teamz;"
	   it will create a new user named 'teamz' with password 'teamz'
	4) Quit the mysql terminal
	5) Login as new user with "mysql -u teamz -p", use teamz as password
	6) Create a database named teamz "CREATE DATABASE teamz;"
##### Populate database with some pre-scraped data:
	1) Download [this sql file](https://github.com/shehabk/CSCE-741-TeamZ/blob/master/data/course_small_n.sql) at 
	your convenient location.
	2) Navigate your terminal to that location using cd.
	3) Login as user with "mysql -u teamz -p", use teamz as password
	4) Do "source course_small_n.sql", this will create the course table and populate it with pre-scraped data from
	CSCE Fall 17, CSCE Fall 18, BMEN Fall 17, ECHE Fall 17, ECIV Fall 17, ELCT Fall 17,EMCH Fall 17,MATH Fall 17.
YOU are now set up to use MySQL database for this project.



#### USING CUCUMBER TESTING:
	1) The cucumber staff is located at src/test folder.
	   src/test/java/cucumberRunner/RunCucumberTest.java is the Runner for cucumber test
	   src/test/java/cucumbersteps/cucumberTestStep.java is the step definition which map the feature file in real code to            be excuted
	   src/test/resources/cucumber/cucumber.feature  is the feature file where the test scenarios are written in Gherkin 
	2) There are currently 3 scenarios in cucumber test. 
	   1. A simple test case showing every file and dependencies are in their right place to integrate to STS
	   2. Given the api is running at localhost8080, when the user perform a get request of /loadDriver, the web driver                 should be loaded successfully
	   3. Given web driver is loaded successfully, when the user perform a post request of their username and pass to the               path /login, it should successfully login to my.sc.edu
	3) To run the cucumber test, 
	   1. run the api
	   2. right click at the root folder of the final project, run as maven test, and all the test scenarios will be run                 automatically
	   3. In order to login to my.sc.edu, before running the cucumber test, you should first edit the .feature file                     scenario #3 of the username and password part to be your real username and pass, or this step will fail in the                 test.
	4)  You can see the testing result at the console 
	    And a good looking format report located at target/cucumber/index.html 
	    after runing the cucumber test
