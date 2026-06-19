package com.rba.rest.utils;

import com.rba.common.exceptions.AppError;
import com.rba.common.exceptions.AppException;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.ClientAbortException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.http.converter.HttpMessageNotReadableException;

import java.io.IOException;

@ControllerAdvice
@Slf4j
public class GlobalControllerExceptionHandler {

    @Value("${exception.show-message:false}")
    private boolean showMessage;

    @ExceptionHandler({AppException.class})
    public ResponseEntity<Object> handleAppException(
            AppException ex) {

        String response = null;
        try {
            if (ex.getErrorData() != null && showMessage) {
                response = ResponseMessage.packageAndJsoniseError(ex.getError(), ex.getErrorData().toString());
            } else {
                response = ResponseMessage.packageAndJsoniseError(ex.getError());
            }
        } catch (AppException e) {

            e.printStackTrace();
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @ExceptionHandler({NumberFormatException.class, MethodArgumentNotValidException.class, ConstraintViolationException.class, MissingServletRequestParameterException.class, HttpMessageNotReadableException.class, BindException.class})
    public ResponseEntity<Object> handleBadRequestExceptions(Exception ex) {
        String response = null;
        ex.printStackTrace();
        try {
            String exceptionMessage = showMessage ? ex.toString() : "";
            response = ResponseMessage.packageAndJsoniseError(AppError.BAD_REQUEST, exceptionMessage);
        } catch (AppException e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({ClientAbortException.class})
    public void handleException(ClientAbortException ex) {
        log.info("Client aborted request!");
    }

    @ExceptionHandler({IOException.class})
    public void handleException(IOException ex) {
        log.info("Socket IO exception!");
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleException(
            Exception ex) {

        String response = null;
        ex.printStackTrace();
        try {
            String exceptionMessage = showMessage ? ex.toString() : "";
            response = ResponseMessage.packageAndJsoniseError(AppError.UNRECOGNIZED_EXCEPTION, exceptionMessage);
        } catch (AppException e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
