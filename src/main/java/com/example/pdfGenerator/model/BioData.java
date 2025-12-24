package com.example.pdfGenerator.model;

import javax.persistence.*;

@Entity
@Table(name = "bio_data")
public class BioData {
    @Id
    private String studentNumber;

    private String fname;
    private String lname;
    private String programme;
    private int currentYearOfStudy;
    
     // getters and setters
    
    public String getStudentNumber() {
        return studentNumber;
    }
    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }
    public String getFname() {
        return fname;
    }
    public void setFname(String fname) {
        this.fname = fname;
    }
    public String getLname() {
        return lname;
    }
    public void setLname(String lname) {
        this.lname = lname;
    }
    public String getProgramme() {
        return programme;
    }
    public void setProgramme(String programme) {
        this.programme = programme;
    }
    public int getCurrentYearOfStudy() {
        return currentYearOfStudy;
    }
    public void setCurrentYearOfStudy(int currentYearOfStudy) {
        this.currentYearOfStudy = currentYearOfStudy;
    }

   
    

}
