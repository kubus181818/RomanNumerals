package com.adobe.RomanNumerals.exception;

public enum ExceptionCode {
    INTERNAL(1, "Internal error"),
    VALIDATION(2, "Validation error");

    public final int code;
    public final String message;

    ExceptionCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
