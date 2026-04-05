package com.password.domain.wifi;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface WifiRepository extends JpaRepository<Wifi, Long> {

    @Query("SELECT w FROM Wifi w JOIN FETCH w.store WHERE w.store.id = :storeId")
    List<Wifi> findByStoreId(@Param("storeId") Long storeId);

    @Query("SELECT w FROM Wifi w JOIN FETCH w.store WHERE w.id = :wifiId")
    Optional<Wifi> findByIdWithStore(@Param("wifiId") Long wifiId);

    void deleteByStoreId(Long storeId);
}
