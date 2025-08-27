package com.ec2.main.security;
import java.util.function.Function;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SpringSecurityConfiguration {
    @Bean
    public InMemoryUserDetailsManager createUserDetailsManager(){
        
        UserDetails userDetails1 = createNewUser("admin", "admin");
        UserDetails userDetails2 = createNewUser("sumit", "vish");
        UserDetails userDetails3 = createNewUser("faculty1", "faculty1");
        UserDetails userDetails4 = createNewUser("faculty2", "faculty2");
        UserDetails userDetails5 = createNewUser("faculty3", "faculty3");
        UserDetails userDetails6 = createNewUser("faculty4", "faculty4");
        UserDetails userDetails7 = createNewUser("faculty5", "faculty5");

        return new InMemoryUserDetailsManager(userDetails1,userDetails2,userDetails3,userDetails4,userDetails5,userDetails6,userDetails7);
    }
    private UserDetails createNewUser(String username, String password) {
        Function<String,String> passwordEncoder = input -> passwordEncoder().encode(input);
        UserDetails userDetails = User.builder().passwordEncoder(passwordEncoder)
                                    .username(username)
                                    .password(password)
                                    .roles("USER","ADMIN")
                                    .build();
        return userDetails;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{  
        http.authorizeHttpRequests(
            auth -> auth
            .requestMatchers("/images/**", "/css/**", "/js/**").permitAll()
            .anyRequest().authenticated()
            );
        http.formLogin(form -> form
            .loginPage("/login")
            .defaultSuccessUrl("/segs/faculty", true)
            .permitAll()
        );
        http.httpBasic(withDefaults());
        http.logout(withDefaults());
        http.csrf().disable();
        http.headers().frameOptions().disable();
        return http.build(); //Creates the SecurityFilterChain with all the rules you defined.
    }
}
    

