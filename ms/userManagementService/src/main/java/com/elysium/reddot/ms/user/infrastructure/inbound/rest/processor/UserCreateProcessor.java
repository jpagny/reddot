package com.elysium.reddot.ms.user.infrastructure.inbound.rest.processor;

import com.elysium.reddot.ms.user.application.data.dto.ApiResponseDTO;
import com.elysium.reddot.ms.user.application.data.dto.UserDTO;
import com.elysium.reddot.ms.user.application.service.UserApplicationService;
import com.elysium.reddot.ms.user.infrastructure.mapper.UserProcessorMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class UserCreateProcessor implements Processor {

    private final UserApplicationService userApplicationService;

    @Override
    public void process(Exchange exchange) {
        UserDTO inputUserDTO = exchange.getIn().getBody(UserDTO.class);
        UserRepresentation userRepresentation = UserProcessorMapper.toUserRepresentation(inputUserDTO);

        UserRepresentation response = userApplicationService.createUser(userRepresentation);
        UserDTO userCreated = UserProcessorMapper.toDTO(response);

        ApiResponseDTO apiResponseDTO = new ApiResponseDTO(HttpStatus.CREATED.value(), "User created successfully.", userCreated);

        exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, HttpStatus.CREATED.value());
        exchange.getMessage().setBody(apiResponseDTO);
    }

}
