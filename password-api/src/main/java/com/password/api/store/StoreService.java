package com.password.api.store;

import com.password.api.store.dto.StoreCreateRequest;
import com.password.api.store.dto.StoreCreateResponse;
import com.password.domain.store.Store;
import com.password.domain.store.StoreRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StoreService {

    private final StoreRepository storeRepository;

    public StoreService(StoreRepository storeRepository) {
        this.storeRepository = storeRepository;
    }

    @Transactional
    public StoreCreateResponse create(StoreCreateRequest request) {
        Store store = storeRepository.findByNaverPlaceId(request.naverPlaceId())
                .orElseGet(() -> storeRepository.save(request.toEntity()));

        return new StoreCreateResponse(store);
    }
}
