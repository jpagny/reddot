package com.elysium.reddot.ms.user.infrastructure.inbound.rest.processor.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Component;

/**
 * Holder class for the CreateUserProcessor instance.
 */
@AllArgsConstructor
@Getter
@Component
public class UserProcessorHolder {
    private CreateUserProcessor createUserProcessor;
}
