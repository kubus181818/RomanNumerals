package com.adobe.RomanNumerals.dto;

import lombok.Value;

@Value
public class ErrorResponse {
    int code;
    String message;
    String doc;
    String traceId;
}
