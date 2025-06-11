package com.segs.demo.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.segs.demo.model.StudentGradeDTO;
import com.segs.demo.service.FileService;

@Service
public class FileServiceImpl implements FileService {

    @Override
    public boolean hasCsvFormat(MultipartFile file) {
        String type = "text/csv";
        return type.equals(file.getContentType());
    }

    @Override
    public void processAndSaveData(MultipartFile file) {
        // Not needed if you're saving via StudentGradeDTO processing
        System.out.println("This method is optional if you're using saveOrUpdateGrades()");
    }

    @Override
    public List<StudentGradeDTO> csvToStudentGradeDTOs(InputStream inputStream) {
        List<StudentGradeDTO> grades = new ArrayList<>();

        try (
            BufferedReader fileReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            CSVParser csvParser = new CSVParser(fileReader,
                    CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())
        ) {
            for (CSVRecord csvRecord : csvParser) {
                String studentId = csvRecord.get("studentid");
                String grade = csvRecord.get("grade");

                if (studentId != null && !studentId.isEmpty() && grade != null && !grade.isEmpty()) {
                    StudentGradeDTO dto = new StudentGradeDTO();
                    dto.setStudentId(studentId);
                    dto.setGrade(grade);
                    dto.setStudentName(studentId);      // Set name = ID
                    dto.setStudentEmail(null);          // Email is null in DB
                    grades.add(dto);
                }
            }

        } catch (IOException e) {
            System.err.println("Error parsing CSV file: " + e.getMessage());
        }

        return grades;
    }
}
