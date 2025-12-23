package com.example.pdfGenerator.service;



import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class EmailServicePDF {

    private final JavaMailSender mailSender;

    public EmailServicePDF(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public boolean sendResultsEmail(String to, byte[] pdfData) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setSubject("Your UNZA Exam Results");
            helper.setText("Dear Student,\n\nPlease find attached your exam results PDF.\n\nRegards,\nUNZA Student Results Hub");

            InputStreamSource attachment = new ByteArrayResource(pdfData);
            helper.addAttachment("ExamResults.pdf", attachment);

            mailSender.send(message);
            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }
}
