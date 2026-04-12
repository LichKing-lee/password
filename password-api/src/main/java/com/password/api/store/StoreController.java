package com.password.api.store;

import com.password.api.store.dto.StoreCreateRequest;
import com.password.api.store.dto.StoreCreateResponse;
import com.password.api.store.dto.StoreSearchRequest;
import com.password.api.store.dto.StoreSearchResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/stores")
public class StoreController {

    private final StoreService storeService;

    public StoreController(StoreService storeService) {
        this.storeService = storeService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public StoreCreateResponse create(@RequestBody StoreCreateRequest request) {
        return storeService.create(request);
    }

    @GetMapping
    public StoreSearchResponse search(
            @RequestParam(required = false) BigDecimal minLat,
            @RequestParam(required = false) BigDecimal maxLat,
            @RequestParam(required = false) BigDecimal minLng,
            @RequestParam(required = false) BigDecimal maxLng,
            @RequestParam(required = false) Integer limit
    ) {
        return storeService.search(StoreSearchRequest.of(minLat, maxLat, minLng, maxLng, limit));
    }

    @DeleteMapping("/{storeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long storeId) {
        storeService.delete(storeId);
    }
}
