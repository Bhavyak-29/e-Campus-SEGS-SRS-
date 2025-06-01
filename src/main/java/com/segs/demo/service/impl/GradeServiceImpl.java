    package com.segs.demo.service.impl;

    import com.segs.demo.model.*;
    import com.segs.demo.repository.*;
    import com.segs.demo.service.GradeService;
    import jakarta.servlet.http.HttpSession;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Service;

    import java.util.*;
    import java.util.stream.Collectors;

    @Service
    public class GradeServiceImpl implements GradeService {

        @Autowired
        private GradeRepository gradeRepository;

        @Autowired
        private StudentGradeRepository studentGradeRepository;

        // @Autowired
        // private UserRepository userRepository;

        // @Autowired
        // private CourseRepository courseRepository;

        // @Autowired
        // private ExamTypeRepository examTypeRepository;

        // @Autowired
        // private TermRepository termRepository;

        @Autowired
        private EnrollmentRepository enrollmentRepository;  // Assuming this exists to get Enrollment records

        @Override
        public List<Grade> getAllGrades() {
            return gradeRepository.findAll();
        }

        @Override
        public List<StudentGradeDTO> getStudentGrades(Long CRSID, Long examTypeId, List<String> selectedGrades) {
            List<StudentGradeDTO> allStudentGrades = studentGradeRepository.findByCRSIDAndExamTypeId(CRSID, examTypeId);

            if (selectedGrades == null || selectedGrades.isEmpty()) {
                return allStudentGrades;
            }

            return allStudentGrades.stream()
                    .filter(sg -> selectedGrades.contains(sg.getGrade()))
                    .collect(Collectors.toList());
        }

        @Override
        public String uploadGrades(GradeUploadForm form, HttpSession session) {
            try {
                    Object facultyIdObj = session.getAttribute("userid");
                    if (facultyIdObj == null) return "Faculty not logged in";

                    int facultyId = Integer.parseInt(facultyIdObj.toString());

                    Grade grade = new Grade();

                    // Directly map all values from GradeUploadForm
                    Enrollment enrollment = new Enrollment();
                    enrollment.setId(Long.parseLong("100"));  // ← You must define this method

                    Student student = new Student();
                    student.setStdinstid(form.getStudentId());  // or Integer, depending on your Student ID type
                    enrollment.setStudent(student);

                    Course course = new Course();
                    course.setId(Long.parseLong(form.getCourse()));
                    enrollment.setCourse(course);

                    Term term = new Term();
                    term.setId(Long.parseLong(form.getTerm()));
                    enrollment.setTerm(term);

                    grade.setEnrollment(enrollment);

                    Users faculty = new Users();
                    faculty.setUserId(facultyId);
                    grade.setFaculty(faculty);

                    ExamType examType = new ExamType();
                    examType.setId((form.getExamtype()));
                    grade.setExamType((form.getExamtype()));

                    grade.setGradeValue(form.getGrade().trim().toUpperCase());
                    grade.setRemarks(null);
                    grade.setRowState(1);
                    grade.setId(Long.parseLong("100"));
                    Enrollment enrollment2 = enrollmentRepository.save(grade.getEnrollment()); // persist the enrollment first
                    grade.setEnrollment(enrollment2);
                    gradeRepository.save(grade);

                    return "Grade uploaded successfully";
                } catch (Exception e) {
                    e.printStackTrace();
                    return "Error occurred: " + e.getMessage();
                }
            }
            // private Long generateUniqueEnrollmentId() {
            //     return System.currentTimeMillis(); // or UUID/random-based strategy
            // }
        }


        