package com.password.api.wifi;

import com.password.api.wifi.dto.WifiCreateRequest;
import com.password.api.wifi.dto.WifiCreateResponse;
import com.password.api.wifi.dto.WifiListResponse;
import com.password.api.wifi.dto.WifiResponse;
import com.password.api.wifi.dto.WifiUpdateRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class WifiController {

    private final WifiService wifiService;

    public WifiController(WifiService wifiService) {
        this.wifiService = wifiService;
    }

    @PostMapping("/stores/{storeId}/wifis")
    @ResponseStatus(HttpStatus.CREATED)
    public WifiCreateResponse create(@PathVariable Long storeId, @RequestBody WifiCreateRequest request) {
        return wifiService.create(storeId, request);
    }

    @GetMapping("/stores/{storeId}/wifis")
    public WifiListResponse searchWifis(@PathVariable Long storeId) {
        return wifiService.searchWifis(storeId);
    }

    @PatchMapping("/stores/{storeId}/wifis/{wifiId}")
    public WifiResponse update(@PathVariable Long storeId, @PathVariable Long wifiId, @RequestBody WifiUpdateRequest request) {
        return wifiService.update(wifiId, request);
    }
}
