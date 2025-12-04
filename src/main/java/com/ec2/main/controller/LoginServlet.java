package com.ec2.main.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ec2.main.model.Users;
import com.ec2.main.repository.UserRepository;
import com.ec2.main.security.CustomUserDetails;
import com.ec2.main.service.PasswordSetupService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/auth")
public class LoginServlet {

    @Autowired
    private PasswordSetupService passwordSetupService;

    @Autowired
    private UserRepository userRepository;


    @GetMapping("/showIndexPage")
    public String showIndexPage() {
        return "index1"; // shows form for entering univid
    }

    @GetMapping("/check")
    public String showSetupPage(@RequestParam("univid") String univid, RedirectAttributes redirectAttributes) {
        System.out.println("Inside /auth/check with univid: " + univid);
        Users user = userRepository.findByUnivId(univid).orElse(null);

        if (user == null) {
            System.out.println("User not found");
            redirectAttributes.addFlashAttribute("error", "User not found.");
            return "redirect:/auth/login";
        }

        System.out.println("User found: " + user.getUnivId() + ", Password: " + user.getPassword());

        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            System.out.println("Password not set. Redirecting to /initiate-password-setup");
            return "redirect:/auth/initiate-password-setup?univid=" + univid;
        }

        System.out.println("Password already set. Redirecting to login.");
        return "redirect:/auth/login";
    }




    @GetMapping("/login")
    public String loginPage(
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "logout", required = false) String logout,
            Model model, HttpSession session) {

        if (error != null) {
            model.addAttribute("error", "Invalid university ID or password.");
        }
        // if(session!=null) {
        //     session.invalidate();
        // }
        if (logout != null) {
            session.invalidate();
            model.addAttribute("message", "You have been logged out successfully.");
        }

        return "index";
    }

    @GetMapping("/student-homepage")
    public String studentDashboard(Authentication authentication, HttpSession session, Model model) {
        if (authentication != null) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            String userName = userDetails.getUser().getUname();
            String roleId = userDetails.getUser().getRole().getRid();

            session.setAttribute("username", userName);
            model.addAttribute("username", userName);
            model.addAttribute("roleId", roleId);
            session.setAttribute("studentId", 6179L);

            return "redirect:/srs";
        }
        return "redirect:/auth/login";
    }


    // @GetMapping("/homepage")
    // public String dashboard(Authentication authentication, HttpSession session, Model model) {
    //     if (authentication != null) {
    //         CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
    //         Users user = userDetails.getUser();
    //         Long uid = userDetails.getUser().getUid();
    //         String userName = user.getUname();
    //         String roleName = user.getRole() != null ? user.getRole().getRoleName() : null;
    //         String roleId = user.getRole() != null ? user.getRole().getRid() : null;

    //         // Store in session and model
    //         session.setAttribute("username", userName);
    //         session.setAttribute("univId", userName);
    //         session.setAttribute("userid",uid);
    //         session.setAttribute("roleId", roleId);
    //         model.addAttribute("username", userName);
    //         model.addAttribute("roleId", roleId);
    //         model.addAttribute("userid",uid);

    //         if ("STUDENT".equalsIgnoreCase(roleName)) {
    //             return "redirect:/srs";
    //         } else if("FACULTY".equalsIgnoreCase(roleName)){
    //             return "redirect:/faculty/current-courses";
    //         }else {
    //             return "segsMenuFaculty";
    //         }
    //     }
    //     return "redirect:/auth/login";
    // }
   @GetMapping("/homepage")
public String dashboard(Authentication authentication, HttpSession session, Model model) {
    if (authentication != null) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Users user = userDetails.getUser();
        Long uid      = user.getUid();
        String uname  = user.getUname();   // login / display name
        String roleName = user.getRole() != null ? user.getRole().getRoleName() : null;
        String roleId   = user.getRole() != null ? user.getRole().getRid() : null;

        // keep existing behaviour for admins etc.
        session.setAttribute("username", uname);


        session.setAttribute("userid", uid);
        session.setAttribute("roleId", roleId);
        model.addAttribute("username", uname);
        model.addAttribute("roleId", roleId);
        model.addAttribute("userid", uid);

        if ("STUDENT".equalsIgnoreCase(roleName)) {
            return "redirect:/srs";
        } else if ("FACULTY".equalsIgnoreCase(roleName)) {
            return "redirect:/faculty/current-courses";
        } else {
            return "segsMenuFaculty";
        }
    }
    return "redirect:/auth/login";
}


    @GetMapping("/wrong-password")
    public String wrongPassword(@RequestParam(required = false) String remarks, Model model) {
        if (remarks != null) {
            model.addAttribute("remarks", remarks);
        }
        return "WrongPassword";
    }

    @ExceptionHandler(AccessDeniedException.class)
    public String handleAccessDenied() {
        return "redirect:/auth/login?error=unauthorized";
    }
}
