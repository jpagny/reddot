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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CheckTokenProcessorTest {

    private CheckTokenProcessor checkTokenProcessor;
    @Mock
    private KeycloakService keycloakService;
    private CamelContext camelContext;

    @BeforeEach
    void setUp() {
        camelContext = new DefaultCamelContext();
        checkTokenProcessor = new CheckTokenProcessor(keycloakService);
    }

    @Test
    @DisplayName("Given a valid auth header, when validating the token, then no exception is thrown")
    public void validateToken_ValidToken_NoExceptionThrown() throws URISyntaxException, IOException {
        // given
        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setHeader("Authorization", "Bearer myFakeToken");
        String headerAfterCheckToken = "{\"realm_access\":{\"roles\":[\"default-roles-reddot\",\"user\"]},\"active\":true}";

        when(keycloakService.callTokenIntrospectionEndpoint(any(String.class))).thenReturn(headerAfterCheckToken);

        // when && then
        assertDoesNotThrow(() -> checkTokenProcessor.process(exchange));
    }

    @Test
    @DisplayName("Given a null auth header, when validating the token, then TokenNotValidException is thrown")
    public void validateToken_NullToken_ThrowsException() {
        // given
        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setHeader("Authorization", null);

        // when && then
        assertThrows(TokenNotValidException.class, () -> checkTokenProcessor.process(exchange));
    }

    @Test
    @DisplayName("Given an empty auth header, when validating the token, then TokenNotValidException is thrown")
    public void validateToken_EmptyToken_ThrowsException() {
        // given
        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setHeader("Authorization", "");

        // when && then
        assertThrows(TokenNotValidException.class, () -> checkTokenProcessor.process(exchange));
    }

    @Test
    @DisplayName("Given an auth header without 'Bearer ', when validating the token, then TokenNotValidException is thrown")
    public void validateToken_TokenWithoutBearer_ThrowsException() {
        // given
        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setHeader("Authorization", "myFakeToken");

        // when && then
        assertThrows(TokenNotValidException.class, () -> checkTokenProcessor.process(exchange));
    }


}
