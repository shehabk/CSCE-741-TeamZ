This is the Angular client application for Team Z, CSCE 741, Fall 2018.
-Allen Bates
-Ahmed Khan
-Zhiyuan Li
-Justin McCreary
-Rick Stroud

Requirements:
1) Ensure the server application is installed.
2) Ensure the server application is running running and advertising the ReST web service.
3) Data must have been loaded either via Solenium or through the sql scripts provided.
4) Install the Angular client code.
5) Compile the Angular client code by going to the source director and executing "npm start".

Test:
6) Execute the Angular client by browsing to the url http://localhost:4200
7) This will display an explanation of the project, retrieve all of the course information from the ReST service on the server and compute locally in the client summaries of the courses by Dept and by Instrutor, then display this information.
8) If no data is displayed by the client ensure the ReST service is running and has been populated with data by browsing directly to the server at the url http://localhost:8080/courses
9) To verify the correctness of the summaries you may compare the output of the Angular client's output in step 6 to the output of browsing directly to the server at the urls http://localhost:8080/teachingSummary and http://localhost:8080/teachingSummarybyDept

