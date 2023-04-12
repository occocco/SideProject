package com.toy.overall_practice.exception.exhandler.advice;

import com.toy.overall_practice.exception.DuplicateMemberException;
import com.toy.overall_practice.exception.ForbiddenException;
import com.toy.overall_practice.exception.InsufficientFundsException;
import com.toy.overall_practice.exception.NotFoundMemberException;
import com.toy.overall_practice.exception.exhandler.ExResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletResponse;
import java.util.NoSuchElementException;

@Slf4j
@RestControllerAdvice
public class ApiControllerAdvice {

    @ExceptionHandler(NotFoundMemberException.class)
    public ResponseEntity<ExResult> NotFoundMemberExceptionHandler(NotFoundMemberException e) {
        log.error("[NotFoundMemberException]", e);
        ExResult exResult = new ExResult("404", e.getMessage());
        return ResponseEntity.status(HttpServletResponse.SC_BAD_REQUEST).body(exResult);
    }

    @ExceptionHandler(DuplicateMemberException.class)
    public ResponseEntity<ExResult> duplicateMemberExceptionHandler(DuplicateMemberException e) {
        log.error("[DuplicateMemberException]", e);
        ExResult exResult = new ExResult("409", e.getMessage());
        return ResponseEntity.status(HttpServletResponse.SC_CONFLICT).body(exResult);
    }

    @ExceptionHandler(InsufficientFundsException.class)
    public ResponseEntity<ExResult> insufficientFundsExceptionHandler(InsufficientFundsException e) {
        log.error("[InsufficientFundsException]", e);
        ExResult exResult = new ExResult("402", e.getMessage());
        return ResponseEntity.status(HttpServletResponse.SC_PAYMENT_REQUIRED).body(exResult);
    }
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ExResult> illegalArgumentExceptionHandler(IllegalArgumentException e) {
        log.error("[IllegalArgumentException]", e);
        ExResult exResult = new ExResult("400", e.getMessage());
        return ResponseEntity.status(HttpServletResponse.SC_BAD_REQUEST).body(exResult);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ExResult> forbiddenExceptionHandler(ForbiddenException e) {
        log.error("[ForbiddenException]", e);
        ExResult exResult = new ExResult("403", e.getMessage());
        return ResponseEntity.status(HttpServletResponse.SC_FORBIDDEN).body(exResult);
    }
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ExResult> noSuchElementExceptionHandler(NoSuchElementException e) {
        log.error("[NoSuchElementException]", e);
        ExResult exResult = new ExResult("400", e.getMessage());
        return ResponseEntity.status(HttpServletResponse.SC_BAD_REQUEST).body(exResult);
    }

    @ExceptionHandler(EmptyResultDataAccessException.class)
    public ResponseEntity<ExResult> emptyResultDataAccessExceptionHandler(EmptyResultDataAccessException e) {
        log.error("[EmptyResultDataAccessException]", e);
        ExResult exResult = new ExResult("404", e.getMessage());
        return ResponseEntity.status(HttpServletResponse.SC_NOT_FOUND).body(exResult);
    }

    @ExceptionHandler
    public ResponseEntity<ExResult> exHandler(Exception e) {
        log.error("[Exception] ex", e);
        ExResult exResult = new ExResult("500", "INTERNAL_SERVER_ERROR");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exResult);
    }
}
