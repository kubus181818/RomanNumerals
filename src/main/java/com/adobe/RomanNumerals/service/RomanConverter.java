package com.adobe.RomanNumerals.service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.cache.GuavaCacheMetrics;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.sleuth.annotation.NewSpan;
import org.springframework.stereotype.Service;

import java.util.TreeMap;

@Service
@Slf4j
public class RomanConverter {

    private final static TreeMap<Integer, String> romanSymbols = new TreeMap<>();
    private final MeterRegistry meterRegistry;
    private final LoadingCache<Integer, String> cache = CacheBuilder.newBuilder()
            .recordStats()
            .maximumSize(255)
            .build(new CacheLoader<>() {
                @Override
                public String load(Integer number) throws Exception {
                    log.info("Populating cache. arabic={}", number);
                    String romanNumber = computeRoman(number);
                    log.info("Cache populated. arabic={} roman={}", number, romanNumber);
                    return romanNumber;
                }
            });

    static {
        romanSymbols.put(100, "C");
        romanSymbols.put(90, "XC");
        romanSymbols.put(50, "L");
        romanSymbols.put(40, "XL");
        romanSymbols.put(10, "X");
        romanSymbols.put(9, "IX");
        romanSymbols.put(5, "V");
        romanSymbols.put(4, "IV");
        romanSymbols.put(1, "I");
    }

    public RomanConverter(MeterRegistry meterRegistry) {
        GuavaCacheMetrics.monitor(meterRegistry, cache, "Roman cache");
        this.meterRegistry = meterRegistry;
    }

    /***
    Converts given integer into roman representation. Acceptable values are within 1-255 range.
     @param number Number between 1 and 255
     @return Roman number
     ***/
    @NewSpan("romanCalculation")
    public String convertFromArabic(int number) {
        if(number < 1) {
            throw new IllegalArgumentException("Provided number: " + number + " is lower than 1");
        }
        if(number > 255) {
            throw new IllegalArgumentException("Provided number: " + number + " is greater than 255");
        }
        meterRegistry.counter("roman.count", "number", String.valueOf(number)).increment();
        return cache.getUnchecked(number);
    }

    private String computeRoman(int number) {
        int closestNumber =  romanSymbols.floorKey(number);
        return number == closestNumber  ?
                romanSymbols.get(number) :
                romanSymbols.get(closestNumber) + computeRoman(number - closestNumber);
    }
}
