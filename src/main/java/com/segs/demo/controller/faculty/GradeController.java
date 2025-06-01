package com.segs.demo.controller.faculty;

import com.segs.demo.model.GradeUploadForm;
import com.segs.demo.service.GradeService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/grades")
public class GradeController {

    @Autowired
    private GradeService gradeService;

    @GetMapping("/upload")
    public String showUploadForm(ModelMap model) {
        model.addAttribute("gradeForm", new GradeUploadForm());
        return "upload-grades";
    }

    @PostMapping("/upload")
    public String uploadGrades(@ModelAttribute("gradeForm") @Valid GradeUploadForm form,
                               BindingResult result,
                               HttpSession session,
                               ModelMap model) {

        if (result.hasErrors()) {
            model.addAttribute("error", "Please fill all required fields correctly.");
            return "upload-grades";
        }

        String responseMessage = gradeService.uploadGrades(form, session);
        model.addAttribute("message", responseMessage);
        return "upload-grades";
    }
}
