package com.elysium.reddot.ms.user.infrastructure.inbound.rest.processor;

import com.elysium.reddot.ms.user.application.data.dto.ApiResponseDTO;
import com.elysium.reddot.ms.user.application.data.dto.UserDTO;
import com.elysium.reddot.ms.user.application.data.mapper.UserDTOUserModelMapper;
import com.elysium.reddot.ms.user.application.service.UserApplicationServiceImpl;
import com.elysium.reddot.ms.user.domain.model.UserModel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 * Processor for handling the creation of a user.
 */
@Component
@AllArgsConstructor
@Slf4j
public class CreateUserProcessor implements Processor {

    private final UserApplicationServiceImpl userApplicationService;

    /**
     * Processes the create user request by converting the incoming UserDTO to UserModel,
     * calling the user application service to create the user, and constructing the response.
     *
     * @param exchange the Camel Exchange object representing the message exchange
     */
    @Override
    public void process(Exchange exchange) {

        UserDTO inputUserDTO = exchange.getIn().getBody(UserDTO.class);
        log.debug("Received input UserDTO: {}", inputUserDTO);

        UserModel userModel = UserDTOUserModelMapper.toUserModel(inputUserDTO);


        UserModel userCreated = userApplicationService.createUser(userModel);

        ApiResponseDTO apiResponseDTO = new ApiResponseDTO(HttpStatus.CREATED.value(), "User created successfully.", userCreated);
        exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, HttpStatus.CREATED.value());
        exchange.getMessage().setBody(apiResponseDTO);
    }

}
