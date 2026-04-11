package com.password.api.store.dto;

import com.password.domain.store.Store;

import java.math.BigDecimal;

public record StoreCreateResponse(
        Long storeId,
        String name,
        String address,
        BigDecimal latitude,
        BigDecimal longitude
) {
    public StoreCreateResponse(Store store) {
        this(
                store.getId(),
                store.getName(),
                store.getAddress(),
                store.getLatitude(),
                store.getLongitude()
        );
    }
}
