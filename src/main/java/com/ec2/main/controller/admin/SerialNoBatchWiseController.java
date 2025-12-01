package com.ec2.main.controller.admin;

import com.ec2.main.model.Batches;
import com.ec2.main.model.Semesters;
import com.ec2.main.model.Programs;
import com.ec2.main.model.DropdownItem;
import com.ec2.main.model.StudentRegistration;
import com.ec2.main.repository.BatchesRepository;
import com.ec2.main.repository.ProgramsRepository;
import com.ec2.main.repository.SemestersRepository;
import com.ec2.main.repository.StudentRegistrationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/gradecard/serialno/batchwise")
public class SerialNoBatchWiseController {

    @Autowired private ProgramsRepository programsRepository;
    @Autowired private BatchesRepository batchesRepository;
    @Autowired private SemestersRepository semestersRepository;
    @Autowired private StudentRegistrationRepository studentRegistrationRepository;

    // 1️⃣ Selector page
    @GetMapping("/selector")
    public String showSelector(Model model) {
        List<Programs> programs =
                programsRepository.findByPrgrowstateGreaterThanOrderByPrgfield1Asc((short) 0);
        model.addAttribute("programs", programs);
        return "serialno_batchwise_selector";
    }

    // 2️⃣ AJAX: batches by program (returns DropdownItem)
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

    // 3️⃣ AJAX: semesters by batch (returns DropdownItem)
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

    // 4️⃣ List page
    @GetMapping("/list")
    public String showBatchSerialList(@RequestParam Long semesterId, Model model) {

        List<StudentRegistration> registrations =
                studentRegistrationRepository.findAll().stream()
                        .filter(r -> r.getSemester().getStrid().equals(semesterId))
                        .filter(r -> r.getSrgrowstate() > 0)
                        .sorted(Comparator.comparing(
                                r -> r.getStudents().getStdinstid(), String::compareToIgnoreCase))
                        .collect(Collectors.toList());

        model.addAttribute("registrations", registrations);
        model.addAttribute("semesterId", semesterId);
        return "serialno_batchwise_list";
    }
}
