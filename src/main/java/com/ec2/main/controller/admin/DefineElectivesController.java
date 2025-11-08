// package com.ec2.main.controller.admin;
// import com.ec2.main.model.Programs;
// import com.ec2.main.model.Terms;
// import com.ec2.main.repository.ProgramsRepository;
// import com.ec2.main.repository.TermsRepository;
// import com.ec2.main.model.CourseGroups;
// import com.ec2.main.model.Courses;
// import com.ec2.main.repository.CourseGroupsRepository;
// import com.ec2.main.repository.CoursesRepository;
// import jakarta.servlet.http.HttpSession;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.ResponseEntity;
// import org.springframework.stereotype.Controller;
// import org.springframework.web.bind.annotation.*;
// import org.springframework.ui.Model;
// import java.util.List;
// import java.util.Map;

// @Controller
// @RequestMapping("/admin/electives")
// public class ManageElectiveController {
    
//     @Autowired
//     private TermsRepository termsRepository;

//     @Autowired
//     private ProgramsRepository programsRepository;

//     @Autowired
//     private CourseGroupsRepository courseGroupsRepository;

//     @Autowired
//     private CoursesRepository coursesRepository;

//     @GetMapping("/select")
//     public String showSelectForm(Model model, HttpSession session) {
//         Long latestTrmId = (Long) session.getAttribute("latestTrmId");
//         if (latestTrmId != null) {
//             Terms term = termsRepository.findById(latestTrmId).orElse(null);
//             if (term != null) {
//                 model.addAttribute("selectedTerm", term);
//                 List<Programs> programs = programsRepository.findAll();
//                 model.addAttribute("programs", programs);
//                 List<CourseGroups> electiveGroups = courseGroupsRepository.findElectiveGroups(term.getTrmid());
//                 model.addAttribute("courseGroups", electiveGroups);
//                 return "defineElectivesManage";
//             }
//         }
//         List<Programs> programs = programsRepository.findAll();
//         List<String> termNames = termsRepository.findDistinctTermNames();

//         if (termNames == null || termNames.isEmpty()) {
//             termNames = List.of("Autumn", "Winter", "Summer");
//         }

//         model.addAttribute("programs", programs);
//         model.addAttribute("termNames", termNames);
//         return "defineElectivesSelect";
//     }

//     @PostMapping("/select")
//     public String handleSelection(
//         @RequestParam("termName") String termName,
//         @RequestParam("programId") Long programId,
//         Model model,
//         HttpSession session
//     ) {
//         Long latestTrmId = (Long) session.getAttribute("latestTrmId");
//         Terms latestTerm;
//         if(latestTrmId == null){
//             latestTerm = termsRepository.findLatestTermByName(termName);
//             if (latestTerm == null) {
//                 model.addAttribute("error", "No term found for " + termName);
//                 return "defineElectivesSelect";
//             }
//             session.setAttribute("latestTrmId", latestTerm.getTrmid());
//             session.setAttribute("latestTermName", termName);
//             System.out.println("Storing latestTrmId in session: " + latestTerm.getTrmid());
//         }
//         else{
//             latestTerm = termsRepository.findById(latestTrmId).orElse(null);
//             if (latestTerm == null) {
//                 model.addAttribute("error", "Session term not found in database");
//                 return "defineElectivesSelect";
//             }
//             System.out.println("Retrieved latestTrmId from session: " + latestTrmId);
//         }
        
//         Programs program = programsRepository.findById(programId)
//             .orElse(null);
//         if (program == null) {
//             model.addAttribute("error", "Invalid program selected");
//             return "defineElectivesSelect";
//         }

//         model.addAttribute("selectedTerm", latestTerm);
//         model.addAttribute("selectedProgram", program);

//         System.out.println("Selected Term ID: " + latestTerm.getTrmid());
//         System.out.println("Selected Program ID: " + programId);
//         List<CourseGroups> electiveGroups = courseGroupsRepository.findElectiveGroups(latestTerm.getTrmid());
//         model.addAttribute("courseGroups", electiveGroups);

//         return "defineElectivesManage";
//     }

//     @GetMapping("/courses")
//     @ResponseBody
//     public List<Courses> getCoursesForGroup(
//             @RequestParam("cgpid") Long cgpid,
//             @RequestParam("prgid") Long prgid,
//             HttpSession session
//            ) {

//         Long trmid = (Long) session.getAttribute("latestTrmId");
//         if (trmid == null) {
//             throw new IllegalStateException("Session expired: Term not set");
//         }
//         System.out.println("Fetching courses for cgpid=" + cgpid + ", prgid=" + prgid + ", trmid=" + trmid);
//         return coursesRepository.getbycgpId(cgpid, prgid, trmid);
//     }

//     @PostMapping("/update")
//     @ResponseBody
//     public ResponseEntity<String> updateElectives(@RequestBody Map<String, Object> updateData) {
//         // Placeholder for now: You can handle saving logic later
//         // updateData may include: { cgpid, prgid, trmid, updatedCourses: [...] }
//         return ResponseEntity.ok("Electives updated successfully");
//     }

//     @PostMapping("/reset-term")
//     public String resetTermSession(HttpSession session) {
//         session.removeAttribute("latestTrmId");
//         session.removeAttribute("latestTermName");
//         return "redirect:/admin/electives/select";
//     }
// }


// package com.ec2.main.controller.admin;

// import com.ec2.main.model.Courses;
// import com.ec2.main.model.TermCourses;
// import com.ec2.main.model.Users;
// import com.ec2.main.model.Terms;
// import com.ec2.main.repository.CoursesRepository;
// import com.ec2.main.repository.TermCoursesRepository;
// import com.ec2.main.repository.TermsRepository;
// import com.ec2.main.repository.UsersRepository;
// import com.ec2.main.service.TermCoursesService;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.http.ResponseEntity;
// import org.springframework.stereotype.Controller;
// import org.springframework.ui.Model;
// import org.springframework.web.bind.annotation.*;
// import jakarta.servlet.http.HttpSession;

// import java.util.*;

// @Controller
// @RequestMapping("/admin/defineElectives")
// public class DefineElectivesController {

//     @Autowired
//     private TermsRepository termsRepository;

//     @Autowired
//     private TermCoursesRepository termCoursesRepository;

//     @Autowired
//     private CoursesRepository coursesRepository;

//     @Autowired
//     private UsersRepository usersRepository;

//     @Autowired
//     private TermCoursesService defineElectivesService;

//     @Value("${ACTIVE_TERM_NAME:Autumn}")
//     private String activeTermName;

//     @GetMapping
//     public String showDefineElectives(Model model, HttpSession session) {
//         session.setAttribute("latesttrmid", 59L);
//         Terms previousTerm = termsRepository.findLatestTermByName(activeTermName);
//         if (previousTerm == null) {
//             model.addAttribute("error", "No previous term found for " + activeTermName);
//             return "errorPage";
//         }

//         List<TermCourses> prevCourses = termCoursesRepository.findByTermWithCourseDetails(previousTerm.getTrmid());

//         model.addAttribute("termName", activeTermName);
//         model.addAttribute("courses", prevCourses);
//         // model.addAttribute("termYear", previousTerm.getTrmyear());
//         return "defineElectivesManage";
//     }

//     @GetMapping("/faculty/search")
//     @ResponseBody
//     public List<Map<String, Object>> searchFaculty(@RequestParam("q") String query) {
//         List<Users> facultyList = usersRepository.searchFacultyByName(query);
//         List<Map<String, Object>> results = new ArrayList<>();
//         for (Users u : facultyList) {
//             Map<String, Object> map = new HashMap<>();
//             map.put("uid", u.getUid());
//             map.put("name", u.getUemail().replace('_', ' '));
//             results.add(map);
//         }
//         return results;
//     }

//     // @PostMapping("/save")
//     // @ResponseBody
//     // public ResponseEntity<String> saveElectives(@RequestBody List<Map<String, Object>> electives, HttpSession session) {
//     //     System.out.println("Received electives: " + electives);
//     //     // TODO: implement DB save logic later
//     //     return ResponseEntity.ok("Saved successfully");
//     // }

//     @PostMapping("/save")
//     @ResponseBody
//     public String saveElectives(@RequestBody List<Map<String, Object>> electives,
//                                 HttpSession session) {
//         try {
//             Long termId = (Long) session.getAttribute("latesttrmid");
//             if (termId == null) {
//                 return "error: termId missing";
//             }

//             defineElectivesService.saveSelectedCourses(termId, electives);
//             return "success";
//         } catch (Exception e) {
//             e.printStackTrace();
//             return "error";
//         }
//     }
// }

package com.ec2.main.controller.admin;

import com.ec2.main.model.Courses;
import com.ec2.main.model.TermCourses;
import com.ec2.main.model.Users;
import com.ec2.main.service.DefineElectivesService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;

// @Controller
// @RequestMapping("/admin/defineElectives")
// public class DefineElectivesController {

//     private final DefineElectivesService defineElectivesService;

//     public DefineElectivesController(DefineElectivesService defineElectivesService) {
//         this.defineElectivesService = defineElectivesService;
//     }

//     @GetMapping
//     public String showDefineElectivesPage(HttpSession session, Model model) {
//         Long latestTermId = (Long) session.getAttribute("latesttrmid");
//         if (latestTermId == null){
//             latestTermId = 59L; // default to 59 if not in session
//             session.setAttribute("latesttrmid", latestTermId);
//         }
//         Long prevTermId = latestTermId - 3; // example; adjust as per DB (e.g., 56→59)
//         System.out.println("Latest Term ID: " + latestTermId + ", Previous Term ID: " + prevTermId);

//         defineElectivesService.copyPreviousTermCoursesIfNeeded(prevTermId, latestTermId, session);
//         List<TermCourses> termCourses = defineElectivesService.getTermCoursesForTerm(latestTermId);

//         model.addAttribute("termCourses", termCourses);
//         model.addAttribute("latestTermId", latestTermId);
//         return "defineElectivesManage";
//     }

//     @GetMapping("/searchCourses")
//     @ResponseBody
//     public List<Courses> searchCourses(@RequestParam("q") String query) {
//         return defineElectivesService.searchCourses(query);
//     }

//     @GetMapping("/searchFaculty")
//     @ResponseBody
//     public List<Users> searchFaculty(@RequestParam("q") String query) {
//         return defineElectivesService.searchFaculty(query);
//     }

//     @PostMapping("/save")
//     @ResponseBody
//     public String saveCourses(@RequestBody List<Map<String, String>> rows, HttpSession session) {
//         Long latestTermId = (Long) session.getAttribute("latesttrmid");
//         if (latestTermId == null) latestTermId = 59L;
//         defineElectivesService.saveSelectedCourses(latestTermId, rows);
//         return "redirect:/admin/defineElectives";
//     }
// }

@Controller
@RequestMapping("/admin/defineElectives")
public class DefineElectivesController {

    private final DefineElectivesService defineElectivesService;

    public DefineElectivesController(DefineElectivesService defineElectivesService) {
        this.defineElectivesService = defineElectivesService;
    }

    @GetMapping
    public String showDefineElectivesPage(HttpSession session, Model model) {
        Long latestTermId = (Long) session.getAttribute("latesttrmid");
        if (latestTermId == null){
            latestTermId = 59L;
            session.setAttribute("latesttrmid", latestTermId);
        }
        Long prevTermId = latestTermId - 3; // adjust if needed
        System.out.println("Latest Term ID: " + latestTermId + ", Previous Term ID: " + prevTermId);

        defineElectivesService.copyPreviousTermCoursesIfNeeded(prevTermId, latestTermId, session);
        List<TermCourses> termCourses = defineElectivesService.getTermCoursesForTerm(latestTermId);

        model.addAttribute("termCourses", termCourses);
        model.addAttribute("latestTermId", latestTermId);
        return "defineElectivesManage";
    }

    @GetMapping("/searchCourses")
    @ResponseBody
    public List<Map<String, Object>> searchCourses(@RequestParam("q") String query) {
        List<Courses> list = defineElectivesService.searchCourses(query);
        List<Map<String, Object>> out = new ArrayList<>();
        for (Courses c : list) {
            Map<String,Object> m = new HashMap<>();
            m.put("crsid", c.getCrsid());
            m.put("crscode", c.getCrscode());
            m.put("crsname", c.getCrsname() != null ? c.getCrsname() : c.getCrstitle());
            m.put("crscreditpoints", c.getCrscreditpoints());
            out.add(m);
        }
        return out;
    }

    @GetMapping("/searchFaculty")
    @ResponseBody
    public List<Map<String, Object>> searchFaculty(@RequestParam("q") String query) {
        // returns simple list of {uid, name} where name is uemail cleaned up for display
        List<Users> facultyList = defineElectivesService.searchFacultyRaw(query); // we'll expose this in service
        List<Map<String, Object>> results = new ArrayList<>();
        for (Users u : facultyList) {
            Map<String, Object> map = new HashMap<>();
            map.put("uid", u.getUid());
            // uemail in DB contains name without domain; replace underscores with space for display
            String display = u.getUemail() == null ? (u.getUfullname()!=null?u.getUfullname():u.getUname()) :
                             u.getUemail().replace('_',' ');
            map.put("name", display);
            map.put("rawEmail", u.getUemail());
            results.add(map);
        }
        return results;
    }

    @PostMapping("/save")
    @ResponseBody
    public ResponseEntity<String> saveCourses(@RequestBody List<Map<String, String>> rows, HttpSession session) {
        System.out.println("ENTERED saveCourses CONTROLLER, rows=" + rows.size());
        Long latestTermId = (Long) session.getAttribute("latesttrmid");
        if (latestTermId == null) latestTermId = 59L;
        try {
            defineElectivesService.saveSelectedCourses(latestTermId, rows);
            return ResponseEntity.ok("success");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("error");
        }
    }
}


