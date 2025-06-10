package com.segs.demo.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.segs.demo.model.Course;
import com.segs.demo.model.Egcrstt1;
import com.segs.demo.model.Egcrstt1Id;
import com.segs.demo.model.Enrollment;
import com.segs.demo.model.Grade;
import com.segs.demo.model.GradeUploadForm;
import com.segs.demo.model.Student;
import com.segs.demo.model.StudentGradeDTO;
import com.segs.demo.model.Term;
import com.segs.demo.model.Users;
import com.segs.demo.repository.EnrollmentRepository;
import com.segs.demo.repository.GradeRepository;
import com.segs.demo.repository.TermCourseRepository;
import com.segs.demo.service.GradeService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext; // Import the TermCourseRepository
import jakarta.persistence.Query;
import jakarta.servlet.http.HttpSession; // Import EntityManager

@Service
public class GradeServiceImpl implements GradeService {

    @Autowired
    private GradeRepository gradeRepository;

    // We no longer need StudentGradeRepository for the getStudentGrades method
    // @Autowired
    // private StudentGradeRepository studentGradeRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private TermCourseRepository termCourseRepository; // Inject TermCourseRepository

    @PersistenceContext
    private EntityManager entityManager; // Inject EntityManager for native queries

    @Override
    public List<Grade> getAllGrades() {
        return gradeRepository.findAll();
    }

    @Override
    public List<StudentGradeDTO> getStudentGrades(Long CRSID, Long trmid, Long examTypeId, List<String> selectedGrades) {
        // 1. Get tcrid using CRSID and TRMID
        Long tcrid = termCourseRepository.findTcridByCrsidAndTrmid(CRSID, trmid);

        if (tcrid == null) {
            System.out.println("No tcrid found for CRSID: " + CRSID + " and TRMID: " + trmid);
            return new ArrayList<>(); // Return empty list if no term course is found
        }

        // 2. Fetch stud_id and obtgr_id from egcrstt1 using tcrid and examTypeId
        // We are building a native SQL query to join necessary tables and filter results.
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("SELECT s.stdinstid, e.obtgr_id, s.stdfirstname, s.stdemail, g.grad_lt ");
        sqlBuilder.append("FROM ec2.egcrstt1 e ");
        sqlBuilder.append("JOIN ec2.students s ON e.stud_id = s.stdid ");
        sqlBuilder.append("JOIN ec2.eggradm1 g ON e.obtgr_id = g.grad_id ");
        sqlBuilder.append("WHERE e.tcrid = :tcrid AND e.examtype_id = :examTypeId");

        // Add filter for selected grades if provided
        if (selectedGrades != null && !selectedGrades.isEmpty()) {
            sqlBuilder.append(" AND g.grade_lt IN (:selectedGrades)");
        }

        Query query = entityManager.createNativeQuery(sqlBuilder.toString());
        query.setParameter("tcrid", tcrid);
        query.setParameter("examTypeId", examTypeId);

        if (selectedGrades != null && !selectedGrades.isEmpty()) {
            query.setParameter("selectedGrades", selectedGrades);
        }

        @SuppressWarnings("unchecked")
        List<Object[]> results = query.getResultList();

        List<StudentGradeDTO> studentGrades = new ArrayList<>();
        for (Object[] row : results) {
            String studentId = ((String) row[0]);
            // Long obtgrId = ((Number) row[1]).longValue(); // Not directly needed in DTO
            String studentName = (String) row[2];
            String studentEmail = (String) row[3];
            String gradeValue = (String) row[4];

            studentGrades.add(new StudentGradeDTO(studentId, studentName, studentEmail, gradeValue));
        }

        return studentGrades;
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
            // TODO: You must define a proper way to set Enrollment ID.
            // For now, using a placeholder, but this needs to be a valid, existing enrollment ID
            // or you need logic to create/find an enrollment.
            enrollment.setId(Long.parseLong("100"));

            Student student = new Student();
            student.setStdinstid(form.getStudentId());
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

            Egcrstt1Id egcrstt1Id = new Egcrstt1Id();
            // Assuming form.getTerm() maps to tcrid, and form.getStudentId() is for stud_id
            egcrstt1Id.setTcrid(Long.parseLong(form.getTerm()));
            egcrstt1Id.setExamtypeId(form.getExamtype());
            egcrstt1Id.setStudId(form.getStudentId());

            Egcrstt1 egcrstt1 = new Egcrstt1();
            egcrstt1.setId(egcrstt1Id);
            // You might need to set other properties for Egcrstt1 like obtgr_id, etc.
            // based on your Egcrstt1 entity and database design, if you plan to persist it.

            grade.setGradeValue(form.getGrade().trim().toUpperCase());
            grade.setRemarks(null);
            grade.setRowState(1);
            // TODO: Ensure Grade ID is properly generated by the database or a sequence.
            // Setting a fixed "100" is likely not correct for production.
            grade.setId(Long.parseLong("100"));

            // Save enrollment (if it's a new one or needs updating) before saving grade
            // Ensure enrollment.getId() is correctly handled (e.g., auto-generated or existing)
            Enrollment savedEnrollment = enrollmentRepository.save(grade.getEnrollment());
            grade.setEnrollment(savedEnrollment); // Link the grade to the persisted enrollment

            gradeRepository.save(grade);

            return "Grade uploaded successfully";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error occurred: " + e.getMessage();
        }
    }
}
