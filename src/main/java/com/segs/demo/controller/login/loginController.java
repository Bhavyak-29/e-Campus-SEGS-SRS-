package com.segs.demo.controller.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import com.segs.demo.model.User;
import com.segs.demo.service.loginservice;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;


@Controller
public class loginController {
    @Autowired
    private loginservice loginService;

    @GetMapping("/login")
    public String login(HttpServletRequest request, HttpSession session, ModelMap model) {
         session.setAttribute("userId", 7); // Simulated input
        Integer userId = (Integer) session.getAttribute("userId");

        if (userId == null) {
            return "error";
        }

        User user = loginService.fetchUserDetails(userId);

        if (user == null) {
            session.setAttribute("SegsUserCategory", "INVALIDUSER");
            session.setAttribute("SegsAccessFailure", "YOU ARE NOT PERMITTED TO ACCESS THE APPLICATION");
            return "accessDenied";
        }

        String userCategory = user.getUserCategory();
        String userIp = "";

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

        return "segsMenuFaculty";
    }

}
