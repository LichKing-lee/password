package com.password.api.wifi.dto;

import com.password.domain.wifi.Wifi;

import java.time.LocalDateTime;

public record WifiResponse(
        Long wifiId,
        Long storeId,
        String ssid,
        String password,
        boolean open,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public WifiResponse(Wifi wifi) {
        this(
                wifi.getId(),
                wifi.getStore().getId(),
                wifi.getSsid(),
                wifi.getPassword(),
                wifi.isOpen(),
                wifi.getCreatedAt(),
                wifi.getUpdatedAt()
        );
    }
}
