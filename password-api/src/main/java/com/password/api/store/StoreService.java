package com.password.api.store;

import com.password.api.exception.StoreNotFoundException;
import com.password.api.store.dto.StoreCreateRequest;
import com.password.api.store.dto.StoreCreateResponse;
import com.password.api.store.dto.StoreSearchRequest;
import com.password.api.store.dto.StoreSearchResponse;
import com.password.domain.store.Store;
import com.password.domain.store.StoreRepository;
import com.password.domain.wifi.WifiRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StoreService {

    private final StoreRepository storeRepository;
    private final WifiRepository wifiRepository;

    public StoreService(StoreRepository storeRepository, WifiRepository wifiRepository) {
        this.storeRepository = storeRepository;
        this.wifiRepository = wifiRepository;
    }

    @Transactional
    public StoreCreateResponse create(StoreCreateRequest request) {
        Store store = storeRepository.findByNameAndLatitudeAndLongitude(request.name(), request.latitude(), request.longitude())
                .orElseGet(() -> storeRepository.save(request.toEntity()));

        return new StoreCreateResponse(store);
    }

    @Transactional(readOnly = true)
    public StoreSearchResponse search(StoreSearchRequest request) {
        List<Store> stores = storeRepository.findInBoundingBox(
                request.minLat(), request.maxLat(),
                request.minLng(), request.maxLng(),
                PageRequest.of(0, request.limit())
        );
        return StoreSearchResponse.from(stores);
    }

    @Transactional
    public void delete(Long storeId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new StoreNotFoundException(storeId));

        wifiRepository.deleteByStoreId(storeId);
        storeRepository.delete(store);
    }
}
