package com.password.api.wifi.dto;

import java.time.LocalDateTime;

public record WifiCreateResponse(
        Long wifiId,
        StoreResponse store,
        String ssid,
        String password,
        boolean open,
        LocalDateTime createdAt
) {

    public record StoreResponse(
            Long storeId,
            String name
    ) {
    }
}
