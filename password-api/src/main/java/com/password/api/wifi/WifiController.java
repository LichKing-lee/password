package com.password.api.wifi;

import com.password.api.wifi.dto.WifiCreateRequest;
import com.password.api.wifi.dto.WifiCreateResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/wifis")
public class WifiController {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public WifiCreateResponse create(@RequestBody WifiCreateRequest request) {
        return new WifiCreateResponse(
                1L,
                new WifiCreateResponse.StoreResponse(1L, request.store().name()),
                request.ssid(),
                request.open() ? null : request.password(),
                request.open(),
                LocalDateTime.now()
        );
    }
}
