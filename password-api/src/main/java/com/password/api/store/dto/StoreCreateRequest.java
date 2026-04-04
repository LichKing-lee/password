package com.password.api.store.dto;

import com.password.domain.store.Store;

import java.math.BigDecimal;

public record StoreCreateRequest(
        String naverPlaceId,
        String name,
        String address,
        BigDecimal latitude,
        BigDecimal longitude
) {
    public Store toEntity() {
        return new Store(naverPlaceId, name, address, latitude, longitude);
    }
}
