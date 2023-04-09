package com.elysium.reddot.ms.topic.application.exception;

import com.elysium.reddot.ms.topic.application.response.ApiResult;
import com.elysium.reddot.ms.topic.domain.exception.ResourceAlreadyExistException;
import com.elysium.reddot.ms.topic.domain.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceAlreadyExistException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<ApiResult> handleResourceAlreadyExistException(ResourceAlreadyExistException exception) {
        ApiResult apiResult = new ApiResult(exception.getMessage());
        return new ResponseEntity<>(apiResult, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ApiResult> handleResourceNotFoundException(ResourceNotFoundException exception) {
        ApiResult apiResult = new ApiResult(exception.getMessage());
        return new ResponseEntity<>(apiResult, HttpStatus.NOT_FOUND);
    }


}