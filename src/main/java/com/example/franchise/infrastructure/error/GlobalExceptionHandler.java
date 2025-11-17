package com.example.franchise.infrastructure.error;

import com.example.franchise.domain.exception.BranchNotFoundException;
import com.example.franchise.domain.exception.FranchiseNotFoundException;
import com.example.franchise.domain.exception.ProductNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

import java.time.Instant;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
            FranchiseNotFoundException.class,
            BranchNotFoundException.class,
            ProductNotFoundException.class
    })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Mono<ApiErrorResponse> handleNotFound(RuntimeException ex,
                                                 ServerHttpRequest request) {
        log.warn("Not found: {} - path={}", ex.getMessage(), request.getPath());

        HttpStatus status = HttpStatus.NOT_FOUND;

        return Mono.just(
                ApiErrorResponse.builder()
                        .timestamp(Instant.now())
                        .status(status.value())
                        .error(status.getReasonPhrase())
                        .message(ex.getMessage())
                        .path(request.getPath().value())
                        .build()
        );
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Mono<ApiErrorResponse> handleGeneric(Exception ex,
                                                ServerHttpRequest request) {
        log.error("Unexpected error at path={}", request.getPath(), ex);

        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        return Mono.just(
                ApiErrorResponse.builder()
                        .timestamp(Instant.now())
                        .status(status.value())
                        .error(status.getReasonPhrase())
                        .message("Unexpected error")
                        .path(request.getPath().value())
                        .build()
        );
    }
}
