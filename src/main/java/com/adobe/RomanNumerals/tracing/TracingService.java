package com.adobe.RomanNumerals.tracing;

import org.springframework.cloud.sleuth.Tracer;
import org.springframework.stereotype.Component;

@Component
public class TracingService {

    private final Tracer tracer;

    public TracingService(Tracer tracer) {
        this.tracer = tracer;
    }

    public String getCurrentTraceId() {
        return tracer.currentSpan() == null ? "undefined" : tracer.currentSpan().context().traceId();
    }
}
