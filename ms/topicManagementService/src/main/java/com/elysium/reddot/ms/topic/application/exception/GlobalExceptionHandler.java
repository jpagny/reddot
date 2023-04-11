package com.elysium.reddot.ms.topic.application.exception;

import com.elysium.reddot.ms.topic.application.service.dto.ApiResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceAlreadyExistException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<ApiResponseDTO> handleResourceAlreadyExistException(ResourceAlreadyExistException exception) {
        ApiResponseDTO apiResponseDTO = new ApiResponseDTO(exception.getMessage());
        return new ResponseEntity<>(apiResponseDTO, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ApiResponseDTO> handleResourceNotFoundException(ResourceNotFoundException exception) {
        ApiResponseDTO apiResponseDTO = new ApiResponseDTO(exception.getMessage());
        return new ResponseEntity<>(apiResponseDTO, HttpStatus.NOT_FOUND);
    }


}