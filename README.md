# CSCE-741-TeamZ

#### Using Selenium
1) Make sure you downloaded the chrome driver and the exePath in loadDriver() method in CourseService class is correct.
2) Make sure you have setup duo activation on your account.
3) Run the project and do get request to http://localhost:8080/loadDriver , this will load the driver.
4) The next step is to send a post request to http://localhost:8080/login, and the body of the request would be of the format 

{
	"username":"username",
	"pass":"password"
}

this will use your credential to login to my.sc.edu

4) To save a single semester of a department to the database you can send a get request to "http://localhost:8080//saveCourses/{sem}/{subj}" , for example http://localhost:8080//saveCourses/Spring 2018/CSCE will store
all courses of CSCE from Spring 2018 semester.


#### USING MYSQL DATABASE:(https://www.a2hosting.com/kb/developer-corner/mysql/managing-mysql-databases-and-users-from-the-command-line)
1) Install MySQL server. (remember the root password)
2) Do "mysql -u root -p" in the terminal, which will prompt for the password
   use the password you set while installing.
3) After this step do "GRANT ALL PRIVILEGES ON *.* TO teamz@'localhost' IDENTIFIED BY teamz;"
   it will create a new user named 'teamz' with password 'teamz'
4) Quit the mysql terminal
5) Login as new user with "mysql -u teamz -p", use teamz as password
6) Create a database named teamz "CREATE DATABASE teamz;"

YOU are now set up to use MySQL database for this project.
