package com.segs.demo.security;
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
    // LDAP or Database
    @Bean
    public InMemoryUserDetailsManager createUserDetailsManager(){
        
        UserDetails userDetails1 = createNewUser("admin", "admin");
        UserDetails userDetails2 = createNewUser("sumit", "vish");
        
        return new InMemoryUserDetailsManager(userDetails1,userDetails2);
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

    // whenever a web request comes in it is processed by the chain first
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{  // we take HttpSecurity object to implement our own rules
        
        // this implements that whenever we refer to any url in our domain....it must
        // be authenticated first, then only we would be able to see those webpages
        http.authorizeHttpRequests(
            auth -> auth
            .requestMatchers("/images/**", "/css/**", "/js/**").permitAll()
            .anyRequest().authenticated()
            );
        //All incoming HTTP requests must be authenticated (i.e. logged in)
        
        
        http.formLogin(form -> form
            .loginPage("/login")
            .defaultSuccessUrl("/", true)
            .permitAll()
        );
        http.httpBasic(withDefaults());
        http.logout(withDefaults());
        http.csrf().disable();
        http.headers().frameOptions().disable();
        return http.build(); //Creates the SecurityFilterChain with all the rules you defined.
    }
}
    

