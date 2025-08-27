package com.ec2.main.controller.login;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class loginController {

    private static final Map<String, Integer> userIdMap = new HashMap<>();
    static {
        userIdMap.put("admin", 2026);
        userIdMap.put("faculty1", 687);
        userIdMap.put("faculty2", 659);
        userIdMap.put("faculty3", 688);
        userIdMap.put("faculty4", 675);
        userIdMap.put("faculty5", 1147);
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login"; 
    }

    @RequestMapping("/segs/faculty")
    public String postLoginRedirect(HttpServletRequest request, HttpSession session, Authentication authentication, ModelMap model) {
        if (authentication != null) {
            String username = authentication.getName();

            Integer userid = userIdMap.getOrDefault(username, -1);
            session.setAttribute("username",username);
            session.setAttribute("userid", userid);
        }
        return "segsMenuFaculty"; 
    }
}
