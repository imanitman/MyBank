package com.example.secureApplication.handleException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;
import java.time.LocalDateTime;

public class customAuthenticationEntryPoint implements AuthenticationEntryPoint {
    /**
     * @param request       that resulted in an <code>AuthenticationException</code>
     * @param response      so that the user agent can begin authentication
     * @param authException that caused the invocation
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        LocalDateTime currentTime = LocalDateTime.now();
        String message = authException.getMessage();
        String path = request.getRequestURI();
        response.setHeader("NAM Bank error", "authentication failed");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json; charset=UTF-8");
        String jsonFormat  = String.format("{\"timestamp\":\"%s\", \"status\":\"%s\",\"message\":\"%s\",\"path\":\"%s\"}",currentTime, HttpStatus.UNAUTHORIZED.value(),message, path);
        response.getWriter().write(jsonFormat);
    }
}
