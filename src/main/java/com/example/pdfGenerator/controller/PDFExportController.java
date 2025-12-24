package com.example.pdfGenerator.controller;

import com.example.pdfGenerator.model.LoginCredentials;
import com.example.pdfGenerator.model.BioData; // 🔽 ADDED
import com.example.pdfGenerator.repository.CourseRepository;
import com.example.pdfGenerator.repository.UserCredentialsRepo;
import com.example.pdfGenerator.repository.BioDataRepository; // 🔽 ADDED
import com.example.pdfGenerator.security.JwtUtil;
import com.example.pdfGenerator.service.PDFGeneratorService;
import com.example.pdfGenerator.service.EmailService;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model; // 🔽 ADDED
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@Controller
public class PDFExportController {

    private final PDFGeneratorService pdfService;
    private final CourseRepository courseRepo;
    private final JwtUtil jwtUtil;
    private final UserCredentialsRepo userRepo;
    private final EmailService emailService;
    private final BioDataRepository bioDataRepo; // 🔽 ADDED

    public PDFExportController(
            PDFGeneratorService pdfService,
            CourseRepository courseRepo,
            JwtUtil jwtUtil,
            UserCredentialsRepo userRepo,
            EmailService emailService,
            BioDataRepository bioDataRepo) { // 🔽 ADDED

        this.pdfService = pdfService;
        this.courseRepo = courseRepo;
        this.jwtUtil = jwtUtil;
        this.userRepo = userRepo;
        this.emailService = emailService;
        this.bioDataRepo = bioDataRepo; // 🔽 ADDED
    }

    // ✅ Login page endpoint
    @GetMapping("/loginPage")
    public String loginPage() {
        return "Login";
    }

    // 🔽 UPDATED (but not breaking)
    @GetMapping("/home")
    public String home(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
            Model model) {

        String studentNumber = extractUsername(authHeader);

        BioData bio = bioDataRepo.findByStudentNumber(studentNumber);

        model.addAttribute("bio", bio);

        return "home";
    }

    @GetMapping("/pdf/check")
    @ResponseBody
    public ResponseEntity<String> checkStudent(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {

        String studentNumber = extractUsername(authHeader);

        if (courseRepo.findByStudentNumber(studentNumber).isEmpty()) {
            return ResponseEntity.badRequest().body("Invalid");
        }
        return ResponseEntity.ok("Valid");
    }

    @GetMapping("/pdf/generate")
    public void generatePDF(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
            HttpServletResponse response) throws Exception {

        String studentNumber = extractUsername(authHeader);
        pdfService.export(response, studentNumber);
    }

    @PostMapping("/pdf/email")
    @ResponseBody
    public String emailResults(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {

        String studentNumber = extractUsername(authHeader);

        LoginCredentials user = userRepo.findByUsername(studentNumber)
                .orElseThrow(() -> new RuntimeException("User not found"));

        try {
            byte[] pdfBytes = pdfService.generatePdfBytes(studentNumber);
            emailService.sendResults(user.getEmail(), pdfBytes);
            return "✅ Results successfully sent to " + user.getEmail();
        } catch (Exception e) {
            e.printStackTrace();
            return "❌ Failed to send results email.";
        }
    }

    private String extractUsername(String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        return jwtUtil.extractUsername(token);
    }
}
