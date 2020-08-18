package com.kosign.push.configs;

import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.*;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.io.IOException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class RequestExceptionHandler extends ResponseEntityExceptionHandler  {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException err,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status, WebRequest request) {

        try{
            Map<String, Object> body = new LinkedHashMap<>();
            body.put("timestamp", new Date().toInstant().toString());
            body.put("status", status.value());

            //Get all errors
            List<String> errors = err.getBindingResult()
                    .getFieldErrors()
                    .stream()
                    .map(x ->
                            x.getField() + " : " + x.getDefaultMessage()
                    )
                    .collect(Collectors.toList());

            body.put("errors", errors);

            return new ResponseEntity<>(body, headers, status);
        }catch (Exception ex ){
            System.out.println(ex);

            return new ResponseEntity<>(request, headers, status);
        }

    }
    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException err, HttpHeaders headers,
            HttpStatus status, WebRequest request) {

        try{
            Map<String, Object> body = new LinkedHashMap<>();
            body.put("timestamp", new Date().toInstant().toString());
            body.put("status", status.value());
            body.put("errors", err.getMessage());

            return new ResponseEntity<>(body, headers, status);

        }catch (Exception ex ){
            System.out.println(ex);

            return new ResponseEntity<>(request, headers, status);
        }
        // MissingServletRequestParameterException handling code goes here.
    }

//    @ExceptionHandler(value = { Exception.class , ConstraintViolationException.class })
//    public void constraintViolationException(HttpServletResponse response) throws IOException {
//        response.sendError(HttpStatus.BAD_REQUEST.value());
//    }

    @ExceptionHandler({Exception.class,ConstraintViolationException.class})
    public final ResponseEntity<Object> handleConstraintViolation(
            ConstraintViolationException ex,
            WebRequest request)
    {
        List<String> details = ex.getConstraintViolations()
                .parallelStream()
                .map(e -> e.getMessage())
                .collect(Collectors.toList());

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", new Date().toInstant().toString());
        body.put("status", "400");
        body.put("errors", details);

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ValidationException.class)
    protected ResponseEntity<Object> handleValidationException(
            ValidationException ex) {


        return new ResponseEntity<>(ex, HttpStatus.BAD_REQUEST);
    }

    protected ResponseEntity<Object> handleServletRequestBindingException(
            ServletRequestBindingException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        return handleExceptionInternal(ex, null, headers, status, request);
    }


    protected ResponseEntity<Object> handleConversionNotSupported(
            ConversionNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        return handleExceptionInternal(ex, null, headers, status, request);
    }



    protected ResponseEntity<Object> handleTypeMismatch(
            TypeMismatchException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        System.out.println("handleTypeMismatch");
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", new Date().toInstant().toString());
        body.put("status", status.value());
        body.put("errors",ex.getRequiredType()+ " is mismatch");

        return new ResponseEntity<>(body,HttpStatus.BAD_REQUEST);
    }


    protected ResponseEntity<Object> handleBindException(
            BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        System.out.println("handleBindException");
//        return handleExceptionInternal(ex, ex, headers, status, request);
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", new Date().toInstant().toString());
        body.put("status", status.value());

        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(x ->
                        x.getField() + " : " + x.getDefaultMessage()
                )
                .collect(Collectors.toList());

        body.put("errors", errors);
        return new ResponseEntity<>(body,HttpStatus.BAD_REQUEST);
    }

}
