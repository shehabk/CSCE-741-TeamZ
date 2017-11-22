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
  //credits: number,
  cred: string,
  part_of_term: string,
  title: string,
  days: string,
  time: string,
  cap: string,
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
  computedSummaryArray: Summary[];
  tempSummary: Summary;

  courseArray: Course[];
  uniqueCourseArrayByDept: Course[];
  uniqueCourseArrayByInstructor: Course[];
  tempCourse: Course;

  constructor(private http: HttpClient) {

  }
  
  computeSummaryArray(localCourseArray) {

    var myArray = new Array<Summary>();
    
    for (var i = 0; i < localCourseArray.length; i++) {
        var tempSummary = new Summary();
        tempSummary.dept = localCourseArray[i].dept;
        console.log(tempSummary.dept);

        myArray[myArray.length] = tempSummary;

        for(var j = 0; j < this.courseArray.length; j++) {
          if (myArray[i].dept == this.courseArray[j].dept) {
            myArray[i].sectionTot++;

            var actNum = Number(this.courseArray[j].act);
            myArray[i].studentTot = myArray[i].studentTot + actNum;
            
            var credNum: Number;
            if(!isNaN(Number(this.courseArray[j].cred)))
              credNum = Number(this.courseArray[j].cred);
            else
              credNum = 1;

            myArray[i].studentCreditHoursTot = myArray[i].studentCreditHoursTot + (credNum * actNum);
          }
        }

        myArray[i].fte = myArray[i].studentCreditHoursTot / 15;
      }

      for(var i = 0; i < myArray.length; i++)
        console.log(myArray[i].dept);

        this.computedSummaryArray = myArray;
  }


  ngOnInit(): void {
    
      this.http.get<Course[]>('http://localhost:8080/courses').subscribe(data => {
        console.log(data); 
        
        this.courseArray = data;
        
        this.uniqueCourseArrayByDept = _.uniqBy(this.courseArray, 'dept');
        console.log(this.uniqueCourseArrayByDept);

        this.computeSummaryArray(this.uniqueCourseArrayByDept);
      })
  }
}   