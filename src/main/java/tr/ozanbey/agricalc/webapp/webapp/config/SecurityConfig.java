package tr.ozanbey.agricalc.webapp.webapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import tr.ozanbey.agricalc.webapp.webapp.security.CurrentUserDetailsService;
import tr.ozanbey.agricalc.webapp.webapp.security.CustomPasswordEncoder;
import tr.ozanbey.agricalc.webapp.webapp.security.SecurityFailureHandler;
import tr.ozanbey.agricalc.webapp.webapp.security.SecuritySuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final SecuritySuccessHandler successHandler;
    private final SecurityFailureHandler failureHandler;
    private final CurrentUserDetailsService userDetailsService;

    public SecurityConfig(SecuritySuccessHandler successHandler,
                          SecurityFailureHandler failureHandler,
                          CurrentUserDetailsService userDetailsService) {
        this.successHandler = successHandler;
        this.failureHandler = failureHandler;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // JSF için CSRF açık olmalı
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/jakarta.faces.resource/**", "/public/**", "/common/**")
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/jakarta.faces.resource/**").permitAll()
                        .requestMatchers("/", "/common/**", "/public/**").permitAll()
                        // user özel
                        .requestMatchers("/secured/**").hasAnyRole("USER")
                        // admin özel
                        .requestMatchers("/secured/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/public/login.xhtml")
                        .usernameParameter("loginForm:phone")
                        .passwordParameter("loginForm:password")
                        .successHandler(successHandler)
                        .failureHandler(failureHandler)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutRequestMatcher(request ->
                                request.getRequestURI().equals(request.getContextPath() + "/logout")
                        )
                        .deleteCookies("AGRICALCSESSION", "agricalc-remember-me")
                        .invalidateHttpSession(true)
                        .logoutSuccessUrl("/")
                        .clearAuthentication(true)
                        .permitAll()
                )
                .sessionManagement(session -> session
                        .sessionFixation().migrateSession()
                        .maximumSessions(3)
                )
                .rememberMe(remember -> remember
                        .tokenValiditySeconds(1209600)
                        .useSecureCookie(true)
                        .rememberMeCookieName("agricalc-remember-me")
                        .key("")
                )
                .exceptionHandling(ex -> ex
                        .accessDeniedPage("/common/access-denied")
                )
                .authenticationProvider(authenticationProvider());
        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(new CustomPasswordEncoder());
        provider.setUserDetailsService(userDetailsService);
        return provider;
    }

}
