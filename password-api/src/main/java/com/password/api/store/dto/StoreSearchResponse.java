package com.password.api.store.dto;

import com.password.domain.store.Store;

import java.math.BigDecimal;
import java.util.List;

public record StoreSearchResponse(List<StoreItem> stores) {

    public static StoreSearchResponse from(List<Store> stores) {
        return new StoreSearchResponse(stores.stream().map(StoreItem::from).toList());
    }

    public record StoreItem(
            Long storeId,
            String name,
            String address,
            BigDecimal latitude,
            BigDecimal longitude
    ) {
        public static StoreItem from(Store store) {
            return new StoreItem(
                    store.getId(),
                    store.getName(),
                    store.getAddress(),
                    store.getLatitude(),
                    store.getLongitude()
            );
        }
    }
}
