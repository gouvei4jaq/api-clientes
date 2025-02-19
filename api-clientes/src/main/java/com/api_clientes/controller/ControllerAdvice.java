package com.api_clientes.controller;


import com.api_clientes.exception.ClientNotFoundException;
import com.api_clientes.exception.ClientUnderageException;
import com.api_clientes.exception.DuplicateCpfException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
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
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error -> {
            String messageCode = error.getDefaultMessage();

            String resolvedMessage = messageSource.getMessage(messageCode, null, messageCode, LocaleContextHolder.getLocale());

            errors.put(error.getField(), resolvedMessage);

        });

        return errors;
    }

    @ExceptionHandler(ClientNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleClientNotFoundException(ClientNotFoundException ex) {
        return messageSource.getMessage(ex.getMessage(), null, LocaleContextHolder.getLocale());
    }

    @ExceptionHandler(ClientUnderageException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handlerClientUnderageException(ClientUnderageException ex) {
        return messageSource.getMessage(ex.getMessage(), null, LocaleContextHolder.getLocale());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public void handleException(Exception exception) {
        log.error(exception.getMessage(), exception);
    }

    @ExceptionHandler(DuplicateCpfException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public String handlerDuplicateCpfException(DuplicateCpfException ex) {
        return messageSource.getMessage(ex.getMessage(), null, LocaleContextHolder.getLocale());
    }

}
