package com.password.api.wifi.dto;

public record WifiUpdateRequest(
        boolean open,
        String password
) {
}
