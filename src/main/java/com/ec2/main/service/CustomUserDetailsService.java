package com.ec2.main.service;

import com.ec2.main.model.Users;
import com.ec2.main.repository.UserRepository;
import com.ec2.main.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String univId) throws UsernameNotFoundException {
        Users user = userRepository.findByUnividWithRoles(univId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with univid: " + univId));
        return new CustomUserDetails(user);
    }


}
