package com.example.pdfGenerator.service;

import com.example.pdfGenerator.model.Course;
import com.example.pdfGenerator.repository.CourseRepository;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;

import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class PDFGeneratorService {

    private final CourseRepository courseRepository;

    public PDFGeneratorService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    // Export PDF to browser
    public void export(HttpServletResponse response, String studentNumber) throws IOException {
        try {
            List<Course> courses = courseRepository.findByStudentNumber(studentNumber);
            if (courses.isEmpty()) throw new RuntimeException("No results found");

            response.setContentType("application/pdf");
            PdfWriter writer = null;
            Document document = new Document(PageSize.A4);
            writer = PdfWriter.getInstance(document, response.getOutputStream());

            buildPdf(document, writer, courses, studentNumber);

        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    // Generate PDF as byte[] for emailing
    public byte[] generatePdfBytes(String studentNumber) throws Exception {
        List<Course> courses = courseRepository.findByStudentNumber(studentNumber);
        if (courses.isEmpty()) throw new RuntimeException("No results found");

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4);
        PdfWriter writer = PdfWriter.getInstance(document, out);

        buildPdf(document, writer, courses, studentNumber);

        return out.toByteArray();
    }

    // Common PDF builder
    private void buildPdf(Document document, PdfWriter writer,
                          List<Course> courses, String studentNumber)
            throws DocumentException {

        document.open();
        String studentName = courses.get(0).getStudentName();

        // Border
        PdfContentByte canvas = writer.getDirectContent();
        Rectangle rect = new Rectangle(23, 23,
                PageSize.A4.getWidth() - 23,
                PageSize.A4.getHeight() - 23);
        rect.setBorder(Rectangle.BOX);
        rect.setBorderWidth(4);
        canvas.rectangle(rect);

        // Logo
        try {
            Image image = Image.getInstance(getClass().getClassLoader().getResource("images.png"));
            image.setAlignment(Image.ALIGN_CENTER);
            image.scaleToFit(200, 200);
            document.add(image);
        } catch (Exception ignored) {}

        // Heading
        Font headingFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
        Paragraph heading = new Paragraph(
                "EXAM RESULTS\n" + studentName + " - " + studentNumber,
                headingFont);
        heading.setAlignment(Element.ALIGN_CENTER);
        heading.setSpacingBefore(30);
        heading.setSpacingAfter(30);
        document.add(heading);

        // Table
        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(100);
        table.setWidths(new int[]{3, 1, 2});

        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        table.addCell(new PdfPCell(new Phrase("Course", headerFont)));
        table.addCell(new PdfPCell(new Phrase("Grade", headerFont)));
        table.addCell(new PdfPCell(new Phrase("Interpretation", headerFont)));

        int dCount = 0;
        for (Course c : courses) {
            table.addCell(c.getCourseName());
            table.addCell(c.getGrade());
            table.addCell(c.getInterpretation());
            if ("D".equalsIgnoreCase(c.getGrade())) dCount++;
        }

        document.add(table);

        // Comment
        String comment = dCount >= 4 ? "REPEAT YEAR" : dCount > 0 ? "PROCEED AND REPEAT" : "CLEAR PASS";
        Paragraph commentPara = new Paragraph("\nCOMMENT: " + comment,
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14));
        document.add(commentPara);

        document.close();
    }
}
