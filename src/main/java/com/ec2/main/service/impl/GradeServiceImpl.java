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

import com.ec2.main.model.Courses;
import com.ec2.main.model.DropdownItem;
import com.ec2.main.model.Egcrstt1;
import com.ec2.main.model.Egcrstt1Id;
import com.ec2.main.model.Enrollment; // Added for new methods
import com.ec2.main.model.Grade; // Assuming this is your Eggradm1 equivalent
import com.ec2.main.model.GradeChangeStatusDTO;
import com.ec2.main.model.GradeUploadForm;
import com.ec2.main.model.StudentGradeDTO;
import com.ec2.main.model.Students;
import com.ec2.main.model.Terms;
import com.ec2.main.model.Users;
import com.ec2.main.model.eggrademodification;
import com.ec2.main.model.work_trail;
import com.ec2.main.repository.CoursesRepository;
import com.ec2.main.repository.Egcrstt1Repository;
import com.ec2.main.repository.Eggradm1Repository;
import com.ec2.main.repository.EnrollmentRepository;
import com.ec2.main.repository.GradeRepository;
import com.ec2.main.repository.StudentsRepository;
import com.ec2.main.repository.TermCourseCreditsRepository;
import com.ec2.main.repository.TermCoursesRepository;
import com.ec2.main.repository.TermsRepository;
import com.ec2.main.repository.eggrademodificationRepository;
import com.ec2.main.repository.work_trailRepository;
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
    private TermCoursesRepository termCoursesRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private Egcrstt1Repository egcrstt1Repository;

    @Autowired
    private StudentsRepository StudentRepository;

    @Autowired
    private Eggradm1Repository eggradm1Repository;

    @Autowired
    private TermsRepository termsRepository;

    @Autowired
    private CoursesRepository coursesRepository;

    @Autowired
    private work_trailRepository worktrailRepository;

    @Autowired
    private eggrademodificationRepository eggrademodificationRepository;

    // --- New methods for Dropdowns as per previous response ---
    @Override
    public List<DropdownItem> getUpdatedTermCoursesByTermId(Long termId) {
        return termCoursesRepository.findByTerm_TrmidAndTcrrowstateGreaterThan(termId, (int) 0)
                .stream()
                .map(tc -> new DropdownItem(String.valueOf(tc.getTcrid()), tc.getCourse().getCrsname()))
                .collect(Collectors.toList());
    }

    @Override
    public List<DropdownItem> getExamTypesWithUpdatedGradesByTermCourseId(Long termCourseId) {
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
        Long tcrid = termCoursesRepository.findTcridByCrsidAndTrmid(CRSID, trmid);

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

    @Override
    public List<StudentGradeDTO> getStudentGrades(Long CRSID, Long trmid, Long examTypeId, List<String> selectedGrades) {
        Long tcrid = termCoursesRepository.findTcridByCrsidAndTrmid(CRSID, trmid);

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
        Terms term = termsRepository.findById(termId).orElse(null);
        return (term != null) ? term.getTrmname() : "Unknown Term";
    }

    @Override
    public String getCourseName(Long courseId) {
        Courses course = coursesRepository.findById(courseId).orElse(null);
        return (course != null) ? course.getCrsname() : "Unknown Course";
    }

    @Override
    public String uploadGrades(GradeUploadForm form, HttpSession session) {
        try {
            Object facultyIdObj = session.getAttribute("userid");
            if (facultyIdObj == null) {
                return "Faculty not logged in";
            }

            int facultyId = Integer.parseInt(facultyIdObj.toString());

            Grade grade = new Grade();

            Enrollment enrollment = new Enrollment();

            Students student = new Students();
            student.setStdinstid(form.getStudentId());
            enrollment.setStudent(student);

            Courses course = new Courses();
            course.setCrsid(Long.parseLong(form.getCourse()));
            enrollment.setCourse(course);

            Terms term = new Terms();
            term.setTrmid(Long.parseLong(form.getTerm()));
            enrollment.setTerm(term);

            grade.setEnrollment(enrollment);

            Users faculty = new Users();
            faculty.setUid((long) facultyId);
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
                        System.out.println("Warning: Cannot calculate obt_credits for student " + dto.getStudentId()
                                + " (TCID: " + tcrid + ", Grade: " + dto.getGrade()
                                + ") due to missing tccCreditPoints (" + tccCreditPoints
                                + ") or gradPt (" + gradPt + "). obt_credits will be NULL.");
                    }

                    LocalDateTime now = LocalDateTime.now();
                    Timestamp currentTimestamp = Timestamp.valueOf(now);

                    // Attempt to update
                    int updated = entityManager.createNativeQuery(
                            "UPDATE ec2.egcrstt1 "
                            + "SET obtgr_id = :gradeId, "
                            + "    obt_credits = :obtCredits, "
                            + "    updat_by = :updatedBy, "
                            + "    updat_dt = :updatedDt, "
                            + "    row_st = :rowSt "
                            + "WHERE stud_id = :stdid AND tcrid = :tcrid AND examtype_id = :examTypeId"
                    )
                            .setParameter("gradeId", gradeId)
                            .setParameter("obtCredits", obtCredits)
                            .setParameter("stdid", currentStdid)
                            .setParameter("tcrid", tcrid)
                            .setParameter("examTypeId", examTypeId)
                            .setParameter("updatedBy", updatedBy)
                            .setParameter("updatedDt", currentTimestamp)
                            .setParameter("rowSt", rowStatus)
                            .executeUpdate();

                    if (updated == 0) {
                        entityManager.createNativeQuery(
                                "INSERT INTO ec2.egcrstt1 (stud_id, tcrid, examtype_id, obtgr_id, obt_mks, obt_credits, crst_field1, creat_by, creat_dt, updat_by, updat_dt, row_st, crsid) "
                                + "VALUES (:stdid, :tcrid, :examTypeId, :gradeId, :obtMks, :obtCredits, :crstField1, :creatBy, :creatDt, :updatedBy, :updatedDate, :rowSt, :crsid)"
                        )
                                .setParameter("stdid", currentStdid)
                                .setParameter("tcrid", tcrid)
                                .setParameter("examTypeId", examTypeId)
                                .setParameter("gradeId", gradeId)
                                .setParameter("obtMks", null)
                                .setParameter("obtCredits", obtCredits)
                                .setParameter("crstField1", null)
                                .setParameter("creatBy", createdBy)
                                .setParameter("creatDt", currentTimestamp)
                                .setParameter("updatedBy", updatedBy)
                                .setParameter("updatedDate", currentTimestamp)
                                .setParameter("rowSt", rowStatus)
                                .setParameter("crsid", null)
                                .executeUpdate();
                    }
                }
            }
        }
    }

    @Override
    public Map<Long, Long> getGradeDistribution() {
        return new TreeMap<>();
    }

    @Transactional
    @Override
    public void submitGradeChange(Long facultyId, Long tcrid, Long examtypeId, String studInstId, String newGradeLetter, String remarks) {

        List<Long> ids = StudentRepository.findStudentIdByInstituteId(studInstId);
        if (ids.isEmpty()) {
            throw new RuntimeException("Student not found with institute ID: " + studInstId);
        }
        Long studId = ids.get(0);

        Long newGradeId = eggradm1Repository.findGradeIdByValue(newGradeLetter);

        Egcrstt1Id key = new Egcrstt1Id();
        key.setTcrid(tcrid);
        key.setExamtypeId(examtypeId);
        key.setStudId(studId);

        Egcrstt1 record = egcrstt1Repository.findById(key)
                .orElseThrow(() -> new RuntimeException("Grade record not found for student " + studId));

        Long presentGradeId = record.getObtainedGradeId();
        Long ID = generateNextWorkId();
        //System.out.println(ID+"--------this is the ID");
        eggrademodification modification = new eggrademodification();
        modification.setGmdid(ID);
        modification.setGmdtcrid(tcrid);
        modification.setGmdexamtype_id(examtypeId);
        modification.setGmdstdid(studId);
        modification.setGmditerationno(1L);
        modification.setGmdpresentgrade(presentGradeId);
        modification.setGmdnewgrade(newGradeId);
        modification.setGmdchangedesc("Faculty modified grade");
        modification.setGmdcreateby(facultyId.toString());
        modification.setGmdtcreatedt(LocalDateTime.now());
        modification.setGmdrowstate(1L);
        eggrademodificationRepository.save(modification);

        work_trail trail = new work_trail();
        trail.setWork_id(ID);
        trail.setWork_type_code(28L);
        trail.setNode_number(0L);
        trail.setEmployee_id(facultyId);
        trail.setInteration_number(1L);
        trail.setResponse_code(1L);
        trail.setResponse_date(LocalDateTime.now());
        trail.setRemarks(remarks);
        trail.setPrev_node_number(null);
        trail.setPrev_iteration_number(null);
        trail.setPrev_employee_id(null);
        worktrailRepository.save(trail);
    }

    private Long generateNextGmdId() {
        Long maxId = eggrademodificationRepository.findMaxId();
        return (maxId == null) ? 1L : maxId + 1;
    }

    private Long generateNextWorkId() {
        Long maxId = worktrailRepository.findMaxId();
        return (maxId == null) ? 1L : maxId + 1;
    }

    @Override
    public List<GradeChangeStatusDTO> getGradeChangeStatuses(Long facultyId, String facultyUsername, Long tcrid) {
        List<eggrademodification> modifications
                = eggrademodificationRepository.findByGmdcreatebyAndGmdtcrid(
                        facultyId.toString(), tcrid);

        List<GradeChangeStatusDTO> dtos = new ArrayList<>();

        for (eggrademodification mod : modifications) {
            GradeChangeStatusDTO dto = new GradeChangeStatusDTO();

            Students student = StudentRepository.findStudent(mod.getGmdstdid());
            if (student != null) {
                dto.setStudentInstituteId(student.getStdinstid());
            } else {
                dto.setStudentInstituteId("Unknown");
            }

            String presentGrade = eggradm1Repository.getGrade(mod.getGmdpresentgrade());
            String newGrade = eggradm1Repository.getGrade(mod.getGmdnewgrade());

            dto.setPresentGradeLetter(presentGrade);
            dto.setNewGradeLetter(newGrade);
            System.out.println(mod.getGmdstdid());
            List<work_trail> trails = worktrailRepository
                    //.findByEmployeeIdAndWorkTypeCode(facultyId, 28L)
                    .findByWorkIdAndWorkTypeCode(mod.getGmdid(), 28L)
                    .stream()
                    .filter(wt -> wt.getWork_id().equals(mod.getGmdid()))
                    .toList();
            //System.out.println(trails);
            if (!trails.isEmpty()) {
                dto.setStatusDean("Pending");
                dto.setStatusRegistrar("Pending");

                for (work_trail wt : trails) {
                    //System.out.println("the work_id is "+wt.getWork_id());
                    Long node = wt.getNode_number();
                    Long resp = wt.getResponse_code();
                    if (node == 0 && resp == 1) {
                        dto.setStatusSubmitted("Submitted Successfully");
                    } else if (node == 1) {
                        dto.setStatusSubmitted("Submitted Successfully");
                        dto.setStatusDean(mapResponse(resp));
                    } else if (node == 2) {
                        dto.setStatusSubmitted("Submitted Successfully");
                        dto.setStatusDean("Approved");
                        dto.setStatusRegistrar(mapResponse(resp));
                    }
                    dto.setRemarks(wt.getRemarks());
                }
                dtos.add(dto);
            }
        }

        return dtos;
    }

    private String mapResponse(Long response) {
        if (response == null) {
            return "Pending";
        }
        return switch (response.intValue()) {
            case 0 ->
                "Pending";
            case 1 ->
                "Approved";
            case 2 ->
                "Rejected";
            default ->
                "Unknown";
        };
    }

    @Override
    public List<GradeChangeStatusDTO> getPendingDeanRequests() {
        List<work_trail> pendingWork = worktrailRepository.findPendingDeanRequests();
        List<GradeChangeStatusDTO> dtos = new ArrayList<>();

        for (work_trail wt : pendingWork) {
            eggrademodification mod = eggrademodificationRepository.findById(wt.getWork_id()).orElse(null);
            if (mod == null) {
                continue;
            }

            GradeChangeStatusDTO dto = new GradeChangeStatusDTO();

            Students student = StudentRepository.findStudent(mod.getGmdstdid());
            dto.setStudentInstituteId(student != null ? student.getStdinstid() : "Unknown");
            dto.setGmdid(wt.getWork_id());
            dto.setPresentGradeLetter(eggradm1Repository.getGrade(mod.getGmdpresentgrade()));
            dto.setNewGradeLetter(eggradm1Repository.getGrade(mod.getGmdnewgrade()));
            dto.setRemarks(wt.getRemarks());
            dto.setStatusSubmitted("Submitted Successfully");
            dto.setStatusDean("Pending");
            dto.setStatusRegistrar("Pending");

            dtos.add(dto);
        }

        return dtos;
    }

    @Override
    public List<GradeChangeStatusDTO> getPendingRegistrarRequests() {
        // List<work_trail> pendingWork = worktrailRepository.findPendingRegistrarRequests();
        // List<GradeChangeStatusDTO> dtos = new ArrayList<>();

        // for (work_trail wt : pendingWork) {
        //     eggrademodification mod = eggrademodificationRepository.findById(wt.getWork_id()).orElse(null);
        //     if (mod == null) continue;
        //     GradeChangeStatusDTO dto = new GradeChangeStatusDTO();
        //     Students student = StudentRepository.findStudent(mod.getGmdstdid());
        //     dto.setStudentInstituteId(student != null ? student.getStdinstid() : "Unknown");
        //     dto.setGmdid(wt.getWork_id());
        //     dto.setPresentGradeLetter(eggradm1Repository.getGrade(mod.getGmdpresentgrade()));
        //     dto.setNewGradeLetter(eggradm1Repository.getGrade(mod.getGmdnewgrade()));
        //     dto.setStatusSubmitted("Submitted Successfully");
        //     dto.setStatusDean("Approved");
        //     dto.setStatusRegistrar("Pending");
        //     dtos.add(dto);
        // }
        // return dtos;
        List<Object[]> results = worktrailRepository.findPendingRegistrarRequestsNative();
        List<GradeChangeStatusDTO> dtos = new ArrayList<>();

        for (Object[] row : results) {
            GradeChangeStatusDTO dto = new GradeChangeStatusDTO();
            dto.setStudentInstituteId((String) row[0]);
            dto.setPresentGradeLetter((String) row[1]);
            dto.setNewGradeLetter((String) row[2]);
            dto.setGmdid(((Number) row[3]).longValue());
            dto.setStatusSubmitted((String) row[4]);
            dto.setStatusDean((String) row[5]);
            dto.setStatusRegistrar((String) row[6]);
            dto.setRemarks((String) row[7]);
            dtos.add(dto);
        }

        return dtos;
    }

    @Transactional
    @Override
    public boolean processDeanAction(Long gmdid, Long deanId, String action) {
        eggrademodification mod = eggrademodificationRepository.findById(gmdid).orElse(null);
        if (mod == null) {
            return false;
        }

        // Check if Dean already acted on this request
        boolean alreadyProcessed = worktrailRepository.existsByWorkIdAndNodeNumberIn(gmdid, List.of(1L));
        if (alreadyProcessed) {
            return false;
        }

        // Create a new work_trail record for Dean action (do NOT update old one)
        work_trail wt = new work_trail();
        wt.setWork_id(mod.getGmdid());
        wt.setWork_type_code(28L);
        wt.setNode_number(1L); // Dean node
        wt.setEmployee_id(deanId);
        wt.setInteration_number(1L); // Optional, could be auto-incremented
        wt.setResponse_code(action.equalsIgnoreCase("approve") ? 1L : 2L);
        wt.setResponse_date(LocalDateTime.now());
        wt.setRemarks(action.equalsIgnoreCase("approve") ? "Approved by Dean AP" : "Rejected by Dean AP");
        wt.setPrev_node_number(0L); // previous node = faculty
        wt.setPrev_iteration_number(1L);
        wt.setPrev_employee_id(mod.getGmdcreateby() != null ? Long.parseLong(mod.getGmdcreateby()) : null);

        worktrailRepository.save(wt);
        return true;
    }

    @Transactional
    @Override
    public boolean processRegistrarAction(Long gmdid, Long registrarId, String action) {
        eggrademodification mod = eggrademodificationRepository.findById(gmdid).orElse(null);
        if (mod == null) {
            return false;
        }

        // Check if Registrar already acted on this request
        boolean alreadyProcessed = worktrailRepository.existsByWorkIdAndNodeNumberIn(gmdid, List.of(2L));
        if (alreadyProcessed) {
            return false;
        }

        // Create new work_trail record for Registrar action (do NOT update)
        work_trail wt = new work_trail();
        wt.setWork_id(mod.getGmdid());
        wt.setWork_type_code(28L);
        wt.setNode_number(2L); // Registrar node
        wt.setEmployee_id(registrarId);
        wt.setInteration_number(1L);
        wt.setResponse_code(action.equalsIgnoreCase("approve") ? 1L : 2L);
        wt.setResponse_date(LocalDateTime.now());
        wt.setRemarks(action.equalsIgnoreCase("approve") ? "Approved by Registrar" : "Rejected by Registrar");
        wt.setPrev_node_number(1L); // previous = Dean
        wt.setPrev_iteration_number(1L);
        wt.setPrev_employee_id(1150L); // Dean’s ID

        worktrailRepository.save(wt);

        // If registrar approves, update the grade table
        if (action.equalsIgnoreCase("approve")) {
            Egcrstt1Id id = new Egcrstt1Id();
            id.setTcrid(mod.getGmdtcrid());
            id.setExamtypeId(mod.getGmdexamtype_id());
            id.setStudId(mod.getGmdstdid());

            Egcrstt1 record = egcrstt1Repository.findById(id).orElse(null);
            if (record != null) {
                record.setObtainedGradeId(mod.getGmdnewgrade());
                record.setUpdatedBy(20L);
                record.setUpdatedDate(LocalDateTime.now());
                egcrstt1Repository.save(record);
            } else {
                Egcrstt1 newRecord = new Egcrstt1();
                newRecord.setId(id);
                newRecord.setObtainedGradeId(mod.getGmdnewgrade());
                newRecord.setCreatedBy(20L);
                newRecord.setCreatedDate(LocalDateTime.now());
                newRecord.setRowStatus("1");
                egcrstt1Repository.save(newRecord);
            }
        }
        return true;
    }

    // @Transactional
    // @Override
    // public boolean processDeanAction(Long gmdid, Long deanId, String action) {
    //     eggrademodification mod = eggrademodificationRepository.findById(gmdid).orElse(null);
    //     if (mod == null) return false;
    //     // Check if already processed
    //     boolean alreadyProcessed = worktrailRepository.existsByWorkIdAndNodeNumberIn(gmdid, List.of(1L, 2L));
    //     if (alreadyProcessed) return false;
    //     // Create new work_trail record for Dean action
    //     work_trail wt = new work_trail();
    //     wt.setWork_id(mod.getGmdid());
    //     wt.setWork_type_code(28L);
    //     wt.setNode_number(1L); // Dean node
    //     wt.setEmployee_id(deanId);
    //     wt.setInteration_number(1L); // You can decide iteration logic
    //     wt.setResponse_code(action.equalsIgnoreCase("approve") ? 1L : 2L);
    //     wt.setResponse_date(LocalDateTime.now());
    //     wt.setRemarks(action.equalsIgnoreCase("approve") ? "Approved by Dean AP" : "Rejected by Dean AP");
    //     wt.setPrev_node_number(0L);
    //     wt.setPrev_iteration_number(0L);
    //     wt.setPrev_employee_id(mod.getGmdcreateby() != null ? Long.parseLong(mod.getGmdcreateby()) : null);
    //     worktrailRepository.save(wt);
    //     return true;
    // }
    // @Transactional
    // @Override
    // public boolean processRegistrarAction(Long gmdid, Long deanId, String action) {
    //     eggrademodification mod = eggrademodificationRepository.findById(gmdid).orElse(null);
    //     if (mod == null) return false;
    //     // Check if already processed
    //     boolean alreadyProcessed = worktrailRepository.existsByWorkIdAndNodeNumberIn(gmdid, List.of( 2L));
    //     if (alreadyProcessed) return false;
    //     // Create new work_trail record for Dean action
    //     work_trail wt = new work_trail();
    //     wt.setWork_id(mod.getGmdid());
    //     wt.setWork_type_code(28L);
    //     wt.setNode_number(2L); // Dean node
    //     wt.setEmployee_id(deanId);
    //     wt.setInteration_number(1L); // You can decide iteration logic
    //     wt.setResponse_code(action.equalsIgnoreCase("approve") ? 1L : 2L);
    //     wt.setResponse_date(LocalDateTime.now());
    //     wt.setRemarks(action.equalsIgnoreCase("approve") ? "Approved by Registrar" : "Rejected by Registrar");
    //     wt.setPrev_node_number(1L);
    //     wt.setPrev_iteration_number(1L);
    //     wt.setPrev_employee_id(1150L);
    //     worktrailRepository.save(wt);
    //     if (action.equalsIgnoreCase("approve")) {
    //     Egcrstt1Id id = new Egcrstt1Id();
    //     id.setTcrid(mod.getGmdtcrid());
    //     id.setExamtypeId(mod.getGmdexamtype_id());
    //     id.setStudId(mod.getGmdstdid());
    //     Egcrstt1 record = egcrstt1Repository.findById(id).orElse(null);
    //     if (record != null) {
    //         record.setObtainedGradeId(mod.getGmdnewgrade());
    //         record.setUpdatedBy(20L);
    //         record.setUpdatedDate(LocalDateTime.now());
    //         egcrstt1Repository.save(record);
    //     } else {
    //         // Optionally, create new record if not exists
    //         Egcrstt1 newRecord = new Egcrstt1();
    //         newRecord.setId(id);
    //         newRecord.setObtainedGradeId(mod.getGmdnewgrade());
    //         newRecord.setCreatedBy(20L);
    //         newRecord.setCreatedDate(LocalDateTime.now());
    //         newRecord.setRowStatus("1"); // active
    //         egcrstt1Repository.save(newRecord);
    //     }
    // }
    //     return true;
    // }
}
