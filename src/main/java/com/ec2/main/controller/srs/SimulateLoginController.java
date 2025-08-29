package com.ec2.main.controller.srs;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;

@Controller
public class SimulateLoginController {

    @GetMapping("/loginsrs")
    public String login(HttpSession session) {
        session.setAttribute("studentId", 6179L);
        return "redirect:/srs"; 
    }
}