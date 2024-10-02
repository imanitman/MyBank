package com.example.secureApplication.config;

import com.example.secureApplication.handleException.customAccessDeniedHandler;
import com.example.secureApplication.handleException.customAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
// Apply for end user
@Profile("prod")
@Configuration
@RequiredArgsConstructor
public class ProjectSecurityProdConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                //số session tối đa, ngăn chặn người dùng mới đăng nhập khi đanh có người sử dụng
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
