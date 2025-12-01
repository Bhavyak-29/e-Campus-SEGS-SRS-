package com.ec2.main.controller.admin;

import com.ec2.main.model.Programs;
import com.ec2.main.model.Batches;
import com.ec2.main.model.Semesters;
import com.ec2.main.model.DropdownItem;
import com.ec2.main.model.StudentRegistration;
import com.ec2.main.repository.ProgramsRepository;
import com.ec2.main.repository.BatchesRepository;
import com.ec2.main.repository.SemestersRepository;
import com.ec2.main.repository.StudentRegistrationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/gradecard/serialno/search")
public class SerialNoSearchController {

    @Autowired private ProgramsRepository programsRepository;
    @Autowired private BatchesRepository batchesRepository;
    @Autowired private SemestersRepository semestersRepository;
    @Autowired private StudentRegistrationRepository studentRegistrationRepository;

    // 1️⃣ Selector page
    @GetMapping("/selector")
    public String showSelector(Model model,
                               @ModelAttribute("errorMessage") String errorMessage) {

        List<Programs> programs =
                programsRepository.findByPrgrowstateGreaterThanOrderByPrgfield1Asc((short) 0);
        model.addAttribute("programs", programs);

        if (errorMessage != null && !errorMessage.isBlank()) {
            model.addAttribute("errorMessage", errorMessage);
        }

        return "serialno_search_selector";
    }

    // 2️⃣ AJAX: batches by program (reuse logic; same as batchwise)
    @GetMapping("/api/batches")
    @ResponseBody
    public List<DropdownItem> getBatches(@RequestParam Long programId) {
        List<Batches> batches =
                batchesRepository.findByPrograms_PrgidAndBchrowstateGreaterThanOrderByBchfield1Asc(
                        programId, 0L);

        return batches.stream()
                .map(b -> new DropdownItem(
        String.valueOf(b.getBchid()),
        (b.getBchname() != null ? b.getBchname() : String.valueOf(b.getBchfield1()))
))
.collect(Collectors.toList());
    }

    // 3️⃣ AJAX: semesters by batch
    @GetMapping("/api/semesters")
    @ResponseBody
    public List<DropdownItem> getSemesters(@RequestParam Long batchId) {
        List<Semesters> semesters =
                semestersRepository.findByBatches_BchidAndStrrowstateGreaterThanOrderByStrseqnoAsc(
                        batchId, (short) 0);

        return semesters.stream()
                .map(s -> new DropdownItem(
                        String.valueOf(s.getStrid()),
                        s.getStrname()
                ))
                .collect(Collectors.toList());
    }

    // 4️⃣ Handle search: serialNo → redirect to grade card
    @GetMapping("/result")
    public String handleSerialSearch(@RequestParam Long semesterId,
                                     @RequestParam int serialNo,
                                     RedirectAttributes redirectAttributes) {

        if (serialNo <= 0) {
            redirectAttributes.addFlashAttribute("errorMessage", "Serial Number must be positive.");
            return "redirect:/admin/gradecard/serialno/search/selector";
        }

        List<StudentRegistration> regs =
                studentRegistrationRepository.findBySemesterOrderByStudentInstId(semesterId);

        if (regs.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "No registrations found for selected semester.");
            return "redirect:/admin/gradecard/serialno/search/selector";
        }

        if (serialNo > regs.size()) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Serial Number out of range. Max for this semester is " + regs.size() + ".");
            return "redirect:/admin/gradecard/serialno/search/selector";
        }

        StudentRegistration target = regs.get(serialNo - 1); // 1-based → 0-based
        String instId = target.getStudents().getStdinstid();

        // 🔁 redirect to existing GradeCardController endpoint
        return "redirect:/results/gradecard/semester/" + instId + "?semesterId=" + semesterId;
    }
}
