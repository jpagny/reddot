package com.elysium.reddot.ms.user.infrastructure.inbound.rest.route;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class UserRouteBuilder extends RouteBuilder {
    @Override
    public void configure() {
        from("direct:createUser")
                .to("https://<your_keycloak_url>/auth/admin/realms/<your_realm>/users")
                .id("createUserRoute");

        from("direct:updateUser")
                .toD("https://<your_keycloak_url>/auth/admin/realms/<your_realm>/users/${header.id}")
                .id("updateUserRoute");
    }
}
