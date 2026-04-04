package com.password.api.wifi.dto;

import com.password.domain.wifi.Wifi;

import java.time.LocalDateTime;

public record WifiCreateResponse(
        Long wifiId,
        Long storeId,
        String ssid,
        String password,
        boolean open,
        LocalDateTime createdAt
) {
    public WifiCreateResponse(Wifi wifi) {
        this(
                wifi.getId(),
                wifi.getStore().getId(),
                wifi.getSsid(),
                wifi.getPassword(),
                wifi.isOpen(),
                wifi.getCreatedAt()
        );
    }
}
