package com.ec2.main.service.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.stereotype.Service;

import com.ec2.main.model.Course;
import com.ec2.main.model.DropdownItem;
import com.ec2.main.model.Egcrstt1;
import com.ec2.main.model.Egcrstt1Id;
import com.ec2.main.model.Enrollment; // Added for new methods
import com.ec2.main.model.Grade; // Assuming this is your Eggradm1 equivalent
import com.ec2.main.model.GradeUploadForm;
import com.ec2.main.model.Student;
import com.ec2.main.model.StudentGradeDTO;
import com.ec2.main.model.Term;
import com.ec2.main.model.Users;
import com.ec2.main.repository.CourseRepository;
import com.ec2.main.repository.Egcrstt1Repository;
import com.ec2.main.repository.Eggradm1Repository;
import com.ec2.main.repository.EnrollmentRepository;
import com.ec2.main.repository.GradeRepository;
import com.ec2.main.repository.StudentRepository;
import com.ec2.main.repository.TermCourseCreditsRepository;
import com.ec2.main.repository.TermCourseRepository;
import com.ec2.main.repository.TermRepository;
import com.ec2.main.service.GradeService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;

@Service
public class GradeServiceImpl implements GradeService {

    @Autowired
    private TermCourseCreditsRepository termCourseCreditsRepository;

    @Autowired
    private GradeRepository gradeRepository; // Assuming this interacts with eggradm1

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private TermCourseRepository termCourseRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private Egcrstt1Repository egcrstt1Repository;

    @Autowired
    private StudentRepository StudentRepository;

    @Autowired
    private Eggradm1Repository eggradm1Repository;

    @Autowired
    private TermRepository termRepository;

    @Autowired
    private CourseRepository courseRepository;

    // --- New methods for Dropdowns as per previous response ---
    @Override
    public List<DropdownItem> getUpdatedTermCoursesByTermId(Long termId) {
        // You'll need a method in TermCourseRepository or a native query here
        // to find TermCourses that have at least one Egcrstt1 record with updat_by/updat_dt NOT NULL
        // This is a placeholder, you'll need to implement the actual query based on your DB schema
        // Example: termCourseRepository.findDistinctTermCoursesWithUpdatedGrades(termId);
        // For now, returning dummy data or implement a proper query.
        // Let's assume you have a way to find term courses by term ID.
        // This part needs a specific query in TermCourseRepository to check for 'updated' grades
        // associated with the term course, similar to Egcrstt1Repository's logic.
        // For simplicity, let's just return all active term courses for now.
        // A more precise implementation would involve a JOIN to egcrstt1 and check updat_by/updat_dt
        return termCourseRepository.findByTerm_IdAndRowStateGreaterThan(termId, (int)0)
                .stream()
                .map(tc -> new DropdownItem(String.valueOf(tc.getId()), tc.getCourse().getName()))
                .collect(Collectors.toList());
    }

    @Override
    public List<DropdownItem> getExamTypesWithUpdatedGradesByTermCourseId(Long termCourseId) {
        // This will need a specific query in ExamTypeRepository or a native query
        // to find exam types that have at least one Egcrstt1 record with updat_by/updat_dt NOT NULL
        // For now, returning all active exam types.
        // A more precise implementation would involve a JOIN to egcrstt1 and check updat_by/updat_dt
        return egcrstt1Repository.findExamTypesWithUpdatedGradesByTermCourseId(termCourseId)
                .stream()
                .map(et -> new DropdownItem(String.valueOf(et.getId()), et.getName()))
                .collect(Collectors.toList());
    }
    // --- End of New methods for Dropdowns ---


    @Override
    public List<Grade> getAllGrades() {
        return gradeRepository.findAll();
    }

    @Override
    public List<StudentGradeDTO> getUpdatedStudentGrades(Long CRSID, Long trmid, Long examTypeId, List<String> selectedGrades) {
        Long tcrid = termCourseRepository.findTcridByCrsidAndTrmid(CRSID, trmid);

        if (tcrid == null) {
            System.out.println("No tcrid found for CRSID: " + CRSID + " and TRMID: " + trmid);
            return new ArrayList<>();
        }

        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("SELECT s.stdinstid, s.stdfirstname, s.stdemail, g.grad_lt "); // Adjusted select list for DTO
        sqlBuilder.append("FROM ec2.egcrstt1 e ");
        sqlBuilder.append("JOIN ec2.students s ON e.stud_id = s.stdid ");
        sqlBuilder.append("JOIN ec2.eggradm1 g ON e.obtgr_id = g.grad_id ");
        sqlBuilder.append("WHERE e.tcrid = :tcrid AND e.examtype_id = :examTypeId ");
        sqlBuilder.append("AND e.updat_by IS NOT NULL AND e.updat_dt IS NOT NULL"); // Corrected condition

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

        List<StudentGradeDTO> updatedGrades = new ArrayList<>();
        for (Object[] row : results) {
            String studentId = (String) row[0];
            String studentName = (String) row[1]; // Corrected index
            String studentEmail = (String) row[2]; // Corrected index
            String gradeValue = (String) row[3]; // Corrected index

            updatedGrades.add(new StudentGradeDTO(studentId, studentName, studentEmail, gradeValue));
        }
        return updatedGrades;
    }

    // New method to fetch updated grades for the report, using JPA repository query
    @Override
    public List<StudentGradeDTO> getUpdatedStudentGradesForReport(Long termCourseId, Long examTypeId, List<String> selectedGrades) {
        List<StudentGradeDTO> grades = egcrstt1Repository.findUpdatedStudentGradesForReport(termCourseId, examTypeId);

        if (selectedGrades != null && !selectedGrades.isEmpty()) {
            grades = grades.stream()
                    .filter(sg -> selectedGrades.contains(sg.getGrade().trim()))
                    .collect(Collectors.toList());
        }
        return grades;
    }


    // @Override
    // public List<StudentGradeDTO> getStudentGrades(Long CRSID, Long trmid, Long examTypeId, List<String> selectedGrades) {
    //     Long tcrid = termCourseRepository.findTcridByCrsidAndTrmid(CRSID, trmid);

    //     if (tcrid == null) {
    //         System.out.println("No tcrid found for CRSID: " + CRSID + " and TRMID: " + trmid);
    //         return new ArrayList<>(); // Return empty list if no term course is found
    //     }

    //     StringBuilder sqlBuilder = new StringBuilder();
    //     sqlBuilder.append("SELECT s.stdinstid, s.stdfirstname, s.stdemail, g.grad_lt "); // Adjusted select list for DTO
    //     sqlBuilder.append("FROM ec2.egcrstt1 e ");
    //     sqlBuilder.append("JOIN ec2.students s ON e.stud_id = s.stdid ");
    //     sqlBuilder.append("JOIN ec2.eggradm1 g ON e.obtgr_id = g.grad_id ");
    //     sqlBuilder.append("WHERE e.tcrid = :tcrid AND e.examtype_id = :examTypeId");

    //     if (selectedGrades != null && !selectedGrades.isEmpty()) {
    //         sqlBuilder.append(" AND g.grad_lt IN (:selectedGrades)");
    //     }

    //     Query query = entityManager.createNativeQuery(sqlBuilder.toString());
    //     query.setParameter("tcrid", tcrid);
    //     query.setParameter("examTypeId", examTypeId);

    //     if (selectedGrades != null && !selectedGrades.isEmpty()) {
    //         query.setParameter("selectedGrades", selectedGrades);
    //     }

    //     @SuppressWarnings("unchecked")
    //     List<Object[]> results = query.getResultList();

    //     List<StudentGradeDTO> studentGrades = new ArrayList<>();
    //     for (Object[] row : results) {
    //         String studentId = (String) row[0];
    //         String studentName = (String) row[1]; // Corrected index
    //         String studentEmail = (String) row[2]; // Corrected index
    //         String gradeValue = (String) row[3]; // Corrected index

    //         studentGrades.add(new StudentGradeDTO(studentId, studentName, studentEmail, gradeValue));
    //     }
    //     return studentGrades;
    // }

    @Override
public List<StudentGradeDTO> getStudentGrades(Long CRSID, Long trmid, Long examTypeId, List<String> selectedGrades) {
    Long tcrid = termCourseRepository.findTcridByCrsidAndTrmid(CRSID, trmid);

    if (tcrid == null) {
        System.out.println("No tcrid found for CRSID: " + CRSID + " and TRMID: " + trmid);
        return new ArrayList<>();
    }

    // Step 1: Fetch students who already have grades
    StringBuilder sqlBuilder = new StringBuilder();
    sqlBuilder.append("SELECT s.stdinstid, s.stdfirstname, s.stdemail, g.grad_lt ");
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
    List<Object[]> existingGradeResults = query.getResultList();

    List<StudentGradeDTO> studentGrades = new ArrayList<>();
    for (Object[] row : existingGradeResults) {
        String studentId = (String) row[0];
        String studentName = (String) row[1];
        String studentEmail = (String) row[2];
        String gradeValue = (String) row[3];

        studentGrades.add(new StudentGradeDTO(studentId, studentName, studentEmail, gradeValue));
    }

    // Step 2: Fetch students who are registered but missing from egcrstt1
    String sqlMissing = """
        SELECT s.stdinstid, s.stdfirstname, s.stdemail 
        FROM ec2.studentregistrationcourses src
        JOIN ec2.studentregistrations sr ON src.srcsrgid = sr.srgid
        JOIN ec2.students s ON sr.srgstdid = s.stdid
        LEFT JOIN ec2.egcrstt1 gr ON gr.stud_id = sr.srgstdid AND gr.tcrid = :tcrid
        WHERE src.srctcrid = :tcrid AND gr.stud_id IS NULL
        """;

    Query missingQuery = entityManager.createNativeQuery(sqlMissing);
    missingQuery.setParameter("tcrid", tcrid);

    @SuppressWarnings("unchecked")
    List<Object[]> missingResults = missingQuery.getResultList();

    for (Object[] row : missingResults) {
        String studentId = (String) row[0];
        String studentName = (String) row[1];
        String studentEmail = (String) row[2];
        String gradeValue = "NULL"; // Default value for missing grade

        studentGrades.add(new StudentGradeDTO(studentId, studentName, studentEmail, gradeValue));
    }
    studentGrades.sort(Comparator.comparing(
        s -> s.getGrade() == null || s.getGrade().equalsIgnoreCase("NULL") ? 0 : 1
    ));

    return studentGrades;
}


    @Override
    public String getTermName(Long termId) {
        Term term = termRepository.findById(termId).orElse(null);
        return (term != null) ? term.getName() : "Unknown Term";
    }

    @Override
    public String getCourseName(Long courseId) {
        Course course = courseRepository.findById(courseId).orElse(null);
        return (course != null) ? course.getName() : "Unknown Course";
    }

    @Override
    public String uploadGrades(GradeUploadForm form, HttpSession session) {
        try {
            Object facultyIdObj = session.getAttribute("userid");
            if (facultyIdObj == null) return "Faculty not logged in";

            int facultyId = Integer.parseInt(facultyIdObj.toString());

            // ... (rest of your uploadGrades logic, no changes needed here regarding rowStatus)
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
            egcrstt1Id.setStudId(Long.parseLong(form.getStudentId()));

            Egcrstt1 egcrstt1 = new Egcrstt1();
            egcrstt1.setId(egcrstt1Id);

            grade.setGradeValue(form.getGrade().trim().toUpperCase());
            grade.setRemarks(null);
            grade.setRowState(1);
            grade.setId(Long.parseLong("100")); // This ID assignment seems problematic. Should be auto-generated or fetched.

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
        Long updatedBy = 7L; // This is the 'updat_by' value
        Integer rowStatus = 1; // This is 'row_st'
        BigDecimal tccCreditPoints = null;

        try {
            Optional<com.ec2.main.model.TermCourseCredits> termCourseCreditsOptional = termCourseCreditsRepository.findByTcctcrid(tcrid);
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
            List<Long> stdids;
            try {
                stdids = StudentRepository.findStudentIdByInstituteId(dto.getStudentId());
                if (stdids.isEmpty()) {
                    System.err.println("Warning: No student IDs found for institute ID: " + dto.getStudentId() + ". Skipping this student.");
                    continue;
                }
            } catch (Exception e) {
                System.err.println("Error retrieving student IDs for institute ID: " + dto.getStudentId() + " - " + e.getMessage() + ". Skipping this student.");
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
                    continue;
                }
            } catch (IncorrectResultSizeDataAccessException e) {
                System.err.println("Error: Multiple grade IDs found for value: " + dto.getGrade() + ". Skipping this student.");
                continue;
            } catch (Exception e) {
                System.err.println("Error retrieving grade ID for value: " + dto.getGrade() + " - " + e.getMessage() + ". Skipping this student.");
                continue;
            }

            for (Long currentStdid : stdids) {
                if (currentStdid != null && gradeId != null) {
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

                    // Attempt to update
                    int updated = entityManager.createNativeQuery(
                            "UPDATE ec2.egcrstt1 " +
                                    "SET obtgr_id = :gradeId, " +
                                    "    obt_credits = :obtCredits, " +
                                    "    updat_by = :updatedBy, " +
                                    "    updat_dt = :updatedDt, " +
                                    "    row_st = :rowSt " + // Also update row_st to 1
                                    "WHERE stud_id = :stdid AND tcrid = :tcrid AND examtype_id = :examTypeId"
                    )
                    .setParameter("gradeId", gradeId)
                    .setParameter("obtCredits", obtCredits)
                    .setParameter("stdid", currentStdid)
                    .setParameter("tcrid", tcrid)
                    .setParameter("examTypeId", examTypeId)
                    .setParameter("updatedBy", updatedBy)
                    .setParameter("updatedDt", currentTimestamp)
                    .setParameter("rowSt", rowStatus) // Set row_st to 1 on update
                    .executeUpdate();

                    if (updated == 0) {
                        // If no rows were updated, insert a new one
                        entityManager.createNativeQuery(
                                "INSERT INTO ec2.egcrstt1 (stud_id, tcrid, examtype_id, obtgr_id, obt_mks, obt_credits, crst_field1, creat_by, creat_dt, updat_by, updat_dt, row_st, crsid) " +
                                        "VALUES (:stdid, :tcrid, :examTypeId, :gradeId, :obtMks, :obtCredits, :crstField1, :creatBy, :creatDt, :updatedBy, :updatedDate, :rowSt, :crsid)"
                        )
                        .setParameter("stdid", currentStdid)
                        .setParameter("tcrid", tcrid)
                        .setParameter("examTypeId", examTypeId)
                        .setParameter("gradeId", gradeId)
                        .setParameter("obtMks", null) // Assuming obt_mks is not set during grade entry
                        .setParameter("obtCredits", obtCredits)
                        .setParameter("crstField1", null)
                        .setParameter("creatBy", createdBy)
                        .setParameter("creatDt", currentTimestamp)
                        .setParameter("updatedBy", updatedBy) // Set updat_by even on new insert if it's an "updated" grade from the start
                        .setParameter("updatedDate", currentTimestamp) // Set updat_dt even on new insert
                        .setParameter("rowSt", rowStatus)
                        .setParameter("crsid", null) // Assuming crsid is part of tcrid already
                        .executeUpdate();
                    }
                }
            }
        }
    }

    @Override
    public Map<Integer, Long> getGradeDistribution() {
        // This method needs to be implemented. findAllValidGradeIds is not defined in Egcrstt1Repository.
        // You would likely need to query egcrstt1 for obtgr_id and group by it to count.
        // For example:
        // List<Object[]> results = entityManager.createNativeQuery("SELECT obtgr_id, COUNT(*) FROM ec2.egcrstt1 WHERE obtgr_id IS NOT NULL GROUP BY obtgr_id").getResultList();
        // Map<Integer, Long> gradeCountMap = new TreeMap<>();
        // for (Object[] row : results) {
        //     gradeCountMap.put(((BigDecimal) row[0]).intValue(), ((Number) row[1]).longValue());
        // }
        // return gradeCountMap;
        return new TreeMap<>(); // Placeholder
    }
}