package com.elysium.reddot.ms.authentication.infrastructure.inbound.rest.processor;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Getter
@Component
public class AuthenticationProcessorHolder {
    private LoginProcessor loginProcessor;
    private LogoutProcessor logoutProcessor;
}
