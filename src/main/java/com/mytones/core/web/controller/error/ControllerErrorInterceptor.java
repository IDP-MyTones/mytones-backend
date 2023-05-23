package com.mytones.core.web.controller.error;

import com.mytones.core.web.dto.error.ErrorMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.HttpMediaTypeException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MultipartException;


@ControllerAdvice
@Slf4j
class ControllerErrorInterceptor {
    @ExceptionHandler({UsernameNotFoundException.class, AuthenticationCredentialsNotFoundException.class})
    public ResponseEntity<ErrorMessage> handleAuthError(Exception e, HttpServletRequest request) {
        log.error("Caught error", e);

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorMessage(e.getMessage(), request.getRequestURI()));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorMessage> handleAccessDenied(HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ErrorMessage("Access denied", request.getRequestURI()));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorMessage> handleNotFoundException(NotFoundException e, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorMessage(e.getMessage(), request.getRequestURI()));
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class, HttpMessageConversionException.class, IllegalArgumentException.class})
    public ResponseEntity<ErrorMessage> handleBadArguments(Throwable t, HttpServletRequest request) {
        log.error("Catched", t);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorMessage("Bad arguments", request.getRequestURI()));
    }

    @ExceptionHandler({MultipartException.class, HttpMediaTypeException.class})
    public ResponseEntity<ErrorMessage> handleBadRequest(Exception e, HttpServletRequest request) {
        log.error("Catched", e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorMessage(e.getMessage(), request.getRequestURI()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessage> handleException(Exception e, HttpServletRequest request) {
        log.error("Caught error", e);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorMessage(e.getMessage(), request.getRequestURI()));
    }

}
