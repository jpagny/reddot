package com.elysium.reddot.ms.user.infrastructure.inbound.rest.route;

import com.elysium.reddot.ms.user.application.data.dto.UserDTO;
import com.elysium.reddot.ms.user.domain.exception.type.BadValueException;
import com.elysium.reddot.ms.user.domain.model.UserModel;
import com.elysium.reddot.ms.user.infrastructure.constant.UserRouteEnum;
import com.elysium.reddot.ms.user.infrastructure.data.exception.GlobalExceptionDTO;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.support.DefaultExchange;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserRouteBuilderIT {

    @Autowired
    private CamelContext camelContext;

    @Autowired
    private ProducerTemplate template;


    @Test
    @DisplayName("given createUser service failure when route is called then GlobalException is triggered")
    void givenCreateUserServiceFailure_whenRouteIsCalled_thenGlobalExceptionIsTriggered() {

        // given
        UserDTO userDTO = new UserDTO("xxx", "username", "Pa", "mail@gmail", true);

        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setHeader(Exchange.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        exchange.getIn().setBody(userDTO);

        // expected
        GlobalExceptionDTO expectedApiResponse = new GlobalExceptionDTO("BadValueException", "Bad value in password : must have a minimum length 8. Password has 2");

        // when
        Exchange responseExchange = template.send(UserRouteEnum.USER_REGISTRATION.getRouteName(), exchange);
        GlobalExceptionDTO actualResponse = responseExchange.getIn().getBody(GlobalExceptionDTO.class);

        // then
        assertEquals(expectedApiResponse.getExceptionClass(), actualResponse.getExceptionClass());
        assertEquals(expectedApiResponse.getMessage(), actualResponse.getMessage());
    }


}
