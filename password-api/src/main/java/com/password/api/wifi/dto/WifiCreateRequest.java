package com.password.api.wifi.dto;

import java.math.BigDecimal;

public record WifiCreateRequest(
        StoreRequest store,
        String ssid,
        boolean open,
        String password
) {

    public record StoreRequest(
            String naverPlaceId,
            String name,
            String address,
            BigDecimal latitude,
            BigDecimal longitude
    ) {
    }
}
