package com.api_clientes.controller;


import com.api_clientes.exception.ClientNotFoundException;
import com.api_clientes.exception.ClientUnderageException;
import com.api_clientes.exception.DuplicateCpfException;
import com.api_clientes.response.ErrorResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


import java.util.HashMap;
import java.util.Map;
@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class ControllerAdvice {

    private final MessageSource messageSource;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, ErrorResponse>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, ErrorResponse> errors = new HashMap<>();
        HttpStatus status = HttpStatus.BAD_REQUEST;

        for (var error : ex.getBindingResult().getFieldErrors()) {
            String messageCode = error.getDefaultMessage();
            String resolvedMessage = messageSource.getMessage(messageCode, null, messageCode, LocaleContextHolder.getLocale());

            errors.put(error.getField(), new ErrorResponse(messageCode, resolvedMessage));

            if (messageCode.startsWith("422")) {
                status = HttpStatus.UNPROCESSABLE_ENTITY;
            }
        }

        return new ResponseEntity<>(errors, status);
    }

    @ExceptionHandler(ClientNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleClientNotFoundException(ClientNotFoundException ex) {
        String message = messageSource.getMessage(ex.getMessage(), null, LocaleContextHolder.getLocale());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponse(ex.getMessage(), message));
    }

    @ExceptionHandler(ClientUnderageException.class)
    public ResponseEntity<ErrorResponse> handlerClientUnderageException(ClientUnderageException ex) {
        String message = messageSource.getMessage(ex.getMessage(), null, LocaleContextHolder.getLocale());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(ex.getMessage(), message));
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception exception) {
        log.error(exception.getMessage(), exception);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("500.000", "Erro inesperado. Tente novamente mais tarde."));
    }

    @ExceptionHandler(DuplicateCpfException.class)
    public ResponseEntity<ErrorResponse> handlerDuplicateCpfException(DuplicateCpfException ex) {
        String message = messageSource.getMessage(ex.getMessage(), null, LocaleContextHolder.getLocale());
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(new ErrorResponse(ex.getMessage(), message));
    }

}
