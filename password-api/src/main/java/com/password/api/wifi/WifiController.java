package com.password.api.wifi;

import com.password.api.wifi.dto.WifiCreateRequest;
import com.password.api.wifi.dto.WifiCreateResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/wifis")
public class WifiController {

    private final WifiService wifiService;

    public WifiController(WifiService wifiService) {
        this.wifiService = wifiService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public WifiCreateResponse create(@RequestBody WifiCreateRequest request) {
        return wifiService.create(request);
    }
}
