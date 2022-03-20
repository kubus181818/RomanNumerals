package com.adobe.RomanNumerals.controller;

import com.adobe.RomanNumerals.dto.ErrorResponse;
import com.adobe.RomanNumerals.exception.ExceptionCode;
import com.adobe.RomanNumerals.service.AsciiDocUrlCreator;
import com.adobe.RomanNumerals.tracing.TracingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class ExceptionController {

    private final TracingService tracingService;
    private final AsciiDocUrlCreator asciiDocUrlCreator;

    public ExceptionController(TracingService tracingService, AsciiDocUrlCreator asciiDocUrlCreator) {
        this.tracingService = tracingService;
        this.asciiDocUrlCreator = asciiDocUrlCreator;
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ErrorResponse> defaultErrorHandler(Exception exc) {
        log.error("Unhandled error occured", exc);
        ExceptionCode exceptionCode = ExceptionCode.INTERNAL;
        return ResponseEntity.internalServerError().body(new ErrorResponse(exceptionCode.code,
                "Unknown error", asciiDocUrlCreator.getTroubleshootingUrl(exceptionCode),
                tracingService.getCurrentTraceId()));
    }
}
