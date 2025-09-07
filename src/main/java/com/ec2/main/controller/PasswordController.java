package com.ec2.main.controller;

import com.ec2.main.model.Users;
import com.ec2.main.repository.UserRepository;
import com.ec2.main.security.CustomUserDetails;
import com.ec2.main.service.PasswordSetupService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/auth")
public class PasswordController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordSetupService passwordSetupService;

    @GetMapping("/initiate-password-setup")
    public String initiatePasswordSetup(@RequestParam String univid, RedirectAttributes redirectAttributes) {

        System.out.println("Redirected to /initiate-password-setup for: " + univid);

        Users user = userRepository.findByUnivId(univid).orElse(null);

        if (user == null) {
            redirectAttributes.addFlashAttribute("error", "User not found.");
            return "redirect:/auth/check";
        }

        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Password already set. Please login.");
            return "redirect:/auth/login";
        }

        passwordSetupService.createAndSendOtp(user); // generates + stores with expiry + email
        redirectAttributes.addFlashAttribute("message", "OTP sent to your registered email.");
        return "redirect:/auth/univid?univid=" + univid;
    }

    @GetMapping("/univid")
    public String showOtpAndPasswordForm(@RequestParam String univid, Model model) {
        model.addAttribute("univid", univid);
        return "otpRequest";
    }

    @PostMapping("/verify-otp")
    public String verifyOtpAndSetPassword(@RequestParam String univid,
                                          @RequestParam String otp,
                                          @RequestParam String newPassword,
                                          Model model,
                                          HttpSession session) {
        if (!passwordSetupService.verifyOtp(univid, otp)) {
            model.addAttribute("univid", univid);
            model.addAttribute("error", "Invalid OTP. Please try again.");
            return "otpRequest";
        }

        boolean updated = passwordSetupService.updatePassword(univid, newPassword);

        if (updated) {
            session.invalidate();
            model.addAttribute("message", "Password set successfully. Please login.");
            return "redirect:/auth/login";
        } else {
            model.addAttribute("univid", univid);
            model.addAttribute("error", "Failed to update password. Contact support.");
            return "otpRequest";
        }
    }

    @GetMapping("/request-otp")
    public String resendOtp(@RequestParam String univid, RedirectAttributes redirectAttributes) {
        Users user = userRepository.findByUnivId(univid).orElse(null);

        if (user == null) {
            redirectAttributes.addFlashAttribute("error", "User not found.");
            return "redirect:/auth/check";
        }

        passwordSetupService.createAndSendOtp(user);
        redirectAttributes.addFlashAttribute("message", "A new OTP has been sent to your email.");
        return "redirect:/auth/univid?univid=" + univid;
    }

    @GetMapping("/forgot-password")
    public String showForgotPasswordForm() {
        return "forgotPassword"; // form that asks for univid
    }

    @PostMapping("/forgot-password")
    public String handleForgotPassword(@RequestParam String univid, RedirectAttributes redirectAttributes) {
        Users user = userRepository.findByUnivId(univid).orElse(null);

        if (user == null) {
            redirectAttributes.addFlashAttribute("error", "User not found.");
            return "redirect:/auth/forgot-password";
        }

        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "You haven't set your password yet. Use 'Set Password' instead.");
            return "redirect:/auth/check";
        }

        passwordSetupService.createAndSendOtp(user);
        redirectAttributes.addFlashAttribute("message", "An OTP has been sent to your email.");
        return "redirect:/auth/univid?univid=" + univid;
    }

    @GetMapping("/change-password")
    public String showChangePasswordPage(Authentication authentication, Model model) {
        if (authentication == null || !authentication.isAuthenticated()) {
            model.addAttribute("error", "You must be logged in to change your password.");
            return "redirect:/auth/login";
        }

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Users user = userDetails.getUser();

        model.addAttribute("username", user.getUname());
        model.addAttribute("roleId", user.getRole().getRid());

        return "change-password";
    }

    @PostMapping("/change-password")
    public String changePassword(@RequestParam String oldpassword,
                                 @RequestParam String newpassword,
                                 @RequestParam String reenterpassword,
                                 Authentication authentication,
                                 Model model) {
        if (authentication == null || !authentication.isAuthenticated()) {
            model.addAttribute("error", "User session expired. Please login again.");
            return "redirect:/auth/login";
        }

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Users user = userDetails.getUser();
        String univid = user.getUnivId();

        model.addAttribute("username", user.getUname());
        model.addAttribute("roleId", user.getRole().getRid());

        if (!newpassword.equals(reenterpassword)) {
            model.addAttribute("error", "New password and reentered password do not match.");
            return "change-password";
        }

        if (!passwordSetupService.passwordMatches(oldpassword, user.getPassword())) {
            model.addAttribute("error", "Old password is incorrect.");
            return "change-password";
        }

        boolean updated = passwordSetupService.updatePassword(univid, newpassword);
        if (updated) {
            model.addAttribute("success", "Password changed successfully.");
        } else {
            model.addAttribute("error", "Failed to change password. Contact support.");
        }

        return "change-password";
    }




}
