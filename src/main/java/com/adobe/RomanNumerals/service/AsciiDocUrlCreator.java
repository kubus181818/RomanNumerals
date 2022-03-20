package com.adobe.RomanNumerals.service;

import com.adobe.RomanNumerals.exception.ExceptionCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AsciiDocUrlCreator {
    @Value("${doc.url}")
    private String docUrl;

    public String getTroubleshootingUrl(ExceptionCode exceptionCode) {
        return docUrl + "/#code_" + exceptionCode.code;
    }
}
