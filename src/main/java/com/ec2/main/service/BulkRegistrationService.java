package com.ec2.main.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.ec2.main.model.StudentRegistrationCourses;
import com.ec2.main.repository.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class BulkRegistrationService {

    @Autowired
    private AcademicYearsRepository academicYearsRepository;

    @Autowired
    private TermsRepository termsRepository;

    @Autowired
    private StudentsRepository studentsRepository;

    @Autowired
    private SemestersRepository semestersRepository;

    @Autowired
    private StudentRegistrationsRepository studentRegistrationsRepository;

    @Autowired
    private StudentRegistrationSaveService studentRegistrationSaveService;

    @Autowired
    private CoursesRepository coursesRepository;

    @Autowired
    private StudentRegistrationCoursesRepository studentRegistrationCoursesRepository;

    @Autowired
    private TermCoursesRepository termCoursesRepository;

    @Autowired
    private SemesterCoursesRepository semesterCoursesRepository;

    public void processBulkRegistration(MultipartFile file) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
        String line;
        int lineNumber = 0;
        String academicYear = "";
        String termName = "";

        Map<String, List<String>> studentCoursesMap = new HashMap<>();

        while ((line = reader.readLine()) != null) {
            lineNumber++;

            if (lineNumber == 1 && line.startsWith("\uFEFF")) {
                line = line.substring(1);
            }

            line = line.trim();

            if (line.isEmpty()) {
                continue;
            }

            String[] tokens = line.split(",");

            if (lineNumber == 1) {
                if (tokens.length != 2) {
                    throw new IllegalArgumentException("Invalid header format at line 1. Expected AcademicYear,TermName");
                }

                academicYear = tokens[0].trim();
                termName = tokens[1].trim();

                System.out.println("Academic Year: " + academicYear);
                System.out.println("Term Name: " + termName);
            } else {
                if (tokens.length != 2) {
                    throw new IllegalArgumentException("Invalid data format at line " + lineNumber + ". Expected StudentId,CourseCode");
                }

                String studentId;
                try {
                    studentId = tokens[0].trim();
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Invalid student ID at line " + lineNumber + ": " + tokens[0]);
                }

                String courseCode = tokens[1].trim();

                if (courseCode.isEmpty()) {
                    throw new IllegalArgumentException("Empty course code at line " + lineNumber);
                }

                studentCoursesMap.computeIfAbsent(studentId, k -> new ArrayList<>()).add(courseCode);
            }
        }

        if (academicYear.isEmpty() || termName.isEmpty()) {
            throw new IllegalArgumentException("Missing academic year or term name in header row.");
        }

        System.out.println("Student Courses Map:");
        for (Map.Entry<String, List<String>> entry : studentCoursesMap.entrySet()) {
            String studentId = entry.getKey();
            List<String> courses = entry.getValue();
            System.out.println("Student INSTID: " + studentId + ", Courses: " + courses);
        }

        Long ayrId = academicYearsRepository.findAcademicYearIdByName(academicYear);
        if (ayrId == null) {
            throw new IllegalArgumentException("Academic year '" + academicYear + "' not found.");
        }
        System.out.println("Academic Year ID: " + ayrId);

        Long termId = termsRepository.findTermIdByName(termName, ayrId);
        if (termId == null) {
            throw new IllegalArgumentException("Term '" + termName + "' not found.");
        }
        System.out.println("Term ID: " + termId);

        for(Map.Entry<String, List<String>> entry : studentCoursesMap.entrySet()) {
            String studentId = entry.getKey();
            List<String> courses = entry.getValue();

            System.out.println("Processing registration for Student ID: " + studentId);
            // System.out.println("Courses: " + courses);
            Long stdid = studentsRepository.findStdid(studentId);
            Long batchId = studentsRepository.findBatchIdByStudentId(studentId);
            Long strid = semestersRepository.findSemesterId(batchId, termId);

            System.out.println("Student ID: " + stdid);
            System.out.println("Batch ID: " + batchId);
            System.out.println("Semester ID: " + strid);

            if (stdid == null) {
                throw new IllegalArgumentException("Student with INSTID '" + studentId + "' not found.");
            }
            if (batchId == null) {
                throw new IllegalArgumentException("Batch for student with INSTID '" + studentId + "' not found.");
            }
            if (strid == null) {
                throw new IllegalArgumentException("Semester for batch ID '" + batchId + "' and term ID '" + termId + "' not found.");
            }

            Long srgid = studentRegistrationsRepository.getsrgbystdidandstrid(stdid, strid).getSrgid();
            if (srgid == null) {
                srgid = studentRegistrationSaveService.newRegistration(stdid, strid);
                System.out.println("Creating new registration with SRGID: " + srgid);
            } else {
                System.out.println("Found existing registration with SRGID: " + srgid);
            }

            for (String courseCode : courses) {
                Long courseId = coursesRepository.findCourseIdByName(courseCode);
                if (courseId == null) {
                    throw new IllegalArgumentException("Course with code '" + courseCode + "' not found.");
                }
                System.out.println("Registering course ID: " + courseId + " for SRGID: " + srgid);
                
                Long tcrid = termCoursesRepository.findTcrid(courseId, termId);
                Integer scrid = semesterCoursesRepository.findScrid(strid, courseId);
                
                System.out.println("Term Course ID (TCRID): " + tcrid);
                System.out.println("Semester Course ID (SCRID): " + scrid);

                Long count = studentRegistrationCoursesRepository.countActiveRegistrations(srgid, tcrid);
                if (count > 0) {
                    System.out.println("Skipping: Course already registered for srgid: " + srgid + ", tcrid: " + tcrid);
                } else {
                    System.out.println("Registering course for srgid: " + srgid + ", tcrid: " + tcrid + ", scrid: " + scrid);
                    StudentRegistrationCourses src = new StudentRegistrationCourses();
                    src.setSrcsrgid(srgid);
                    src.setSrctype("REGULAR");
                    src.setSrcstatus("ACTIVE");
                    src.setSrccreatedby(7L);
                    src.setSrccreatedat(LocalDateTime.now());
                    src.setSrcrowstate((long) 1);
                    src.setSrctcrid(tcrid);
                    src.setSrcscrid(scrid);
                    Optional<Long> srcid = studentRegistrationCoursesRepository.findMaxSrcid();
                    src.setSrcid(srcid.orElse(0L) + 1);

                    studentRegistrationCoursesRepository.save(src);
                }
            }
        }
    }
}

