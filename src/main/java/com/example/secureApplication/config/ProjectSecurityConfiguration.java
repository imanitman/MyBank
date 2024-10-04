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
import com.example.secureApplication.handler.customAuthenticationFailure;
import com.example.secureApplication.handler.customAuthenticationSuccessHandler;

@Configuration
@Profile("!prod")
@RequiredArgsConstructor
public class ProjectSecurityConfiguration {
    private final customAuthenticationFailure customAuthenticationFailure;
    private final customAuthenticationSuccessHandler customAuthenticationSuccessHandler;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(csrfConfig -> csrfConfig.disable())
                .authorizeHttpRequests((request)->
                request.requestMatchers("/balances","/cards","/loans", "/accounts", "/user").authenticated()
                            .requestMatchers("/notices","/contacts","/register").permitAll())
            .formLogin(Customizer.withDefaults())
//  thay đổi tên biến mặc định mà spring dùng để lấy ra username và password
//  .usernameParameter("username").passwordParameter("password")
// trong mvc cấu hình trang login
// .loginPage("/login")
// nếu đăng nhập thành công thì chuyển về đâu
//đăng nhập thành công hoặc thất bại chuyển về trang gì
// .defaultSuccessUrl("/") ..failureUrl("/login")
// thất bại thì làm gì .failureHandler(customAuthenticationFailure)
//  đăng nhập thành công thì làm gì
//  .successHandler(customAuthenticationSuccessHandler)
                .logout(Customizer.withDefaults());
// logoutSuccessUrl("/login?logout=true")
// .invalidateHttpSession(true) xóa session ở người dùng đi
// . clearAuthentication xóa authentication trong security context
// .deleteCookies("JSESSIONID") xóa cookie  name : JSESSIONID
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
