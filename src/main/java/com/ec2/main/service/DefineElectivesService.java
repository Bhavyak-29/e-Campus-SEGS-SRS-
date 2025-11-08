// package com.ec2.main.service;
// import com.ec2.main.model.TermCourses;
// import com.ec2.main.repository.TermCoursesRepository;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;
// import java.time.LocalDateTime;
// import java.util.List;
// import java.util.Map;
// import java.util.ArrayList;

// @Service
// public class TermCoursesService {

//     @Autowired
//     private TermCoursesRepository termCoursesRepository;

//     public void saveSelectedCourses(Long termId, List<Map<String, Object>> selectedCourses) {
//         if (selectedCourses == null || selectedCourses.isEmpty()) return;

//         Long lastId = termCoursesRepository.findMaxTcrid()+1;
//         List<TermCourses> newCourses = new ArrayList<>();

//         for (Map<String, Object> course : selectedCourses) {
//             Long crsid = ((Number) course.get("crsid")).longValue();
//             Long facultyid = course.get("facultyid") != null ? ((Number) course.get("facultyid")).longValue() : null;
//             System.out.println("Processing course CRSID=" + crsid + " for termId=" + termId);
//             System.out.println("Faculty ID: " + facultyid);
//             // Avoid duplicate (term, course)
//             if (termCoursesRepository.existsByTcrtrmidAndTcrcrsid(termId, crsid)) {
//                 continue;
//             }

//             TermCourses tc = new TermCourses();
//             tc.setTcrid(lastId);
//             tc.setTcrtrmid(59L);
//             tc.setTcrcrsid(crsid);
//             tc.setTcrfacultyid(facultyid);
//             tc.setTcrtype("REGULAR");
//             tc.setTcrroundlogic("ROUND");
//             tc.setTcrmarks(100L);
//             tc.setTcrstatus("AVAILABLE");
//             tc.setTcraccessstatus(null);
//             tc.setTcrcreatedby(3809L);
//             tc.setTcrcreatedat(LocalDateTime.now());
//             tc.setTcrlastupdatedby(null);
//             tc.setTcrlastupdatedat(null);
//             tc.setTcrrowstate(1L);

//             newCourses.add(tc);
//         }

//         termCoursesRepository.saveAll(newCourses);
//     }
// }

package com.ec2.main.service;

import com.ec2.main.model.TermCourses;
import com.ec2.main.model.Courses;
import com.ec2.main.model.Users;
import com.ec2.main.repository.TermCoursesRepository;
import com.ec2.main.repository.CoursesRepository;
import com.ec2.main.repository.UsersRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class DefineElectivesService {

    private final TermCoursesRepository termCoursesRepo;
    private final CoursesRepository coursesRepo;
    private final UsersRepository usersRepo;

    public DefineElectivesService(TermCoursesRepository termCoursesRepo,
                                  CoursesRepository coursesRepo,
                                  UsersRepository usersRepo) {
        this.termCoursesRepo = termCoursesRepo;
        this.coursesRepo = coursesRepo;
        this.usersRepo = usersRepo;
    }

    /** Copy termcourses from previous term if not already present */
    @Transactional
    public void copyPreviousTermCoursesIfNeeded(Long prevTermId, Long latestTermId, HttpSession session) {
        System.out.println("Checking to copy courses from term " + prevTermId + " to " + latestTermId);
        String sessionFlag = "coursesCopiedForTerm_" + latestTermId;
        if (Boolean.TRUE.equals(session.getAttribute(sessionFlag))) return;

        boolean exists = termCoursesRepo.existsByTcrtrmid(latestTermId);
        if (!exists) {
            List<TermCourses> prevCourses = termCoursesRepo.findByTcrtrmidOrderByTcrid(prevTermId);
            System.out.println("PrevTermId: " + prevTermId);
            System.out.println("LatestTermId: " + latestTermId);
            System.out.println("PrevCourses found: " + prevCourses.size());
            Long maxId = termCoursesRepo.findMaxTcrid();
            // for (TermCourses tc : prevCourses) {
            //     System.out.println("Prev ID=" + tc.getTcrid() + ", course=" + tc.getTcrcrsid() + ", term=" + tc.getTcrtrmid());
            // }
            for (TermCourses old : prevCourses) {
                System.out.println("Copying course from prevTermId=" + prevTermId + " to latestTermId=" + latestTermId);
                System.out.println("Old ID=" + old.getTcrid() + ", course=" + old.getTcrcrsid() + ", term=" + old.getTcrtrmid());
                TermCourses copy = new TermCourses();
                copy.setTcrid(++maxId);
                copy.setTcrtrmid(latestTermId);
                copy.setTcrcrsid(old.getTcrcrsid());
                copy.setTcrfacultyid(old.getTcrfacultyid());
                copy.setTcrtype("REGULAR");
                copy.setTcrroundlogic("ROUND");
                copy.setTcrmarks(100L);
                copy.setTcrstatus("AVAILABLE");
                copy.setTcraccessstatus(null);
                copy.setTcrcreatedby(3809L);
                copy.setTcrcreatedat(LocalDateTime.now());
                copy.setTcrrowstate(1L);
                copy.setTcrslot(null);
                copy.setTcrlastupdatedby(null);
                copy.setTcrlastupdatedat(null);
                termCoursesRepo.save(copy);
            }
        }
        session.setAttribute(sessionFlag, true);
    }

    public List<TermCourses> getTermCoursesForTerm(Long trmid) {
        return termCoursesRepo.findByTcrtrmidOrderByTcrid(trmid);
    }

    public List<Courses> searchCourses(String query) {
        return coursesRepo.findByCrsnameContainingIgnoreCaseOrCrscodeContainingIgnoreCase(query, query);
    }

    // public List<Users> searchFaculty(String query) {
    //     return usersRepo.findByUemailContainingIgnoreCase(query);
    // }

    public List<Users> searchFacultyRaw(String query) {
        return usersRepo.findByUemailContainingIgnoreCase(query);
    }

    // @Transactional
    // public void saveSelectedCourses(Long latestTermId, List<Map<String, String>> rows) {
    //     System.out.println("Saving " + rows.size() + " termcourses for term " + latestTermId);
    //     Long maxId = termCoursesRepo.findMaxTcrid();
    //     for (Map<String, String> row : rows) {
    //         String tcridStr = row.get("tcrid");
    //         boolean delete = Boolean.parseBoolean(row.getOrDefault("delete", "false"));
    //         Integer slot = row.get("slot") != null && !row.get("slot").isEmpty() ? Integer.parseInt(row.get("slot")) : null;
    //         Long facultyId = row.get("facultyid") != null && !row.get("facultyid").isEmpty() ? Long.parseLong(row.get("facultyid")) : null;

    //         if (tcridStr != null && !tcridStr.isEmpty()) {
    //             // existing row
    //             Long tcrid = Long.parseLong(tcridStr);
    //             if (delete) {
    //                 System.out.println("Deleting course with ID " + tcrid);
    //                 termCoursesRepo.deleteByTcrid(tcrid);
    //                 continue;
    //             }
    //             TermCourses tc = termCoursesRepo.findById(tcrid).orElse(null);
    //             if (tc != null) {
    //                 System.out.println("Updating course ID " + tcrid + " | Slot=" + slot + " | Faculty=" + facultyId);
    //                 tc.setTcrfacultyid(facultyId);
    //                 tc.setTcrslot(slot);
    //                 tc.setTcrlastupdatedby(3809L);
    //                 tc.setTcrlastupdatedat(LocalDateTime.now());
    //                 tc.setTcrrowstate((tc.getTcrrowstate() == null ? 1L : tc.getTcrrowstate() + 1L));
    //                 termCoursesRepo.save(tc);
    //             } else {
    //                 System.out.println(" No existing termcourse found for ID " + tcrid);
    //             }
    //         } else {
    //             // new row (insert)
    //             // Integer crsid = row.get("crsid") != null ? Integer.parseInt(row.get("crsid")) : null;
    //             Long crsid = row.get("crsid") != null ? Long.parseLong(row.get("crsid")) : null;
    //             if (crsid == null || termCoursesRepo.existsByTcrtrmidAndTcrcrsid(latestTermId, crsid)){
    //                 System.out.println("⚠️ Skipping duplicate/new invalid course CRS=" + crsid);
    //                 continue;
    //             }

    //             TermCourses newTc = new TermCourses();
    //             newTc.setTcrid(++maxId);
    //             newTc.setTcrtrmid(latestTermId);
    //             newTc.setTcrcrsid(crsid);
    //             newTc.setTcrfacultyid(facultyId);
    //             newTc.setTcrslot(slot);
    //             newTc.setTcrtype("REGULAR");
    //             newTc.setTcrroundlogic("ROUND");
    //             newTc.setTcrmarks(100L);
    //             newTc.setTcrstatus("AVAILABLE");
    //             newTc.setTcraccessstatus(null);
    //             newTc.setTcrcreatedby(3809L);
    //             newTc.setTcrcreatedat(LocalDateTime.now());
    //             newTc.setTcrrowstate(1L);
    //             newTc.setTcrlastupdatedby(null);
    //             newTc.setTcrlastupdatedat(null);
    //             System.out.println("Inserting new termcourse for CRS=" + crsid + " Faculty=" + facultyId);
    //             termCoursesRepo.save(newTc);
    //         }
    //     }
    // }

    // @Transactional
    // public void saveSelectedCourses(Long latestTermId, List<Map<String, String>> rows) {
    //     System.out.println("Saving " + rows.size() + " termcourses for term " + latestTermId);

    //     for (Map<String, String> row : rows) {
    //         String tcridStr = row.get("tcrid");
    //         boolean delete = Boolean.parseBoolean(row.getOrDefault("delete", "false"));
    //         Integer slot = row.get("slot") != null && !row.get("slot").isEmpty() ? Integer.parseInt(row.get("slot")) : null;
    //         Long facultyId = row.get("facultyid") != null && !row.get("facultyid").isEmpty() ? Long.parseLong(row.get("facultyid")) : null;
    //         Long crsid = row.get("crsid") != null ? Long.parseLong(row.get("crsid")) : null;

    //         try {
    //             if (tcridStr != null && !tcridStr.isEmpty()) {
    //                 Long tcrid = Long.parseLong(tcridStr);
    //                 if (delete) {
    //                     System.out.println("Deleting course with ID " + tcrid);
    //                     termCoursesRepo.deleteByTcrid(tcrid);
    //                     continue;
    //                 }

    //                 TermCourses tc = termCoursesRepo.findById(tcrid).orElse(null);
    //                 if (tc != null) {
    //                     System.out.println("Updating course ID " + tcrid + " | Slot=" + slot + " | Faculty=" + facultyId);
    //                     tc.setTcrfacultyid(facultyId);
    //                     tc.setTcrslot(slot);
    //                     tc.setTcrlastupdatedby(3809L);
    //                     tc.setTcrlastupdatedat(LocalDateTime.now());
    //                     tc.setTcrrowstate((tc.getTcrrowstate() == null ? 1L : tc.getTcrrowstate() + 1L));
    //                     termCoursesRepo.save(tc);
    //                 } else {
    //                     System.out.println("No existing termcourse found for ID " + tcrid);
    //                 }

    //             } else {
    //                 if (crsid == null || termCoursesRepo.existsByTcrtrmidAndTcrcrsid(latestTermId, crsid)) {
    //                     System.out.println("Skipping duplicate/new invalid course CRS=" + crsid);
    //                     continue;
    //                 }

    //                 TermCourses newTc = new TermCourses();
    //                 newTc.setTcrtrmid(latestTermId);
    //                 newTc.setTcrcrsid(crsid);
    //                 newTc.setTcrfacultyid(facultyId);
    //                 newTc.setTcrslot(slot);
    //                 newTc.setTcrtype("REGULAR");
    //                 newTc.setTcrroundlogic("ROUND");
    //                 newTc.setTcrmarks(100L);
    //                 newTc.setTcrstatus("AVAILABLE");
    //                 newTc.setTcraccessstatus(null);
    //                 newTc.setTcrcreatedby(3809L);
    //                 newTc.setTcrcreatedat(LocalDateTime.now());
    //                 newTc.setTcrrowstate(1L);
    //                 System.out.println("Inserting new termcourse for CRS=" + crsid + " Faculty=" + facultyId);
    //                 termCoursesRepo.save(newTc);
    //             }

    //         } catch (Exception e) {
    //             System.err.println("Error while processing course row: " + row);
    //             e.printStackTrace();
    //             throw e;
    //         }
    //     }

    //     System.out.println("All termcourses processed successfully.");
    // }

        @Transactional
        public void saveSelectedCourses(Long latestTermId, List<Map<String, String>> rows) {
            System.out.println(">>> saveSelectedCourses CALLED for term=" + latestTermId);
            System.out.println(">>> Received rows: " + rows);

            for (Map<String, String> row : rows) {
                try {
                    String tcridStr = row.get("tcrid");
                    System.out.println(">>> Processing row tcrid=" + tcridStr + " data=" + row);

                    boolean delete = Boolean.parseBoolean(row.getOrDefault("delete", "false"));
                    Integer slot = row.get("slot") != null && !row.get("slot").isEmpty() ? Integer.parseInt(row.get("slot")) : null;
                    Long facultyId = row.get("facultyid") != null && !row.get("facultyid").isEmpty() ? Long.parseLong(row.get("facultyid")) : null;
                    Long crsid = row.get("crsid") != null && !row.get("crsid").isEmpty() ? Long.parseLong(row.get("crsid")) : null;

                    // If tcrid present → update/delete
                    if (tcridStr != null && !tcridStr.isEmpty()) {
                        Long tcrid = Long.parseLong(tcridStr);
                        TermCourses tc = termCoursesRepo.findById(tcrid).orElse(null);
                        if (tc == null) {
                            System.out.println(">>> No TermCourse found for ID=" + tcrid);
                            continue;
                        }
                        if (delete) {
                            System.out.println(">>> Deleting course " + tcrid);
                            termCoursesRepo.delete(tc);
                            continue;
                        }

                        System.out.println(">>> Updating course ID=" + tcrid + ", slot=" + slot + ", faculty=" + facultyId);
                        tc.setTcrslot(slot);
                        tc.setTcrfacultyid(facultyId);
                        tc.setTcrlastupdatedat(LocalDateTime.now());
                        tc.setTcrlastupdatedby(3809L);
                        termCoursesRepo.save(tc);

                    } else { // Insert new
                        if (crsid == null) {
                            System.out.println(">>> Skipping insert — CRS null");
                            continue;
                        }
                        System.out.println(">>> Inserting new TermCourse for CRS=" + crsid + ", Faculty=" + facultyId);
                        Long nextId = termCoursesRepo.findMaxTcrid() + 1;
                        TermCourses newTc = new TermCourses();
                        newTc.setTcrid(nextId);
                        newTc.setTcrtrmid(latestTermId);
                        newTc.setTcrcrsid(crsid);
                        newTc.setTcrfacultyid(facultyId);
                        newTc.setTcrslot(slot);
                        newTc.setTcrtype("REGULAR");
                        newTc.setTcrroundlogic("ROUND");
                        newTc.setTcrmarks(100L);
                        newTc.setTcrstatus("AVAILABLE");
                        newTc.setTcrcreatedby(3809L);
                        newTc.setTcrcreatedat(LocalDateTime.now());
                        newTc.setTcrrowstate(1L);
                        termCoursesRepo.save(newTc);
                    }
                } catch (Exception e) {
                    System.err.println(">>> Error processing row: " + row);
                    e.printStackTrace();
                    throw e;
                }
            }
            System.out.println(">>> Finished saveSelectedCourses successfully.");
        }
    }

