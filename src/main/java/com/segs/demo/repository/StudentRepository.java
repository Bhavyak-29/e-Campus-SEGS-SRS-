package com.segs.demo.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.segs.demo.model.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

       

    // --- Existing methods (unchanged) ---
    Page<Student> findByStdinstidAndStdrowstateGreaterThan(String stdinstid, int state, Pageable pageable);
    Page<Student> findByStdfirstnameContainingIgnoreCaseAndStdlastnameContainingIgnoreCaseAndStdrowstateGreaterThan(String fname, String lname, int state, Pageable pageable);
    Page<Student> findByStdfirstnameContainingIgnoreCaseAndStdrowstateGreaterThan(String fname, int state, Pageable pageable);
    Page<Student> findByStdlastnameContainingIgnoreCaseAndStdrowstateGreaterThan(String lname, int state, Pageable pageable);

    // --- New methods added based on original SQL queries ---

    /**
     * Corresponds to: SELECT COUNT(*) FROM STUDENTS WHERE UPPER(STDFIRSTNAME) LIKE ?
     */
    long countByStdfirstnameContainingIgnoreCaseAndStdrowstateGreaterThan(String stdfirstname, int rowstate);

    /**
     * Corresponds to: SELECT COUNT(*) FROM STUDENTS WHERE UPPER(STDLASTNAME) LIKE ?
     */
    long countByStdlastnameContainingIgnoreCaseAndStdrowstateGreaterThan(String stdlastname, int rowstate);

    /**
     * Corresponds to: SELECT COUNT(*) FROM STUDENTS WHERE UPPER(STDFIRSTNAME) LIKE ? AND UPPER(STDLASTNAME) LIKE ?
     */
    long countByStdfirstnameContainingIgnoreCaseAndStdlastnameContainingIgnoreCaseAndStdrowstateGreaterThan(String stdfirstname, String stdlastname, int rowstate);

    /**
     * Corresponds to the complex search query joining multiple tables to find students
     * by first and last name, retrieving only the latest registration record.
     */
    @Query("SELECT s FROM Student s JOIN s.batch b JOIN b.program p JOIN StudentRegistrations sr ON sr.student = s JOIN sr.semester sem " +
           "WHERE UPPER(s.stdfirstname) LIKE %:firstName% AND UPPER(s.stdlastname) LIKE %:lastName% AND s.stdrowstate > 0 " +
           "AND sr.srgid IN (SELECT MAX(sr_inner.srgid) FROM StudentRegistrations sr_inner WHERE sr_inner.student = s)")
    List<Student> findStudentsByFullNameWithLatestRegistration(@Param("firstName") String firstName, @Param("lastName") String lastName);

    /**
     * Corresponds to the complex search query by student's institutional ID.
     */
    @Query("SELECT s FROM Student s JOIN s.batch b JOIN b.program p JOIN StudentRegistrations sr ON sr.student = s JOIN sr.semester sem " +
           "WHERE s.stdinstid = :stdinstid AND s.stdrowstate > 0 " +
           "AND sr.srgid IN (SELECT MAX(sr_inner.srgid) FROM StudentRegistrations sr_inner WHERE sr_inner.student = s)")
    List<Student> findStudentByInstIdWithLatestRegistration(@Param("stdinstid") String stdinstid);


    
    @Query("SELECT s.stdid FROM Student s WHERE s.stdinstid = :stdinstid ORDER BY stdid LIMIT 1")
    Long findStudentIdByInstituteId(@Param("stdinstid") String stdinstid);
}