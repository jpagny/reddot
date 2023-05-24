package com.elysium.reddot.ms.board.infrastructure.inbound.rest.processor;

import com.elysium.reddot.ms.board.application.service.KeycloakService;
import com.elysium.reddot.ms.board.infrastructure.data.exception.TokenNotValidException;
import com.elysium.reddot.ms.board.infrastructure.inbound.rest.processor.keycloak.CheckTokenProcessor;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.support.DefaultExchange;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class CheckTokenProcessorTest {

    private CheckTokenProcessor checkTokenProcessor;
    @Mock
    private KeycloakService keycloakService;
    private CamelContext camelContext;

    @BeforeEach
    void setUp() {
        camelContext = new DefaultCamelContext();
        checkTokenProcessor = new CheckTokenProcessor(keycloakService);
    }

    @ParameterizedTest
    @DisplayName("given invalid auth headers, when validating the token, then TokenNotValidException is thrown")
    @NullSource
    @EmptySource
    @ValueSource(strings = "myFakeToken")
    void validateToken_InvalidToken_ThrowsException(String token) {
        // given
        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setHeader("Authorization", token);

        // when && then
        assertThrows(TokenNotValidException.class, () -> checkTokenProcessor.process(exchange));
    }


}
