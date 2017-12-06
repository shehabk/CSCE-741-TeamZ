import { Component, OnInit } from '@angular/core';
import{ HttpClient, HttpHeaders } from '@angular/common/http';

import { Pipe, PipeTransform } from '@angular/core';
import * as _ from 'lodash'; 

/*
interface Summary {
  fte: number,
  instructor: string,
  courseTot: number,
  studentTot: number,
  studentCreditHoursTot: number,
  subject: string
}
*/

class Summary {
  fte: number;
  instructor: string;
  sectionTot: number;
  studentTot: number;
  studentCreditHoursTot: number;
  dept: string;
  
  constructor() {
    this.fte = 0;
    this.instructor = "";
    this.sectionTot = 0;
    this.studentTot = 0;
    this.studentCreditHoursTot = 0;
    this.dept = "";
      }
}

interface Course {
  id: string,
  dept: string,
  semester: string,
  crn: string,
  subj: string,
  crse: string,
  sec: string,
  cmp: string,
  // Note - Change name of property and datatype from number to string to match integrated code.
  //credits: number,
  cred: string,
  part_of_term: string,
  title: string,
  days: string,
  time: string,
  cap: string,
  // Note - Change name of property and datatype from number to string to match integrated code.
  //act: number,
  //rem: number,
  act: string,
  rem: string,
  instructor: string,
  date: string,
  location: string,
  attribute: string,
  select: string
}

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})

export class AppComponent implements OnInit {
  title = 'app';
  summaryArray: Summary[];
  computedDeptSummaryArray: Summary[];
  computedInstructorSummaryArray: Summary[];
  tempSummary: Summary;

  courseArray: Course[];
  uniqueCourseArrayByDept: Course[];
  uniqueCourseArrayByInstructor: Course[];
  tempCourse: Course;

  constructor(private http: HttpClient) {

  }
  
computeInstructorSummaryArray(uniqueInstructorArray) {
  
      var myArray = new Array<Summary>();
      console.log(uniqueInstructorArray.length);
      for (var i = 0; i < uniqueInstructorArray.length; i++) {
          var tempSummary = new Summary();
          tempSummary.instructor = uniqueInstructorArray[i].instructor;
          tempSummary.dept = uniqueInstructorArray[i].dept;
          console.log(tempSummary.instructor);console.log(tempSummary.dept);
  
          myArray[myArray.length] = tempSummary;
  
          for(var j = 0; j < this.courseArray.length; j++) {
            if ((myArray[i].instructor == this.courseArray[j].instructor) && (myArray[i].dept == this.courseArray[j].dept)){
              myArray[i].sectionTot++;
  
              var actNum = Number(this.courseArray[j].act);
              myArray[i].studentTot = myArray[i].studentTot + actNum;
              
              var credNum: number;
              if(!isNaN(Number(this.courseArray[j].cred)))
                credNum = Number(this.courseArray[j].cred);
              else
                credNum = 1;
              
              myArray[i].studentCreditHoursTot = myArray[i].studentCreditHoursTot + (credNum * actNum);
            }
          }
  
          myArray[i].fte = myArray[i].studentCreditHoursTot / 15;
        }
  
        // DEBUG code to list summary
        //for(var i = 0; i < myArray.length; i++)
        //  console.log(myArray[i].dept);
  
        this.computedInstructorSummaryArray = myArray;
    }
  

  computeDeptSummaryArray(uniqueDeptArray) {

    var myArray = new Array<Summary>();
        
    for (var i = 0; i < uniqueDeptArray.length; i++) {
        var tempSummary = new Summary();
        tempSummary.dept = uniqueDeptArray[i].dept;

        myArray[myArray.length] = tempSummary;

        for(var j = 0; j < this.courseArray.length; j++) {
          if (myArray[i].dept == this.courseArray[j].dept) {
            myArray[i].sectionTot++;

            var actNum = Number(this.courseArray[j].act);
            myArray[i].studentTot = myArray[i].studentTot + actNum;
            
            var credNum: number;
            if(!isNaN(Number(this.courseArray[j].cred)))
              credNum = Number(this.courseArray[j].cred);
            else
              credNum = 1;

            myArray[i].studentCreditHoursTot = myArray[i].studentCreditHoursTot + (credNum * actNum);
          }
        }

        myArray[i].fte = myArray[i].studentCreditHoursTot / 15;
      }

      // DEBUG code to list summary
      //for(var i = 0; i < myArray.length; i++)
      //  console.log(myArray[i].dept);

      this.computedDeptSummaryArray = myArray;
  }


  ngOnInit(): void {
    
      // Use the httpClient to make a call to get all the courses on initialization.
      // Note - This requires the Server code to allow Cross Origin Access. The simplest
      // way to accomlish this is for the ReST Controller to add the line @CrossOrigin.
      this.http.get<Course[]>('http://localhost:8080/courses').subscribe(data => {

        // All course (section) data now exists in the data construct, move to courseArray.
        this.courseArray = data;

        // Next summarize that raw data by dept & by instructor for later display.

        // Summarize on the client side by dept.
        // This data should match the data on the server side when browsing the URL below:
        // http://localhost:8080//teachingSummarybyDept
        this.uniqueCourseArrayByDept = _.uniqBy(this.courseArray, 'dept');
        this.computeDeptSummaryArray(this.uniqueCourseArrayByDept);

        // Summarize on the client side by instrutor.
        // This should match the data on the server side when browsing the URL below:
        //http://localhost:8080//teachingSummary
        this.uniqueCourseArrayByInstructor = _.uniqBy(this.courseArray, v => JSON.stringify([v.dept, v.instructor]));
        this.computeInstructorSummaryArray(this.uniqueCourseArrayByInstructor);
      });
  }
}   