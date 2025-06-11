package com.segs.demo.service.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.stereotype.Service; // For current timestamp

import com.segs.demo.model.Course; // For converting LocalDateTime to Timestamp if needed for NativeQuery
import com.segs.demo.model.Egcrstt1;
import com.segs.demo.model.Egcrstt1Id;
import com.segs.demo.model.Enrollment;
import com.segs.demo.model.Grade;
import com.segs.demo.model.GradeUploadForm;
import com.segs.demo.model.Student;
import com.segs.demo.model.StudentGradeDTO;
import com.segs.demo.model.Term;
import com.segs.demo.model.Users;
import com.segs.demo.repository.Eggradm1Repository;
import com.segs.demo.repository.EnrollmentRepository;
import com.segs.demo.repository.GradeRepository;
import com.segs.demo.repository.StudentRepository;
import com.segs.demo.repository.TermCourseCreditsRepository;
import com.segs.demo.repository.TermCourseRepository;
import com.segs.demo.service.GradeService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.servlet.http.HttpSession; // Import the TermCourseRepository
import jakarta.transaction.Transactional;

@Service
public class GradeServiceImpl implements GradeService {

    @Autowired
    private TermCourseCreditsRepository termCourseCreditsRepository;

    @Autowired
    private GradeRepository gradeRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private TermCourseRepository termCourseRepository; // Inject TermCourseRepository

    @PersistenceContext
    private EntityManager entityManager; // Inject EntityManager for native queries

    @Autowired
    private StudentRepository StudentRepository;

    @Autowired
    private Eggradm1Repository eggradm1Repository;


    @Override
    public List<Grade> getAllGrades() {
        return gradeRepository.findAll();
    }

    @Override
    public List<StudentGradeDTO> getStudentGrades(Long CRSID, Long trmid, Long examTypeId, List<String> selectedGrades) {
        Long tcrid = termCourseRepository.findTcridByCrsidAndTrmid(CRSID, trmid);

        if (tcrid == null) {
            System.out.println("No tcrid found for CRSID: " + CRSID + " and TRMID: " + trmid);
            return new ArrayList<>(); // Return empty list if no term course is found
        }

        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("SELECT s.stdinstid, e.obtgr_id, s.stdfirstname, s.stdemail, g.grad_lt ");
        sqlBuilder.append("FROM ec2.egcrstt1 e ");
        sqlBuilder.append("JOIN ec2.students s ON e.stud_id = s.stdid ");
        sqlBuilder.append("JOIN ec2.eggradm1 g ON e.obtgr_id = g.grad_id ");
        sqlBuilder.append("WHERE e.tcrid = :tcrid AND e.examtype_id = :examTypeId");

        if (selectedGrades != null && !selectedGrades.isEmpty()) {
            sqlBuilder.append(" AND g.grad_lt IN (:selectedGrades)");
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

            Enrollment enrollment = new Enrollment();

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
            egcrstt1Id.setTcrid(Long.parseLong(form.getTerm()));
            egcrstt1Id.setExamtypeId(form.getExamtype());
            egcrstt1Id.setStudId(form.getStudentId());

            Egcrstt1 egcrstt1 = new Egcrstt1();
            egcrstt1.setId(egcrstt1Id);

            grade.setGradeValue(form.getGrade().trim().toUpperCase());
            grade.setRemarks(null);
            grade.setRowState(1);
            grade.setId(Long.parseLong("100"));

            Enrollment savedEnrollment = enrollmentRepository.save(grade.getEnrollment());
            grade.setEnrollment(savedEnrollment); // Link the grade to the persisted enrollment

            gradeRepository.save(grade);

            return "Grade uploaded successfully";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error occurred: " + e.getMessage();
        }
    }

    
    @Transactional
    @Override
    public void saveOrUpdateGrades(List<StudentGradeDTO> gradesList, Long tcrid, Long examTypeId) {
        Long createdBy = 7L;
        Long updatedBy = 7L;
        Integer rowStatus = 1;
        BigDecimal tccCreditPoints = null;
        try {
            Optional<com.segs.demo.model.TermCourseCredits> termCourseCreditsOptional = termCourseCreditsRepository.findByTcctcrid(tcrid);
            if (termCourseCreditsOptional.isPresent()) {
                tccCreditPoints = termCourseCreditsOptional.get().getTcccreditpoints();
            } else {
                System.err.println("Warning: No TCC Credit Points entity found for TCID: " + tcrid + ". Will use null for obt_credits calculation.");
            }
        } catch (IncorrectResultSizeDataAccessException e) {
            System.err.println("Error: Multiple TCC Credit Points entities found for TCID: " + tcrid + ". Will use null for obt_credits calculation.");
        } catch (Exception e) {
            System.err.println("Error retrieving TCC Credit Points for TCID: " + tcrid + " - " + e.getMessage() + ". Will use null for obt_credits calculation.");
        }

        for (StudentGradeDTO dto : gradesList) {
            Long stdid = null;
            try {
                stdid = StudentRepository.findStudentIdByInstituteId(dto.getStudentId());
            } catch (IncorrectResultSizeDataAccessException e) {
                System.err.println("Error: Multiple student IDs found for institute ID: " + dto.getStudentId() + ". Skipping this student.");
                continue;
            } catch (Exception e) { 
                System.err.println("Error retrieving student ID for institute ID: " + dto.getStudentId() + " - " + e.getMessage() + ". Skipping this student.");
                continue;
            }

            Long gradeId = null;
            BigDecimal gradPt = null; 
            try {
                gradeId = eggradm1Repository.findGradeIdByValue(dto.getGrade());
                if (gradeId != null) {
                    try {
                        gradPt = eggradm1Repository.findGradPtByGradId(gradeId);
                        if (gradPt == null) {
                             System.err.println("Warning: No Grad Points found for Grade ID: " + gradeId + ". Will use null for obt_credits calculation.");
                        }
                    } catch (IncorrectResultSizeDataAccessException e) {
                        System.err.println("Error: Multiple Grad Points found for Grade ID: " + gradeId + ". Will use null for obt_credits calculation.");
                    } catch (Exception e) {
                        System.err.println("Error retrieving Grad Points for Grade ID: " + gradeId + " - " + e.getMessage() + ". Will use null for obt_credits calculation.");
                    }
                } else {
                    System.err.println("Error: No grade ID found for value: " + dto.getGrade() + ". Skipping this student.");
                    continue; // Skip if grade ID is not found
                }
            } catch (IncorrectResultSizeDataAccessException e) {
                System.err.println("Error: Multiple grade IDs found for value: " + dto.getGrade() + ". Skipping this student.");
                continue;
            } catch (Exception e) { // Catch other potential issues during grade ID retrieval
                System.err.println("Error retrieving grade ID for value: " + dto.getGrade() + " - " + e.getMessage() + ". Skipping this student.");
                continue;
            }
            if (stdid != null && gradeId != null) {
                // Calculate obtCredits
                BigDecimal obtCredits = null;
                if (tccCreditPoints != null && gradPt != null) {
                    obtCredits = tccCreditPoints.multiply(gradPt);
                } else {
                    System.out.println("Warning: Cannot calculate obt_credits for student " + dto.getStudentId() +
                                       " (TCID: " + tcrid + ", Grade: " + dto.getGrade() +
                                       ") due to missing tccCreditPoints (" + tccCreditPoints +
                                       ") or gradPt (" + gradPt + "). obt_credits will be NULL.");
                }

                LocalDateTime now = LocalDateTime.now();
                Timestamp currentTimestamp = Timestamp.valueOf(now);

                int updated = entityManager.createNativeQuery(
                    "UPDATE ec2.egcrstt1 " +
                    "SET obtgr_id = :gradeId, " +
                    "    obt_credits = :obtCredits, " + // Add obt_credits to update
                    "    updat_by = :updatedBy, " +
                    "    updat_dt = :updatedDt " +
                    "WHERE stud_id = :stdid AND tcrid = :tcrid AND examtype_id = :examTypeId"
                )
                .setParameter("gradeId", gradeId)
                .setParameter("obtCredits", obtCredits) // Pass the calculated obtCredits
                .setParameter("stdid", stdid)
                .setParameter("tcrid", tcrid)
                .setParameter("examTypeId", examTypeId)
                .setParameter("updatedBy", updatedBy)
                .setParameter("updatedDt", currentTimestamp)
                .executeUpdate();

                if (updated == 0) {
                    entityManager.createNativeQuery(
                        "INSERT INTO ec2.egcrstt1 (stud_id, tcrid, examtype_id, obtgr_id, obt_mks, obt_credits, crst_field1, creat_by, creat_dt, updat_by, updat_dt, row_st, crsid) " +
                        "VALUES (:stdid, :tcrid, :examTypeId, :gradeId, :obtMks, :obtCredits, :crstField1, :creatBy, :creatDt, :updatBy, :updatDt, :rowSt, :crsid)"
                    )
                    .setParameter("stdid", stdid)
                    .setParameter("tcrid", tcrid)
                    .setParameter("examTypeId", examTypeId)
                    .setParameter("gradeId", gradeId)
                    .setParameter("obtMks", null)      // obt_mark(null) - assuming obt_mks is the correct column name
                    .setParameter("obtCredits", obtCredits)   // Use the calculated obtCredits
                    .setParameter("crstField1", null)
                    .setParameter("creatBy", createdBy)
                    .setParameter("creatDt", currentTimestamp)
                    .setParameter("updatBy", updatedBy)
                    .setParameter("updatDt", null)
                    .setParameter("rowSt", rowStatus)
                    .setParameter("crsid", null)
                    .executeUpdate();
                }
            }
        }
    }
}
