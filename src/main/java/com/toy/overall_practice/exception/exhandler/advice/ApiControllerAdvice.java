package com.toy.overall_practice.exception.exhandler.advice;

import com.toy.overall_practice.exception.NotFoundMemberException;
import com.toy.overall_practice.exception.exhandler.ExResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestControllerAdvice(basePackages = {"com/toy/overall_practice/api"})
public class ApiControllerAdvice {

    @ExceptionHandler
    public ResponseEntity<ExResult> userExHandler(NotFoundMemberException e) {
        log.error("[NotFoundMemberException] ex", e);
        ExResult exResult = new ExResult("400", e.getMessage());
        return ResponseEntity.status(HttpServletResponse.SC_BAD_REQUEST).body(exResult);
    }

    @ExceptionHandler
    public ResponseEntity<ExResult> exHandler(Exception e) {
        log.error("[Exception] ex", e);
        ExResult exResult = new ExResult("500", "INTERNAL_SERVER_ERROR");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exResult);
    }
}
