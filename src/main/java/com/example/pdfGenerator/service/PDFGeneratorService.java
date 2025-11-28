package com.example.pdfGenerator.service;

import com.example.pdfGenerator.model.Course;
import com.example.pdfGenerator.repository.CourseRepository;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Service
public class PDFGeneratorService {

    private final CourseRepository courseRepository;

    public PDFGeneratorService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public void export(HttpServletResponse response, String studentNumber) throws IOException {
        try {
            List<Course> courses = courseRepository.findByStudentNumber(studentNumber);

            if (courses.isEmpty()) {
                throw new RuntimeException("No courses found for student number: " + studentNumber);
            }

            // Assume all rows have the same studentName & number
            String studentName = courses.get(0).getStudentName();

            Document document = new Document(PageSize.A4);
            PdfWriter writer = PdfWriter.getInstance(document, response.getOutputStream());
            document.open();

            // Draw border inset 
            PdfContentByte canvas = writer.getDirectContent();
            float margin = 23f;
            Rectangle rect = new Rectangle(
                    margin,
                    margin,
                    PageSize.A4.getWidth() - margin,
                    PageSize.A4.getHeight() - margin
            );
            rect.setBorder(Rectangle.BOX);
            rect.setBorderWidth(5);
            canvas.rectangle(rect);

            // Load logo image if available
            ClassLoader classLoader = getClass().getClassLoader();
            java.net.URL imageURL = classLoader.getResource("images.png");
            if (imageURL != null) {
                Image image = Image.getInstance(imageURL);
                image.setAlignment(Image.ALIGN_CENTER);
                image.scaleToFit(200, 200);
                document.add(image);
            }

            // Heading
            Font headingFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
            headingFont.setSize(18);
            Paragraph heading = new Paragraph("EXAM RESULTS: " + studentName + " - " + studentNumber, headingFont);
            heading.setAlignment(Paragraph.ALIGN_CENTER);
            heading.setSpacingBefore(40);
            heading.setSpacingAfter(40);
            document.add(heading);

            // Table for courses
            PdfPTable table = new PdfPTable(3);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setWidths(new int[]{3, 1, 2});

            // Headers
            Font tableHeaderFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
            table.addCell(new PdfPCell(new Phrase("Course", tableHeaderFont)));
            table.addCell(new PdfPCell(new Phrase("Grade", tableHeaderFont)));
            table.addCell(new PdfPCell(new Phrase("Interpretation", tableHeaderFont)));

            // Data
            for (Course c : courses) {
                table.addCell(c.getCourseName());
                table.addCell(c.getGrade());
                table.addCell(c.getInterpretation());
            }

            document.add(table);

// Determine comment based on grades
int dCount = 0;
for (Course c : courses) {
    if ("D".equalsIgnoreCase(c.getGrade())) {
        dCount++;
    }
}

String commentText;
if (dCount >= 4) {
    commentText = "REPEAT YEAR";
} else if (dCount > 0) {
    commentText = "PROCEED AND REPEAT";
} else {
    commentText = "CLEAR PASS";
}

// Add comment to document
Font commentFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
commentFont.setSize(14);
Paragraph comment = new Paragraph("COMMENT: " + commentText, commentFont);
comment.setAlignment(Paragraph.ALIGN_LEFT);
document.add(comment);


            document.close();

        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }
}
