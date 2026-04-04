package com.password.api.wifi;

import com.password.api.exception.StoreNotFoundException;
import com.password.api.wifi.dto.WifiCreateRequest;
import com.password.api.wifi.dto.WifiCreateResponse;
import com.password.api.wifi.dto.WifiListResponse;
import com.password.domain.store.Store;
import com.password.domain.store.StoreRepository;
import com.password.domain.wifi.Wifi;
import com.password.domain.wifi.WifiRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WifiService {

    private final StoreRepository storeRepository;
    private final WifiRepository wifiRepository;

    public WifiService(StoreRepository storeRepository, WifiRepository wifiRepository) {
        this.storeRepository = storeRepository;
        this.wifiRepository = wifiRepository;
    }

    @Transactional
    public WifiCreateResponse create(WifiCreateRequest request) {
        Store store = storeRepository.findById(request.storeId())
                .orElseThrow(() -> new StoreNotFoundException(request.storeId()));

        Wifi wifi = wifiRepository.save(request.toEntity(store));

        return new WifiCreateResponse(wifi);
    }

    @Transactional(readOnly = true)
    public WifiListResponse searchWifis(Long storeId) {
        var wifis = wifiRepository.findByStoreId(storeId).stream()
                .map(WifiListResponse.WifiItem::new)
                .toList();
        return new WifiListResponse(wifis);
    }
}
