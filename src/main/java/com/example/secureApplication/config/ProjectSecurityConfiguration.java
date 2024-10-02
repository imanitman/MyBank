package com.example.secureApplication.config;

import com.example.secureApplication.handleException.customAccessDeniedHandler;
import com.example.secureApplication.handleException.customAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.authentication.password.CompromisedPasswordDecision;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
@Profile("!prod")
public class ProjectSecurityConfiguration {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.
                csrf(csrfConfig -> csrfConfig.disable())
                .authorizeHttpRequests((request)->
                request.requestMatchers("/balances","/cards","/loans", "/accounts").authenticated()
                            .requestMatchers("/notices","/contacts","/register").permitAll()
        );
        httpSecurity.formLogin(Customizer.withDefaults());
        httpSecurity.httpBasic(hbc -> hbc.authenticationEntryPoint(new customAuthenticationEntryPoint()));
        // global exception handler because not only login throw error 401 but also other errors
        httpSecurity.exceptionHandling(ehc -> ehc. accessDeniedHandler(new customAccessDeniedHandler()));
        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
