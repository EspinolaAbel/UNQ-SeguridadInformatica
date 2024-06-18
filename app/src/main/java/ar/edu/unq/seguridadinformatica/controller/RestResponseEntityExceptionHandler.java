package ar.edu.unq.seguridadinformatica.controller;

import ar.edu.unq.seguridadinformatica.service.SessionException;
import ar.edu.unq.seguridadinformatica.service.UserException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(UserException.class)
    protected ResponseEntity<Object> handleUserException(RuntimeException ex, WebRequest request) {
        return handleExceptionInternal(ex,
            ex.getMessage(),
            new HttpHeaders(),
            switch (((UserException)ex).error) {
                case NOTFOUND -> HttpStatus.NOT_FOUND;
                default -> HttpStatus.BAD_REQUEST;
            }
            , request
        );
    }

    @ExceptionHandler(SessionException.class)
    protected ResponseEntity<Object> handleSessionException(RuntimeException ex, WebRequest request) {
        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

}
