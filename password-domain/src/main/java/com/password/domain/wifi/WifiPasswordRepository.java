package com.password.domain.wifi;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WifiPasswordRepository extends JpaRepository<WifiPassword, Long> {

    @Query("SELECT w FROM WifiPassword w JOIN FETCH w.store WHERE w.store.id = :storeId")
    List<WifiPassword> findByStoreId(@Param("storeId") Long storeId);
}
