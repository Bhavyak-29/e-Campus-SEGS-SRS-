package com.ec2.main.service;

import java.io.InputStream;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.ec2.main.model.StudentGradeDTO;
import com.ec2.main.model.User2;

public interface FileService {
    boolean hasCsvFormat(MultipartFile file);
    void processAndSaveData(MultipartFile file);
    List<StudentGradeDTO> csvToStudentGradeDTOs(InputStream inputStream);
}
