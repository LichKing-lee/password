package com.password.domain.wifi;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WifiRepository extends JpaRepository<Wifi, Long> {

    @Query("SELECT w FROM Wifi w JOIN FETCH w.store WHERE w.store.id = :storeId")
    List<Wifi> findByStoreId(@Param("storeId") Long storeId);
}
