package br.com.desafio.controller.handler;

import br.com.desafio.controller.ResponseMessages;
import br.com.desafio.domain.exception.DataNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.springframework.http.ResponseEntity.notFound;

@ControllerAdvice
public class ApplicationExceptionHandler {

    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<ResponseMessages> handleNotFoundException(final DataNotFoundException exception) {
        return notFound().build();
    }
}
