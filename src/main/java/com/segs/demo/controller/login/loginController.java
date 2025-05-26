package com.segs.demo.controller.login;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;


@Controller
public class loginController {
    @GetMapping("/login")
    public String login(HttpServletRequest request, HttpSession session, ModelMap model) {
        // Simulate DB user ID from session
        session.setAttribute("userId", 7);
        Integer userId = (Integer) session.getAttribute("userId");

        if (userId == null) {
            // Redirect to error page if session is missing
            return "error";
        }

        // Simulated DB call to get user
        User user = fetchUserDetails(userId);

        if (user == null) {
            session.setAttribute("SegsUserCategory", "INVALIDUSER");
            session.setAttribute("SegsAccessFailure", "YOU ARE NOT PERMITTED TO ACCESS THE APPLICATION");
            return "accessDenied";
        }

        String userCategory = user.getUserCategory();
        String userIp = request.getRemoteAddr();

        // Access control
        if (!isAllowedUser(userCategory, userId)) {
            session.setAttribute("SegsUserCategory", "INVALIDUSER");
            session.setAttribute("SegsAccessFailure", "YOU ARE NOT PERMITTED TO ACCESS THE APPLICATION");
            return "accessDenied";
        }

        // IP Check simulation
        if (!isPermittedMachine(userId, userIp)) {
            session.setAttribute("SegsUserCategory", "INVALIDIP");
            session.setAttribute("SegsAccessFailure", "YOU ARE NOT PERMITTED TO ACCESS THE APPLICATION FROM THIS MACHINE");
            return "accessDenied";
        }

        // All good – proceed to dashboard/menu
        session.setAttribute("SegsUserCategory", userCategory);
        session.setAttribute("Navigation_Mode", "Term");
        session.setAttribute("ATTRIBUTESEGSSHOWREEXAMS", "FALSE");

        return "segsMenuFaculty";
    }

    // Simulate fetching user
    private User fetchUserDetails(int userId) {
        // Replace with real DB/service call
        if (userId == 7) {
            return new User("Bhavya", "bhavya@demo.com", "FACULTY", 7);
        } else if (userId == 2025) {
            return new User("Admin", "admin@demo.com", "REGISTRAR", 2025);
        } else {
            return null;
        }
    }

    // Simulate allowed users list
    private boolean isAllowedUser(String userCategory, int userId) {
        return userCategory.equals("REGISTRAR") || userCategory.equals("FACULTY")
                || userId == 3365 || userId == 1126 || userId == 6326
                || userId == 2025 || userId == 3809 || userId == 2484 || userId == 11045;
    }

    // Simulate IP permission check
    private boolean isPermittedMachine(int userId, String ip) {
        // Allow only 127.0.0.1 for testing
        return true;
    }
}
