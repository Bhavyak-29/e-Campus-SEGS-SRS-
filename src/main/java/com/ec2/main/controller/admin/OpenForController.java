// package com.ec2.main.controller.admin;
// import com.ec2.main.model.Batches;
// import com.ec2.main.model.TermCourseAvailableFor;
// import com.ec2.main.model.TermCourses;
// import com.ec2.main.repository.BatchesRepository;
// import com.ec2.main.repository.ProgramsRepository;
// import com.ec2.main.repository.TermCourseAvailableForRepository;
// import com.ec2.main.repository.TermCoursesRepository;
// import com.ec2.main.service.OpenForService;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.ResponseEntity;
// import org.springframework.stereotype.Controller;
// import org.springframework.ui.Model;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RequestParam;
// import org.springframework.web.bind.annotation.ResponseBody;
// import java.util.List;
// import java.util.Map;
// import jakarta.servlet.http.HttpSession;

// @Controller
// @RequestMapping("/admin/openFor")
// public class OpenForController {

//     @Autowired
//     private ProgramsRepository programsRepo;

//     @Autowired
//     private BatchesRepository batchesRepo;

//     @Autowired
//     private TermCoursesRepository termCoursesRepo;

//     @Autowired
//     private TermCourseAvailableForRepository tcaRepo;

//     @Autowired
//     private OpenForService openForService;

//     @GetMapping
//     public String openForPage(Model model) {
//         model.addAttribute("programs", programsRepo.findAll());
//         return "openFor"; // Thymeleaf view
//     }

//     @GetMapping("/load")
//     @ResponseBody
//     public List<TermCourseAvailableFor> loadOpenFor(@RequestParam Long prgid,
//                                                     @RequestParam Long bchid,
//                                                     HttpSession session) {
//         Long latestTermId = (Long) session.getAttribute("latesttrmid");
//         if (latestTermId == null) latestTermId = 59L;

//         List<TermCourseAvailableFor> list = tcaRepo.findByTermProgramBatch(latestTermId, prgid, bchid);
//         if (list.isEmpty()) {
//             List<TermCourses> termCourses = termCoursesRepo.findByTcrtrmidOrderByTcrid(latestTermId);
//             list = openForService.copyToTCA(latestTermId, termCourses, prgid, bchid);
//         }
//         return list;
//     }

//     @PostMapping("/save")
//     @ResponseBody
//     public ResponseEntity<String> saveOpenFor(@RequestBody List<Map<String, String>> rows, HttpSession session) {
//         Long latestTermId = (Long) session.getAttribute("latesttrmid");
//         if (latestTermId == null) latestTermId = 59L;
//         try {
//             openForService.saveRows(latestTermId, rows);
//             return ResponseEntity.ok("success");
//         } catch (Exception e) {
//             e.printStackTrace();
//             return ResponseEntity.status(500).body("error");
//         }
//     }

//     @GetMapping("/byProgram")
//     @ResponseBody
//     public List<Batches> getBatchesByProgram(@RequestParam Long prgid) {
//         return batchesRepo.findByPrgId(prgid);
//     }
// }

package com.ec2.main.controller.admin;

import com.ec2.main.model.Batches;
import com.ec2.main.model.TermCourseAvailableFor;
import com.ec2.main.repository.BatchesRepository;
import com.ec2.main.repository.ProgramsRepository;
import com.ec2.main.repository.TermCourseAvailableForRepository;
import com.ec2.main.repository.TermCoursesRepository;
import com.ec2.main.service.OpenForService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/openFor")
public class OpenForController {

    @Autowired
    private ProgramsRepository programsRepo;

    @Autowired
    private BatchesRepository batchesRepo;

    @Autowired
    private TermCoursesRepository termCoursesRepo;

    @Autowired
    private TermCourseAvailableForRepository tcaRepo;

    @Autowired
    private OpenForService openForService;

    @GetMapping
    public String openForPage(Model model) {
        model.addAttribute("programs", programsRepo.findAll());
        return "openFor"; // Thymeleaf view
    }

    /**
     * Loads rows for given program+batch for latest term (enriched view).
     * If no TCA rows exist for that combination, copies from termcourses first.
     */
    @GetMapping("/load")
    @ResponseBody
    public List<Map<String, Object>> loadOpenFor(@RequestParam Long prgid,
                                                 @RequestParam Long bchid,
                                                 HttpSession session) {
        Long latestTermId = (Long) session.getAttribute("latesttrmid");
        if (latestTermId == null) latestTermId = 59L;

        // service will ensure copy-to-TCA if needed and return enriched data
        return openForService.loadFor(latestTermId, prgid, bchid);
    }

    @PostMapping("/save")
    @ResponseBody
    public ResponseEntity<String> saveOpenFor(@RequestBody List<Map<String, String>> rows, HttpSession session) {
        Long latestTermId = (Long) session.getAttribute("latesttrmid");
        if (latestTermId == null) latestTermId = 59L;
        try {
            openForService.saveRows(latestTermId, rows);
            return ResponseEntity.ok("success");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("error");
        }
    }

    @GetMapping("/byProgram")
    @ResponseBody
    public List<Batches> getBatchesByProgram(@RequestParam Long prgid) {
        return batchesRepo.findByPrgId(prgid);
    }
}

