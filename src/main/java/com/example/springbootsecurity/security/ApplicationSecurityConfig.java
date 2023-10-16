package com.example.springbootsecurity.security;

import com.example.springbootsecurity.auth.ApplicationUserService;
import com.example.springbootsecurity.jwt.JwtConfig;
import com.example.springbootsecurity.jwt.JwtTokenVerifier;
import com.example.springbootsecurity.jwt.JwtUsernameAndPasswordAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.SecretKey;

import static com.example.springbootsecurity.security.ApplicationUserRole.STUDENT;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ApplicationSecurityConfig {
    private final PasswordEncoder passwordEncoder;
    private final ApplicationUserService applicationUserService;
    private final SecretKey secretKey;
    private final JwtConfig jwtConfig;

    @Autowired
    public ApplicationSecurityConfig(PasswordEncoder passwordEncoder, ApplicationUserService applicationUserService, SecretKey secretKey, JwtConfig jwtConfig) {
        this.passwordEncoder = passwordEncoder;
        this.applicationUserService = applicationUserService;
        this.secretKey = secretKey;
        this.jwtConfig = jwtConfig;
    }



    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
          http
//                  .csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
//                  .and()
                  .csrf().disable()
                  .sessionManagement()
                  .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                  .and()
                  .addFilter(new JwtUsernameAndPasswordAuthenticationFilter(http.authenticationManager(), jwtConfig, secretKey))
                  .addFilterAfter(new JwtTokenVerifier(secretKey, jwtConfig),JwtUsernameAndPasswordAuthenticationFilter.class)
                  .authorizeHttpRequests((authz) -> authz
                          .requestMatchers("/", "index", "/css/*", "/js/*")
                          .permitAll()
                          .requestMatchers("/api/**").hasRole(STUDENT.name())
//                          .requestMatchers(HttpMethod.DELETE, "/management/api/**").hasAuthority(COURSE_WRITE.getPermission())
//                          .requestMatchers(HttpMethod.POST, "/management/api/**").hasAuthority(COURSE_WRITE.getPermission())
//                          .requestMatchers(HttpMethod.PUT, "/management/api/**").hasAuthority(COURSE_WRITE.getPermission())
//                          .requestMatchers(HttpMethod.GET, "/management/api/**").hasAnyRole(ADMIN.name(), ADMINTRAINEE.name())
                          .anyRequest()
                          .authenticated()

                  );
//                  .formLogin()
//                  .loginPage("/login").permitAll()
//                  .defaultSuccessUrl("/courses", true)
//                  .passwordParameter("password")
//                  .usernameParameter("username")
//                  .and()
//                  .rememberMe()
//                  .tokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(21))
//                  .key("somethingverysecured")
//                  .rememberMeParameter("remember-me")
//                  .and()
//                  .logout()
//                  .logoutUrl("/logout")
//                  .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET")) // https://docs.spring.io/spring-security/site/docs/4.2.12.RELEASE/apidocs/org/springframework/security/config/annotation/web/configurers/LogoutConfigurer.html
//                  .clearAuthentication(true)
//                  .invalidateHttpSession(true)
//                  .deleteCookies("JSESSIONID", "remember-me")
//                  .logoutSuccessUrl("/login");

//                  .httpBasic(withDefaults());
          return http.build();
      }

//    @Override
//    @Bean
//    protected UserDetailsService userDetailsService(){
//          UserDetails wycliffeMwimaliUser = User.builder()
//                  .username("wycliffemwimali")
//                  .password(passwordEncoder.encode("mwimali"))
////                  .roles(STUDENT.name())
//                  .authorities(STUDENT.getGrantedAuthorities())
//                  .build();
//
//          UserDetails emmanuelKiptum = User.builder()
//                  .username("emmanuel")
//                  .password(passwordEncoder.encode("kiptum"))
////                  .roles(ADMIN.name())
//                  .authorities(ADMIN.getGrantedAuthorities())
//                  .build();
//
//          UserDetails amigosCode = User.builder()
//
//                  .username("amigoscode")
//                  .password(passwordEncoder.encode("amigos"))
////                  .roles(ADMINTRAINEE.name())
//                  .authorities(ADMINTRAINEE.getGrantedAuthorities())
//                  .build();
//          return new InMemoryUserDetailsManager(
//                  wycliffeMwimaliUser,
//                  emmanuelKiptum,
//                  amigosCode
//          );
//      }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(applicationUserService);
        return provider;
    }

    }

