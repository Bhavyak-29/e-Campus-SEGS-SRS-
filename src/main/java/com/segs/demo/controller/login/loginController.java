package com.segs.demo.controller.login;

import com.segs.demo.model.Users;
import com.segs.demo.service.loginservice;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class loginController {

    @Autowired
    private loginservice loginService;

    // This returns the login form at /login (GET)
    @GetMapping("/login")
    public String loginPage() {
        return "login"; // Make sure login.html is inside /templates
    }

    // This is the post-login landing page
    @RequestMapping("/")
    public String postLoginRedirect(HttpServletRequest request, HttpSession session, Authentication authentication, ModelMap model) {
        // Get the authenticated user's username
        String username = null;
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            username = ((UserDetails) authentication.getPrincipal()).getUsername();
        }

        if (username == null) {
            return "error";
        }

        // You can fetch userId using username (e.g. from DB)
        // For demo, simulate userId = 7
        Integer userId = 7;
        session.setAttribute("userId", userId);

        Users user = loginService.fetchUserDetails(userId);
        if (user == null) {
            session.setAttribute("SegsUserCategory", "INVALIDUSER");
            session.setAttribute("SegsAccessFailure", "YOU ARE NOT PERMITTED TO ACCESS THE APPLICATION");
            return "accessDenied";
        }

        String userCategory = user.getUserCategory();
        String userIp = request.getRemoteAddr(); // Get actual client IP

        if (!loginService.isAllowedUser(userCategory, userId)) {
            session.setAttribute("SegsUserCategory", "INVALIDUSER");
            session.setAttribute("SegsAccessFailure", "YOU ARE NOT PERMITTED TO ACCESS THE APPLICATION");
            return "accessDenied";
        }

        if (!loginService.isPermittedMachine(userId, userIp)) {
            session.setAttribute("SegsUserCategory", "INVALIDIP");
            session.setAttribute("SegsAccessFailure", "YOU ARE NOT PERMITTED TO ACCESS THE APPLICATION FROM THIS MACHINE");
            return "accessDenied";
        }

        session.setAttribute("SegsUserCategory", userCategory);
        session.setAttribute("Navigation_Mode", "Term");
        session.setAttribute("ATTRIBUTESEGSSHOWREEXAMS", "FALSE");

        return "segsMenuFaculty"; // Your home page
    }
}
