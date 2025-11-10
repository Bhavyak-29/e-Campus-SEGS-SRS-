// package com.ec2.main.service;
// import com.ec2.main.model.TermCourseAvailableFor;
// import com.ec2.main.model.TermCourses;
// import com.ec2.main.repository.TermCourseAvailableForRepository;
// import com.ec2.main.repository.TermCoursesRepository;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;
// import org.springframework.transaction.annotation.Transactional;
// import java.time.LocalDateTime;
// import java.util.ArrayList;
// import java.util.List;
// import java.util.Map;


// @Service
// public class OpenForService {

//     @Autowired
//     private TermCourseAvailableForRepository tcaRepo;

//     @Autowired
//     private TermCoursesRepository termCoursesRepo;

//     @Transactional
//     public void saveRows(Long latestTermId, List<Map<String, String>> rows) {
//         System.out.println(">>> saveRows CALLED for term=" + latestTermId);
//         System.out.println(">>> Received rows: " + rows);

//         for (Map<String, String> row : rows) {
//             try {
//                 String tcaidStr = row.get("tcaid");
//                 System.out.println(">>> Processing row tcaid=" + tcaidStr + " data=" + row);

//                 boolean delete = Boolean.parseBoolean(row.getOrDefault("delete", "false"));
//                 Long tcatcrid = row.get("tcatcrid") != null && !row.get("tcatcrid").isEmpty()
//                         ? Long.parseLong(row.get("tcatcrid")) : null;
//                 Long tcaprgid = row.get("tcaprgid") != null && !row.get("tcaprgid").isEmpty()
//                         ? Long.parseLong(row.get("tcaprgid")) : null;
//                 Long tcabchid = row.get("tcabchid") != null && !row.get("tcabchid").isEmpty()
//                         ? Long.parseLong(row.get("tcabchid")) : null;
//                 String electiveType = row.get("tcaelectivetype");

//                 if (tcaidStr != null && !tcaidStr.isEmpty()) {
//                     Long tcaid = Long.parseLong(tcaidStr);
//                     TermCourseAvailableFor tca = tcaRepo.findById(tcaid).orElse(null);
//                     if (tca == null) {
//                         System.out.println(">>> No TCA found for ID=" + tcaid);
//                         continue;
//                     }
//                     if (delete) {
//                         System.out.println(">>> Deleting TCA " + tcaid);
//                         tcaRepo.delete(tca);
//                         continue;
//                     }

//                     System.out.println(">>> Updating TCA ID=" + tcaid);
//                     tca.setTcaelectivetype(electiveType);
//                     tca.setTcalastupdatedat(LocalDateTime.now());
//                     tca.setTcalastupdatedby(1150L);
//                     tcaRepo.save(tca);

//                 } else { // insert
//                     if (tcatcrid == null || tcaprgid == null || tcabchid == null) {
//                         System.out.println(">>> Skipping insert — missing critical fields");
//                         continue;
//                     }

//                     Long nextId = tcaRepo.getMaxTcaId() + 1;
//                     System.out.println(">>> Inserting new TCA id=" + nextId + " CRS=" + tcatcrid);

//                     TermCourseAvailableFor newTca = new TermCourseAvailableFor();
//                     newTca.setTcaid(nextId);
//                     newTca.setTcatcrid(tcatcrid);
//                     newTca.setTcaprgid(tcaprgid);
//                     newTca.setTcabchid(tcabchid);
//                     newTca.setTcaelectivetype(electiveType != null ? electiveType : "ICTE");
//                     newTca.setTcastatus("ACTIVE");
//                     newTca.setTcacreatedby(3809L);
//                     newTca.setTcacreatedat(LocalDateTime.now());
//                     newTca.setTcarowstate(1L);
//                     tcaRepo.save(newTca);
//                 }
//             } catch (Exception e) {
//                 System.err.println(">>> Error processing row: " + row);
//                 e.printStackTrace();
//                 throw e;
//             }
//         }

//         System.out.println(">>> Finished saveRows successfully.");
//     }

//     @Transactional
//     public List<TermCourseAvailableFor> copyToTCA(Long latestTermId, List<TermCourses> termCourses, Long prgid, Long bchid) {
//         System.out.println(">>> copyToTCA CALLED for term=" + latestTermId + ", program=" + prgid + ", batch=" + bchid);
//         Long nextId = tcaRepo.getMaxTcaId();

//         List<TermCourseAvailableFor> newList = new ArrayList<>();
//         for (TermCourses tc : termCourses) {
//             nextId++;
//             TermCourseAvailableFor newTca = new TermCourseAvailableFor();
//             newTca.setTcaid(nextId);
//             newTca.setTcatcrid(tc.getTcrid());
//             newTca.setTcaprgid(prgid);
//             newTca.setTcabchid(bchid);
//             newTca.setTcaelectivetype("ICTE");
//             newTca.setTcastatus("ACTIVE");
//             newTca.setTcacreatedby(3809L);
//             newTca.setTcacreatedat(LocalDateTime.now());
//             newTca.setTcarowstate(1L);
//             tcaRepo.save(newTca);
//             newList.add(newTca);
//         }

//         System.out.println(">>> Finished copyToTCA — inserted " + newList.size() + " records");
//         return newList;
//     }
// }

package com.ec2.main.service;

import com.ec2.main.model.TermCourseAvailableFor;
import com.ec2.main.model.TermCourses;
import com.ec2.main.model.Courses;
import com.ec2.main.model.Users;
import com.ec2.main.repository.TermCourseAvailableForRepository;
import com.ec2.main.repository.TermCoursesRepository;
import com.ec2.main.repository.CoursesRepository;
import com.ec2.main.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class OpenForService {

    @Autowired
    private TermCourseAvailableForRepository tcaRepo;

    @Autowired
    private TermCoursesRepository termCoursesRepo;

    @Autowired
    private CoursesRepository coursesRepo;

    @Autowired
    private UsersRepository usersRepo;

    /**
     * Build enriched rows to send to the UI.
     * Ensures copy-to-TCA if no rows exist for (latestTermId, prg, batch).
     * Returns List<Map<String,Object>> where each map contains:
     *  tcaid, tcatcrid, crsid, crscode, crsname, credits, facultyId, facultyName, slot, tcaelectivetype
     */
    @Transactional
    public List<Map<String, Object>> loadFor(Long latestTermId, Long prgid, Long bchid) {
        List<TermCourseAvailableFor> tcas = tcaRepo.findByTermProgramBatch(latestTermId, prgid, bchid);
        if (tcas.isEmpty()) {
            // copy from termcourses first
            List<TermCourses> termCourses = termCoursesRepo.findByTcrtrmidOrderByTcrid(latestTermId);
            if (!termCourses.isEmpty()) {
                copyToTCA(latestTermId, termCourses, prgid, bchid);
                tcas = tcaRepo.findByTermProgramBatch(latestTermId, prgid, bchid);
            }
        }

        List<Map<String, Object>> out = new ArrayList<>();
        for (TermCourseAvailableFor tca : tcas) {
            Map<String, Object> m = new HashMap<>();
            m.put("tcaid", tca.getTcaid());
            m.put("tcatcrid", tca.getTcatcrid());
            // fetch termcourse to get crsid, slot, faculty
            TermCourses tc = termCoursesRepo.findById(tca.getTcatcrid()).orElse(null);
            if (tc != null) {
                m.put("tcrid", tc.getTcrid());
                m.put("crsid", tc.getTcrcrsid());
                // course details
                Courses c = null;
                if (tc.getTcrcrsid() != null) {
                    c = coursesRepo.findById(tc.getTcrcrsid()).orElse(null);
                }
                m.put("crscode", c != null ? c.getCrscode() : "");
                m.put("crsname", c != null ? (c.getCrsname() != null ? c.getCrsname() : c.getCrstitle()) : "");
                m.put("credits", c != null ? c.getCrscreditpoints() : null);

                m.put("slot", tc.getTcrslot());
                m.put("facultyId", tc.getTcrfacultyid());
                Users u = tc.getTcrfacultyid() != null ? usersRepo.findById(tc.getTcrfacultyid()).orElse(null) : null;
                // display using uemail if exists, fallback to ufullname or uname
                String display = "";
                if (u != null) {
                    if (u.getUemail() != null && !u.getUemail().isEmpty()) display = u.getUemail().replace('_', ' ');
                    else if (u.getUfullname() != null && !u.getUfullname().isEmpty()) display = u.getUfullname();
                    else display = u.getUname();
                }
                m.put("facultyName", display);
            } else {
                // missing linked termcourse — still include tca basic info
                m.put("tcrid", null);
                m.put("crsid", null);
                m.put("crscode", "");
                m.put("crsname", "");
                m.put("credits", null);
                m.put("slot", null);
                m.put("facultyId", null);
                m.put("facultyName", "");
            }

            m.put("tcaelectivetype", tca.getTcaelectivetype());
            out.add(m);
        }
        return out;
    }

    /**
     * Save rows (insert/update/delete) — your existing implementation adapted.
     * rows: list of maps with keys as strings (tcaid, tcatcrid, tcaprgid, tcabchid, tcaelectivetype, delete)
     */
    @Transactional
    public void saveRows(Long latestTermId, List<Map<String, String>> rows) {
        System.out.println(">>> saveRows CALLED for term=" + latestTermId);
        System.out.println(">>> Received rows: " + rows);

        for (Map<String, String> row : rows) {
            try {
                String tcaidStr = row.get("tcaid");
                System.out.println(">>> Processing row tcaid=" + tcaidStr + " data=" + row);

                boolean delete = Boolean.parseBoolean(row.getOrDefault("delete", "false"));
                Long tcatcrid = row.get("tcatcrid") != null && !row.get("tcatcrid").isEmpty()
                        ? Long.parseLong(row.get("tcatcrid")) : null;
                Long tcaprgid = row.get("tcaprgid") != null && !row.get("tcaprgid").isEmpty()
                        ? Long.parseLong(row.get("tcaprgid")) : null;
                Long tcabchid = row.get("tcabchid") != null && !row.get("tcabchid").isEmpty()
                        ? Long.parseLong(row.get("tcabchid")) : null;
                String electiveType = row.get("tcaelectivetype");

                if (tcaidStr != null && !tcaidStr.isEmpty()) {
                    Long tcaid = Long.parseLong(tcaidStr);
                    TermCourseAvailableFor tca = tcaRepo.findById(tcaid).orElse(null);
                    if (tca == null) {
                        System.out.println(">>> No TCA found for ID=" + tcaid);
                        continue;
                    }
                    if (delete) {
                        System.out.println(">>> Deleting TCA " + tcaid);
                        tcaRepo.delete(tca);
                        continue;
                    }

                    System.out.println(">>> Updating TCA ID=" + tcaid);
                    tca.setTcaelectivetype(electiveType);
                    tca.setTcalastupdatedat(LocalDateTime.now());
                    tca.setTcalastupdatedby(1150L);
                    tcaRepo.save(tca);

                } else { // insert
                    if (tcatcrid == null || tcaprgid == null || tcabchid == null) {
                        System.out.println(">>> Skipping insert — missing critical fields");
                        continue;
                    }

                    Long nextId = tcaRepo.getMaxTcaId() + 1;
                    System.out.println(">>> Inserting new TCA id=" + nextId + " CRS=" + tcatcrid);

                    TermCourseAvailableFor newTca = new TermCourseAvailableFor();
                    newTca.setTcaid(nextId);
                    newTca.setTcatcrid(tcatcrid);
                    newTca.setTcaprgid(tcaprgid);
                    newTca.setTcabchid(tcabchid);
                    newTca.setTcaelectivetype(electiveType != null ? electiveType : "ICTE");
                    newTca.setTcastatus("ACTIVE");
                    newTca.setTcacreatedby(3809L);
                    newTca.setTcacreatedat(LocalDateTime.now());
                    newTca.setTcarowstate(1L);
                    tcaRepo.save(newTca);
                }
            } catch (Exception e) {
                System.err.println(">>> Error processing row: " + row);
                e.printStackTrace();
                throw e;
            }
        }

        System.out.println(">>> Finished saveRows successfully.");
    }

    /**
     * copyToTCA as before (unchanged) — copies termcourses into termcourseavailablefor for given prg/batch.
     */
    @Transactional
    public List<TermCourseAvailableFor> copyToTCA(Long latestTermId, List<TermCourses> termCourses, Long prgid, Long bchid) {
        System.out.println(">>> copyToTCA CALLED for term=" + latestTermId + ", program=" + prgid + ", batch=" + bchid);
        Long nextId = tcaRepo.getMaxTcaId();

        List<TermCourseAvailableFor> newList = new ArrayList<>();
        for (TermCourses tc : termCourses) {
            nextId++;
            TermCourseAvailableFor newTca = new TermCourseAvailableFor();
            newTca.setTcaid(nextId);
            newTca.setTcatcrid(tc.getTcrid());
            newTca.setTcaprgid(prgid);
            newTca.setTcabchid(bchid);
            newTca.setTcaelectivetype("ICTE");
            newTca.setTcastatus("ACTIVE");
            newTca.setTcacreatedby(3809L);
            newTca.setTcacreatedat(LocalDateTime.now());
            newTca.setTcarowstate(1L);
            tcaRepo.save(newTca);
            newList.add(newTca);
        }

        System.out.println(">>> Finished copyToTCA — inserted " + newList.size() + " records");
        return newList;
    }
}