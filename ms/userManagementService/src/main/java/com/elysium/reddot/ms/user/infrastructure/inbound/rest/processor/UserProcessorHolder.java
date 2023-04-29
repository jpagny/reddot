package com.elysium.reddot.ms.user.infrastructure.inbound.rest.processor;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Getter
@Component
public class UserProcessorHolder {
    private UserCreateProcessor userCreateProcessor;
}
