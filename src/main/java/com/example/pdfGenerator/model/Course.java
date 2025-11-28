package com.example.pdfGenerator.model;

import javax.persistence.*;

@Entity
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String studentName;   
    private String studentNumber; 
    private String courseName;    
    private String grade;         
    private String interpretation; 

    public Course() {}

    public Course(String studentName, String studentNumber, String courseName, String grade, String interpretation) {
        this.studentName = studentName;
        this.studentNumber = studentNumber;
        this.courseName = courseName;
        this.grade = grade;
        this.interpretation = interpretation;
    }

    // Getters & Setters
    public Long getId() { return id; }

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }

    public String getStudentNumber() { return studentNumber; }
    public void setStudentNumber(String studentNumber) { this.studentNumber = studentNumber; }

    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }

    public String getGrade() { return grade; }
    public void setGrade(String grade) { this.grade = grade; }

    public String getInterpretation() { return interpretation; }
    public void setInterpretation(String interpretation) { this.interpretation = interpretation; }
}
