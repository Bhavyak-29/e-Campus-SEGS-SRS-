package com.ec2.main.security;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        // Check if password is null or empty
        if (userDetails.getPassword() == null || userDetails.getPassword().isEmpty()) {
            // Redirect to OTP request page to start password setup flow
            response.sendRedirect("/initiate-password-setup?univid=" + userDetails.getUsername());
            return;
        }

        // Normal role based redirection
        String roleName = null;
        if (userDetails.getUser().getRole() != null) {
            roleName = userDetails.getUser().getRole().getRoleName();
        }
        // System.out.println(userDetails.getUser().getRole());
        // System.out.println(roleName);
        String role = roleName != null ? "ROLE_" + roleName.toUpperCase() : "";
        // System.out.println(role);
        // System.out.println("----------------------------------------");
        if ("ROLE_STUDENT".equals(role)) {
            response.sendRedirect("/auth/student-homepage");
        } else {
            response.sendRedirect("/auth/homepage");
        }
    }
}

