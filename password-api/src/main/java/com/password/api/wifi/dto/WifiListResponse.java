package com.password.api.wifi.dto;

import com.password.domain.wifi.Wifi;

import java.time.LocalDateTime;
import java.util.List;

public record WifiListResponse(List<WifiItem> wifis) {

    public record WifiItem(
            Long wifiId,
            Long storeId,
            String ssid,
            String password,
            boolean open,
            LocalDateTime createdAt
    ) {
        public WifiItem(Wifi wifi) {
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
}
