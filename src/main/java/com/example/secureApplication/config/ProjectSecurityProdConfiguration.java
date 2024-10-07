package com.example.secureApplication.config;

import com.example.secureApplication.filter.CsrfCookieFilter;
import com.example.secureApplication.handleException.customAccessDeniedHandler;
import com.example.secureApplication.handleException.customAuthenticationEntryPoint;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Collections;

// Apply for end user
@Profile("prod")
@Configuration
@RequiredArgsConstructor
public class ProjectSecurityProdConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        CsrfTokenRequestAttributeHandler csrfTokenRequestAttributeHandler = new CsrfTokenRequestAttributeHandler();
        httpSecurity

                //số session tối đa, ngăn chặn người dùng mới đăng nhập khi đanh có người sử dụng
                .cors(corsConfig -> corsConfig.configurationSource(new CorsConfigurationSource() {

                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {

                        CorsConfiguration corsConfiguration = new CorsConfiguration();
                        corsConfiguration.setAllowedOrigins(Collections.singletonList("http://localhost:4200"));
                        corsConfiguration.setAllowCredentials(true);
                        corsConfiguration.setAllowedMethods(Collections.singletonList("*"));
                        corsConfiguration.setAllowedHeaders(Collections.singletonList("*"));
                        corsConfiguration.setMaxAge(3600L);
                        return corsConfiguration;
                    }
                }))
                .sessionManagement(smc ->
                        smc
                                .sessionCreationPolicy(SessionCreationPolicy.ALWAYS))
                .csrf(csrfConfig -> csrfConfig
                        .csrfTokenRequestHandler(csrfTokenRequestAttributeHandler)
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
                .addFilterAfter(new CsrfCookieFilter(), BasicAuthenticationFilter.class)
                .sessionManagement(smc ->smc.maximumSessions(1).maxSessionsPreventsLogin(true))
                .requiresChannel(rcc -> rcc.anyRequest().requiresSecure()) //only https can request to the server
                .csrf(csrfConfig -> csrfConfig.disable())
                .authorizeHttpRequests((request) -> request
                        .requestMatchers("/balances","/cards","/loans", "/accounts").authenticated()
                        .requestMatchers("/notices","/contacts","/register").permitAll()
                );
        httpSecurity.formLogin(Customizer.withDefaults());
        httpSecurity.httpBasic(hbc -> hbc
                .authenticationEntryPoint(new customAuthenticationEntryPoint()));
        httpSecurity.exceptionHandling(ehc -> ehc. accessDeniedHandler(new customAccessDeniedHandler()));
        return httpSecurity.build();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
