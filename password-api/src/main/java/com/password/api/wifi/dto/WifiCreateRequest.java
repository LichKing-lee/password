package com.password.api.wifi.dto;

import com.password.domain.store.Store;
import com.password.domain.wifi.Wifi;

public record WifiCreateRequest(
        Long storeId,
        String ssid,
        boolean open,
        String password
) {
    public Wifi toEntity(Store store) {
        return open ? Wifi.open(store, ssid) : Wifi.secured(store, ssid, password);
    }
}
