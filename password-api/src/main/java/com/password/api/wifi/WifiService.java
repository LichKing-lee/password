package com.password.api.wifi;

import com.password.api.wifi.dto.WifiCreateRequest;
import com.password.api.wifi.dto.WifiCreateResponse;
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
        Store store = storeRepository.findByNaverPlaceId(request.store().naverPlaceId())
                .orElseGet(() -> storeRepository.save(new Store(
                        request.store().naverPlaceId(),
                        request.store().name(),
                        request.store().address(),
                        request.store().latitude(),
                        request.store().longitude()
                )));

        String password = request.open() ? null : request.password();
        Wifi wifi = wifiRepository.save(
                password == null ? new Wifi(store, request.ssid()) : new Wifi(store, request.ssid(), password)
        );

        return new WifiCreateResponse(
                wifi.getId(),
                new WifiCreateResponse.StoreResponse(store.getId(), store.getName()),
                wifi.getSsid(),
                wifi.getPassword(),
                wifi.isOpen(),
                wifi.getCreatedAt()
        );
    }
}
