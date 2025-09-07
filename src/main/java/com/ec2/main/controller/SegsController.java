package com.ec2.main.controller;

import com.ec2.main.model.Users;
import com.ec2.main.security.CustomUserDetails;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/edu/segs")
public class SegsController {

    @GetMapping("/SegsLoginServ")
    public String processDirectAccess(HttpSession session, Authentication authentication) {
        if (authentication == null) {
            return "redirect:/auth/login";
        }

        try {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            Users user = userDetails.getUser();

            String roleName = user.getRole() != null ? user.getRole().getRoleName() : null;
            String rid = user.getRole() != null ? user.getRole().getRid() : null;

            // Define allowed roles here
            List<String> allowedRoles = List.of("REGISTRAR", "FACULTY", "ADMIN");

            if (roleName == null || !allowedRoles.contains(roleName.toUpperCase())) {
                session.setAttribute("SegsAccessFailure", "YOU ARE NOT PERMITTED TO ACCESS THE APPLICATION");
                return "redirect:/edu/segs/SegsAccessDenied";
            }

            // Set session attributes for rendering and state
            session.setAttribute("Navigation_Mode", "Term");
            session.setAttribute("ATTRIBUTESEGSSHOWREEXAMS", "FALSE");
            session.setAttribute("userId", user.getUid());
            session.setAttribute("username", user.getUname());
            session.setAttribute("roleId", rid); // Used in frontend for menu rendering

            return "redirect:/edu/segs/SegsMenu";

        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/edu/segs/SegsError";
        }
    }



    @GetMapping("/SegsAccessDenied")
    public String accessDenied(Model model, HttpSession session) {
        String errorMessage = (String) session.getAttribute("SegsAccessFailure");
        model.addAttribute("errorMessage", errorMessage != null ? errorMessage : "Access Denied");
        return "SegsAccessDenied";
    }

    @GetMapping("/SegsMenu")
    public String segsMenu(HttpSession session, Model model) {
        String roleId = (String) session.getAttribute("roleId");
        String username = (String) session.getAttribute("username");

        if (roleId == null || username == null) {
            return "redirect:/edu/segs/SegsError";
        }

        model.addAttribute("username", username);
        model.addAttribute("roleId", roleId);

        return "SegsMenu";
    }

    @GetMapping("/SegsError")
    public String segsError() {
        return "SegsError";
    }

    @GetMapping("/SegsNotAllowedUser")
    public String segsNotAllowedUser() {
        return "SegsNotAllowedUser";
    }

    @GetMapping("/SegsUndefinedUser")
    public String segsUndefinedUser() {
        return "SegsUndefinedUser";
    }
}
