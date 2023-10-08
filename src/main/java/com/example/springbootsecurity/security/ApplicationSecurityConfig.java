package com.example.springbootsecurity.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class ApplicationSecurityConfig {
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public ApplicationSecurityConfig(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
          http
                  .authorizeHttpRequests((authz) -> authz
                          .requestMatchers("/", "index", "/css/*", "/js/*")
                          .permitAll()
                          .anyRequest()
                          .authenticated()
                          
                  )
                  .httpBasic(withDefaults());
          return http.build();
      }

//    @Override
    @Bean
    protected UserDetailsService userDetailsService(){
          UserDetails wycliffeMwimaliUser = User.builder()
                  .username("wycliffemwimali")
                  .password(passwordEncoder.encode("mwimali"))
                  .roles("STUDENT")
                  .build();

          return new InMemoryUserDetailsManager(
                  wycliffeMwimaliUser
          );
      }

    }

