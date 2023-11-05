package com.example.expense_management.configs;

import com.example.expense_management.configs.security.JwtAuthenticationFilter;
import com.example.expense_management.models.enums.RoleType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

@EnableWebSecurity
@Configuration
@Component
@RequiredArgsConstructor
@Slf4j
public class SecurityConfiguration {
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private final LogoutHandler logoutHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(auth -> auth
                                .requestMatchers(
                                        "/api/v1/users/**"
                                )
                                .permitAll()
                                .requestMatchers("/api/v1/user-expenses").hasAnyAuthority(RoleType.ADMIN.name(), RoleType.USER.name())
                                .requestMatchers("/api/v1/user-expenses/**").hasAnyAuthority(RoleType.ADMIN.name(), RoleType.USER.name())
                                .requestMatchers("/api/v1/user-expenses/category/**").hasAnyAuthority(RoleType.ADMIN.name(), RoleType.USER.name())
                                .requestMatchers("/api/v1/user-expenses/statistical").hasAnyAuthority(RoleType.ADMIN.name(), RoleType.USER.name())
                                .requestMatchers("/api/v1/user-expenses/statistical-by-time").hasAnyAuthority(RoleType.ADMIN.name(), RoleType.USER.name())
                                .requestMatchers("/api/v1/category").hasAnyAuthority(RoleType.ADMIN.name(), RoleType.USER.name())
                                .requestMatchers("/api/v1/category/**").hasAnyAuthority(RoleType.ADMIN.name(), RoleType.USER.name())

//                        .anyRequest()
//                        .authenticated()

                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .logout(logout -> logout.logoutUrl("/api/v1/auth/logout")
                        .addLogoutHandler(logoutHandler)
                        .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext()));

        log.info("Security configuration is loaded");
        return http.build();
    }


}