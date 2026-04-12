package com.password.api.store.dto;

import com.password.api.exception.InvalidStoreSearchRangeException;

import java.math.BigDecimal;

public record StoreSearchRequest(
        BigDecimal minLat,
        BigDecimal maxLat,
        BigDecimal minLng,
        BigDecimal maxLng,
        Integer limit
) {
    private static final BigDecimal MAX_SPAN = new BigDecimal("0.5");
    private static final int DEFAULT_LIMIT = 100;
    private static final int MAX_LIMIT = 500;

    public static StoreSearchRequest of(BigDecimal minLat, BigDecimal maxLat,
                                        BigDecimal minLng, BigDecimal maxLng,
                                        Integer limit) {
        if (minLat == null || maxLat == null || minLng == null || maxLng == null) {
            throw new InvalidStoreSearchRangeException("missing parameter");
        }
        if (minLat.compareTo(maxLat) > 0 || minLng.compareTo(maxLng) > 0) {
            throw new InvalidStoreSearchRangeException("min > max");
        }
        if (maxLat.subtract(minLat).compareTo(MAX_SPAN) > 0
                || maxLng.subtract(minLng).compareTo(MAX_SPAN) > 0) {
            throw new InvalidStoreSearchRangeException("span exceeds " + MAX_SPAN);
        }
        int resolvedLimit = limit == null ? DEFAULT_LIMIT : limit;
        if (resolvedLimit < 1 || resolvedLimit > MAX_LIMIT) {
            throw new InvalidStoreSearchRangeException("limit out of range: " + resolvedLimit);
        }
        return new StoreSearchRequest(minLat, maxLat, minLng, maxLng, resolvedLimit);
    }
}
