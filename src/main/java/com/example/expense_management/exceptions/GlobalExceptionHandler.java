package com.example.expense_management.exceptions;

import com.example.expense_management.models.ResponseObject;
import com.example.expense_management.models.ValidationObject;
import jakarta.persistence.EntityExistsException;
import jakarta.servlet.ServletException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({ NoSuchElementException.class })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ResponseObject> handleNoSuchElementException(
            NoSuchElementException exception
    ) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject("error", exception.getMessage(), ""));
    }

    @ExceptionHandler(ServletException.class)
    public ResponseEntity<ResponseObject> handleExpiredJwtException(ServletException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObject("error", "Phiên làm việc đã hết hạn, vui lòng đăng nhập lại", ""));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseObject> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        List<ValidationObject> validationObjectList = new ArrayList<ValidationObject>();
        List<String> listFieldExist = new ArrayList<String>();
        exception.getBindingResult().getFieldErrors().forEach(fieldError -> {
            if(!listFieldExist.contains(fieldError.getField())) {
                validationObjectList.add(new ValidationObject(fieldError.getField(), fieldError.getDefaultMessage()));
                listFieldExist.add(fieldError.getField());
            }

        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject("error", "Input data error", validationObjectList));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ResponseObject> handleHttpMessageNotReadableException(HttpMessageNotReadableException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject("error", "Input data cannot be empty", ""));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseObject> handleUnwantedException(Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(new ResponseObject("error", "Unknow error", ""));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ResponseObject> handleBadCredentialsException(BadCredentialsException e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObject("error", "Account name or password is incorrect", ""));
    }

    @ExceptionHandler(EntityExistsException.class)
    public ResponseEntity<ResponseObject> handleEntityExistsException(EntityExistsException e) {

        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ResponseObject("error", e.getMessage(), ""));
    }

}
