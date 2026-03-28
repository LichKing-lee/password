package com.password.domain.wifi;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WifiPasswordRepository extends JpaRepository<WifiPassword, Long> {

    List<WifiPassword> findByStoreId(Long storeId);
}
