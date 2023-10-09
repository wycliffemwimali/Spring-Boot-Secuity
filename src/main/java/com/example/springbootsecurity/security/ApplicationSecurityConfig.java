package com.example.springbootsecurity.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import static com.example.springbootsecurity.security.ApplicationUserPermission.*;
import static com.example.springbootsecurity.security.ApplicationUserRole.*;
import static com.example.springbootsecurity.security.ApplicationUserRole.STUDENT;
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
                  .csrf().disable()
                  .authorizeHttpRequests((authz) -> authz
                          .requestMatchers("/", "index", "/css/*", "/js/*")
                          .permitAll()
                          .requestMatchers("/api/**").hasRole(STUDENT.name())
                          .requestMatchers(HttpMethod.DELETE, "/management/api/**").hasAuthority(COURSE_WRITE.name())
                          .requestMatchers(HttpMethod.POST, "/management/api/**").hasAuthority(COURSE_WRITE.name())
                          .requestMatchers(HttpMethod.PUT, "/management/api/**").hasAuthority(COURSE_WRITE.name())
                          .requestMatchers(HttpMethod.GET, "/management/api/**").hasAnyRole(ADMIN.name(), ADMINTRAINEE.name())
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
//                  .roles(STUDENT.name())
                  .authorities(STUDENT.getGrantedAuthorities())
                  .build();

          UserDetails emmanuelKiptum = User.builder()
                  .username("emmanuel")
                  .password(passwordEncoder.encode("kiptum"))
//                  .roles(ADMIN.name())
                  .authorities(ADMIN.getGrantedAuthorities())
                  .build();

          UserDetails amigosCode = User.builder()

                  .username("amigoscode")
                  .password(passwordEncoder.encode("amigos"))
//                  .roles(ADMINTRAINEE.name())
                  .authorities(ADMINTRAINEE.getGrantedAuthorities())
                  .build();
          return new InMemoryUserDetailsManager(
                  wycliffeMwimaliUser,
                  emmanuelKiptum,
                  amigosCode
          );
      }

    }

