package com.elysium.reddot.ms.topic.infrastructure.configuration;

import org.apache.camel.component.spring.security.SpringSecurityAccessPolicy;
import org.apache.camel.component.spring.security.SpringSecurityAuthorizationPolicy;
import org.apache.camel.spi.Policy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.access.vote.UnanimousBased;
import org.springframework.security.authentication.AuthenticationManager;

import java.util.List;

/**
 * This configuration class is responsible for setting up security policies for different roles.
 */
@Configuration
public class SecurityPolicyConfiguration {

    @Bean
    public Policy adminPolicy(AuthenticationManager authenticationManager) {
        RoleVoter roleVoter = new RoleVoter();
        SpringSecurityAuthorizationPolicy policy = new SpringSecurityAuthorizationPolicy();
        policy.setAuthenticationManager(authenticationManager);
        policy.setAccessDecisionManager(new UnanimousBased(List.of(roleVoter)));
        policy.setSpringSecurityAccessPolicy(new SpringSecurityAccessPolicy(roleVoter.getRolePrefix() + "admin"));
        return policy;
    }

    @Bean
    public Policy userPolicy(AuthenticationManager authenticationManager) {
        RoleVoter roleVoter = new RoleVoter();
        SpringSecurityAuthorizationPolicy policy = new SpringSecurityAuthorizationPolicy();
        policy.setAuthenticationManager(authenticationManager);
        policy.setAccessDecisionManager(new UnanimousBased(List.of(roleVoter)));
        policy.setSpringSecurityAccessPolicy(new SpringSecurityAccessPolicy(roleVoter.getRolePrefix() + "user"));
        return policy;
    }


}