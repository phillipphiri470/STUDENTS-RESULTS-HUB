package com.example.pdfGenerator.controller;

import com.example.pdfGenerator.model.Course;
import com.example.pdfGenerator.repository.CourseRepository;
import com.example.pdfGenerator.service.PDFGeneratorService;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
public class PDFExportController {

    private final PDFGeneratorService pdfGeneratorService;
    private final CourseRepository courseRepository;

public PDFExportController(PDFGeneratorService pdfGeneratorService, CourseRepository courseRepository) {
    this.pdfGeneratorService = pdfGeneratorService;
    this.courseRepository = courseRepository;
}


    // Home Landing page
   @GetMapping("/loginPage")
   public String loginPage(){
    return "Login";
   }
   @GetMapping("/forgotPassword")
   public String forgotPassword(){
    return "forgotPassword";
   }
    @GetMapping("/home")
    public String home() {
        return "home"; // looks for home.html in src/main/resources/templates
    }

    // Get a specific student number
    @GetMapping("/pdf/generate/{studentNumber}")
    public void generatePDF(@PathVariable String studentNumber, HttpServletResponse response) throws IOException {
        response.setContentType("application/pdf");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String currentDateTime = dateFormatter.format(new Date());

        String HeaderKey = "Content-Disposition";
        String HeaderValue = "attachment; filename=exam_results_" + studentNumber + "_" + currentDateTime + ".pdf";
        response.setHeader(HeaderKey, HeaderValue);

        this.pdfGeneratorService.export(response, studentNumber);
    }
   // checks to see if student number is valid 
    @GetMapping("/pdf/check/{studentNumber}")
    public ResponseEntity<String> checkStudent(@PathVariable String studentNumber) {
    List<Course> courses = courseRepository.findByStudentNumber(studentNumber);
    if (courses.isEmpty()) {
        return ResponseEntity.badRequest().body("Invalid student number");
    }
    return ResponseEntity.ok("Valid");
}

}
