package com.example.secureApplication.handleException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;
import java.time.LocalDateTime;

public class customAccessDeniedHandler implements AccessDeniedHandler {
    /**
     * @param request               that resulted in an <code>AccessDeniedException</code>
     * @param response              so that the user agent can be advised of the failure
     * @param accessDeniedException that caused the invocation
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        LocalDateTime currentTime = LocalDateTime.now();
        String message = accessDeniedException.getMessage();
        String path = request.getRequestURI();
        response.setHeader("NAM Bank error", "authentication failed");
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType("application/json; charset=UTF-8");
        String jsonFormat  = String.format("{\"timestamp\":\"%s\", \"status\":\"%s\",\"message\":\"%s\",\"path\":\"%s\"}",currentTime, HttpStatus.FORBIDDEN.value(),message, path);
        response.getWriter().write(jsonFormat);
    }
}
