package com.elysium.reddot.ms.authentication.infrastructure.exception.type;

public class LogoutException extends RuntimeException{

    public LogoutException(String exceptionMessage) {
        super("Fail to logout : " + exceptionMessage);
    }

}
