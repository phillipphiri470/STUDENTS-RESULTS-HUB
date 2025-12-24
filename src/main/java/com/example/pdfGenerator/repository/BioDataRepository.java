package com.example.pdfGenerator.repository;

import com.example.pdfGenerator.model.BioData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//import java.util.List;

@Repository
public interface BioDataRepository extends JpaRepository<BioData, String> {

    BioData findByStudentNumber(String studentNumber);
}

