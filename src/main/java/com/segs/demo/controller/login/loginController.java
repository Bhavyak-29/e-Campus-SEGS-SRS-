package com.segs.demo.controller.login;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class loginController {

    // @Autowired
    // private loginservice loginService;

    // This returns the login form at /login (GET)
    @GetMapping("/login")
    public String loginPage() {
        return "login"; // Make sure login.html is inside /templates
    }

    // This is the post-login landing page
    @RequestMapping("/")
    public String postLoginRedirect(HttpServletRequest request, HttpSession session, Authentication authentication, ModelMap model) {
        session.setAttribute("userid", 1001);
        // Get the authenticated user's username
        return "segsMenuFaculty"; // Your home page
    }
}
