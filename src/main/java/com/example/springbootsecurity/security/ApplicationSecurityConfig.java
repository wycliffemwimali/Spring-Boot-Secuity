package com.example.springbootsecurity.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class ApplicationSecurityConfig {
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
                  .password("mwimali")
                  .roles("STUDENT")
                  .build();

          return new InMemoryUserDetailsManager(
                  wycliffeMwimaliUser
          );
      }

    }

