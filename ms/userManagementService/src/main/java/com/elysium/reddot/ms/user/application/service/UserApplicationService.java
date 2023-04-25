package com.elysium.reddot.ms.user.application.service;

import com.elysium.reddot.ms.user.domain.model.UserModel;
import com.elysium.reddot.ms.user.domain.port.inbound.IUserManagementService;
import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserApplicationService implements IUserManagementService {

    @Autowired
    private ProducerTemplate producerTemplate;

    @Override
    public UserModel getUserById(long id) {
        return null;
    }

    @Override
    public String createUser(UserModel userToCreate) {
        return producerTemplate.requestBody("https://<your_keycloak_url>/auth/admin/realms/<your_realm>/users", userToCreate, String.class);
    }
}
