This is the Angular client application for Team Z, CSCE 741, Fall 2018.
-Allen Bates
-Ahmed Khan
-Zhiyuan Li
-Justin McCreary
-Rick Stroud

Requirements & Preconditions:
1) Ensure that the following components are installed: Node.JS to provide the Node Package Manager (npm), TypeScript, and Angular 4 CLI
Instructions for these installation may be found in the homework tutorial assigned for class, see link below, at the 15min mark.
https://www.youtube.com/watch?v=KhzGSHNhnbI
2) Ensure the Team Z server application is installed from GitHub. This includes related components included with the server application such as the Chrome driver and that MySQL is also installed. See the ReadMe file on GitHub for the server application for details.
3) Ensure the server application is running and advertising the ReST web service which will be used by the Angular client app.
4) Ensure course data has been loaded either via Solenium or through the sql scripts provided with the server application. It is recommended to use the sql script to load data as this will define a robust but manageable mix of courses, slightly over 1,500 courses in total. The sql script may be ran by downloading the script file "course_small_n.sql" from the server GitHub and executing it from MySQL using the source command: mysql> source course_small_n.sql.
5) From GitHub copy the Team Z Angular client code locally.
6) Compile the Angular client code by going to the local Team Z Angular client source directory and executing "npm start".

Test:
1) Execute the Team Z Angular client by browsing to the url http://localhost:4200
2) This will display an explanation of the project, retrieve all of the course information vai one ReST service call to the server, and compute locally in the client summaries of the courses by Dept and by Instrutor, then display this information.
3) If no data is displayed by the client ensure the ReST service is running and has been populated with data by browsing directly to the server application at the url http://localhost:8080/courses, this will bypass the Angular client appicaiton and should display a list of all courses.
4) To verify the correctness of the Angular client summaries you may compare the output of the Angular client's output in step 1 to the output of browsing directly to the server summaries at the urls http://localhost:8080/teachingSummary and http://localhost:8080/teachingSummarybyDept
