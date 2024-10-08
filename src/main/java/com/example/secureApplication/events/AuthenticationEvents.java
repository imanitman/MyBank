package com.example.secureApplication.events;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AuthenticationEvents {

    @EventListener
    public void onSuccess(AuthenticationSuccessEvent event) {
        log.info("login successfull : {}", event.getAuthentication().getName());
    }
    @EventListener
    public void onFailed(AuthenticationSuccessEvent event) {
        log.info("login failed : {}", event.getAuthentication().getName());
    }
}
