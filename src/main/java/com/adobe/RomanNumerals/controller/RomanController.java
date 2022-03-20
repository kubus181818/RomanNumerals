package com.adobe.RomanNumerals.controller;

import com.adobe.RomanNumerals.dto.ErrorResponse;
import com.adobe.RomanNumerals.dto.RomanNumeralResponse;
import com.adobe.RomanNumerals.exception.ExceptionCode;
import com.adobe.RomanNumerals.service.AsciiDocUrlCreator;
import com.adobe.RomanNumerals.service.RomanConverter;
import com.adobe.RomanNumerals.tracing.TracingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ValidationException;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@RestController
@RequestMapping("/romannumeral")
@Slf4j
@Validated
public class RomanController {

    public static final int MIN_NUMBER = 1;
    public static final int MAX_NUMBER = 255;

    private final TracingService tracingService;
    private final RomanConverter romanConverter;
    private final AsciiDocUrlCreator asciiDocUrlCreator;

    public RomanController(TracingService tracingService, RomanConverter romanConverter, AsciiDocUrlCreator asciiDocUrlCreator) {
        this.tracingService = tracingService;
        this.romanConverter = romanConverter;
        this.asciiDocUrlCreator = asciiDocUrlCreator;
    }

    @GetMapping
    public ResponseEntity<?> getRoman(@RequestParam @Min(MIN_NUMBER) @Max(MAX_NUMBER) int query) {
        return ResponseEntity.ok(new RomanNumeralResponse(String.valueOf(query), romanConverter.convertFromArabic(query)));
    }

    @ExceptionHandler(value = {ValidationException.class, MethodArgumentTypeMismatchException.class})
    public ResponseEntity<ErrorResponse> handleValidationException(Exception exc) {
        log.warn("Client exception occured", exc);
        ExceptionCode exceptionCode = ExceptionCode.VALIDATION;
        return ResponseEntity.badRequest().body(new ErrorResponse(exceptionCode.code,
                exc.getMessage(), asciiDocUrlCreator.getTroubleshootingUrl(exceptionCode),
                tracingService.getCurrentTraceId()));
    }
}
