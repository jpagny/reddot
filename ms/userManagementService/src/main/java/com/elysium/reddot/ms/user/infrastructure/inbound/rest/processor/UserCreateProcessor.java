package com.elysium.reddot.ms.user.infrastructure.inbound.rest.processor;

import com.elysium.reddot.ms.user.application.data.dto.ApiResponseDTO;
import com.elysium.reddot.ms.user.application.data.dto.UserDTO;
import com.elysium.reddot.ms.user.application.service.UserApplicationService;
import com.elysium.reddot.ms.user.domain.model.UserModel;
import com.elysium.reddot.ms.user.infrastructure.mapper.UserProcessorMapper;
import lombok.AllArgsConstructor;
import org.apache.camel.Exchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UserCreateProcessor {

    private final UserApplicationService userApplicationService;

    public void createUser(Exchange exchange) {
        UserDTO inputUserDTO = exchange.getIn().getBody(UserDTO.class);
        UserModel userModel = UserProcessorMapper.toModel(inputUserDTO);

        String response = userApplicationService.createUser(userModel);

        ApiResponseDTO apiResponseDTO = new ApiResponseDTO(HttpStatus.CREATED.value(), "User created successfully.", response);

        exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, HttpStatus.CREATED.value());
        exchange.getMessage().setBody(apiResponseDTO);
    }


}
