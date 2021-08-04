package com.example.testemployee.exceptions;

import com.example.testemployee.exceptions.dto.ExceptionDTO;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.internal.engine.ConstraintViolationImpl;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.ConstraintViolationException;
import java.util.Collections;
import java.util.Objects;
import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    //Thrown on use of @Validated with javax.validation
    //400
    @ExceptionHandler({ConstraintViolationException.class})
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Object handleValidationError(ConstraintViolationException exception) {
        log.error("Error ConstraintViolationException: " + exception.getMessage(), exception);
        return new ExceptionDTO(exception.getConstraintViolations().stream()
                .map(violation -> {
                    String propertyName = ((PathImpl) violation.getPropertyPath())
                            .getLeafNode().getName();
                    String errorMessage = propertyName + " " + violation.getMessage();
                    return new ExceptionDTO.Exception(errorMessage,
                            violation.getPropertyPath().toString());
                }).collect(Collectors.toList()));
    }

    //Thrown on use of @Validated with @Valid and javax.validation
    //400
    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Object handleMethodArgumentNotValidExceptionError(MethodArgumentNotValidException exception) {
        log.error("Error MethodArgumentNotValidException: " + exception.getMessage(), exception);
        return new ExceptionDTO(exception.getBindingResult().getAllErrors().stream()
                .map(objectError -> {
                    Object o = objectError.unwrap(Object.class);
                    if (o instanceof ConstraintViolationImpl) {
                        return (ConstraintViolationImpl) o;
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .map(violation -> {
                    String propertyName = ((PathImpl) violation.getPropertyPath())
                            .getLeafNode().getName();
                    String errorMessage = propertyName + " " + violation.getMessage();
                    return new ExceptionDTO.Exception(errorMessage,
                            violation.getPropertyPath().toString());
                }).collect(Collectors.toList()));
    }

    //400
    @ExceptionHandler({HttpMessageNotReadableException.class})
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Object handleHttpMessageNotReadableException(HttpMessageNotReadableException exception) {
        log.error("Error HttpMessageNotReadableException: " + exception.getMessage(), exception);
        return new ExceptionDTO(Collections.singletonList(
                new ExceptionDTO.Exception("Unable to convert supplied json data.", "Request body.")));
    }

    //403
    @ExceptionHandler({AccessDeniedException.class})
    @ResponseStatus(code = HttpStatus.FORBIDDEN)
    @ResponseBody
    public Object handleAccessDeniedException(AccessDeniedException exception) {
        log.error("error AccessDeniedException: " + exception.getMessage());
        return new ExceptionDTO(Collections.singletonList(
                new ExceptionDTO.Exception("AccessDenied.", "Authorization Header.")));
    }

    //500
    @ExceptionHandler({Exception.class})
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public Object handleException(Exception exception) {
        log.error("Error Exception: " + exception.getCause(), exception);
        return new ExceptionDTO(Collections.singletonList(
                new ExceptionDTO.Exception("Internal Server Error.", "Internal Server.")));
    }
}
