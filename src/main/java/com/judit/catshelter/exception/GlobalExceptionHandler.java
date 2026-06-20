package com.judit.catshelter.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CatNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleCatNotFound(CatNotFoundException e) {
        return new ErrorResponse(
                Instant.now(),
                404,
                "Not Found",
                e.getMessage()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String message = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .map(error -> error.getDefaultMessage())
                .orElse("Invalid request");

        return new ErrorResponse(
                Instant.now(),
                400,
                "Bad Request",
                message
        );
    }

    @ExceptionHandler({
            CooldownException.class,
            CatNotReadyForAdoptionException.class,
            CatAlreadyAdoptedException.class
    })
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleConflict(RuntimeException e) {
        return new ErrorResponse(
                Instant.now(),
                409,
                "Conflict",
                e.getMessage()
        );
    }
}
