package com.example.pdfGenerator.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import java.io.ByteArrayInputStream;
import java.io.File;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    // Simple text email
    public void sendSimpleEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("phillipphiri470@gmail.com"); 
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }

    // Email PDF from File
    public void sendResults(String toEmail, File pdfFile) throws Exception {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom("phillipphiri470@gmail.com");
        helper.setTo(toEmail);
        helper.setSubject("UNZA Exam Results");
        helper.setText("Dear Student,\n\nPlease find your examination results attached.\n\nRegards, University of Zambia");

        helper.addAttachment("exam_results.pdf", pdfFile);
        mailSender.send(message);
    }

    // Email PDF from byte[] (in-memory)
    public void sendResults(String toEmail, byte[] pdfBytes) throws Exception {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom("phillipphiri470@gmail.com");
        helper.setTo(toEmail);
        helper.setSubject("UNZA Exam Results");
        helper.setText("Dear Student,\n\nPlease find your examination results attached.\n\nRegards, University of Zambia");

        helper.addAttachment("exam_results.pdf",
                () -> new ByteArrayInputStream(pdfBytes));

        mailSender.send(message);
    }
}
