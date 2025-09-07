package com.ec2.main.service;

import java.security.SecureRandom;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ec2.main.model.Users;
import com.ec2.main.repository.UserRepository;

@Service
public class PasswordSetupService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JavaMailSender mailSender;

    // Temporary store for OTP mapped by univId
    private final Map<String, String> otpCache = new ConcurrentHashMap<>();

    private final SecureRandom random = new SecureRandom();

    // Generate random 6-digit OTP
    public String generateOTP() {
        int number = random.nextInt(900000) + 100000; // 100000 to 999999
        return String.valueOf(number);
    }

    public void createAndSendOtp(Users user) {
        String otp = generateOTP();
        otpCache.put(user.getUnivId(), otp);

        // TODO: send OTP to user's email
        sendEmail(user.getUemail(), "Your OTP Code", "Your OTP for password setup is: " + otp);
    }

    public boolean verifyOtp(String univId, String otp) {
        String cachedOtp = otpCache.get(univId);
        return cachedOtp != null && cachedOtp.equals(otp);
    }

    public void clearOtp(String univId) {
        otpCache.remove(univId);
    }

    public boolean updatePassword(String univId, String newPassword) {
        Users user = userRepository.findByUnivId(univId).orElse(null);
        if (user == null) {
            return false;
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        clearOtp(univId);
        return true;
    }

    private void sendEmail(String toEmailPrefix, String subject, String body) {
        String fullEmail = toEmailPrefix + "@dau.ac.in";

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(fullEmail);
        message.setSubject(subject);
        message.setText(body);
        message.setFrom("ec2mailsender11@gmail.com");
        mailSender.send(message);
    }

    public boolean passwordMatches(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }


}

