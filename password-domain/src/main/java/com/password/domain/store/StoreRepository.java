package com.password.domain.store;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StoreRepository extends JpaRepository<Store, Long> {

    Optional<Store> findByNameAndLatitudeAndLongitude(String name, java.math.BigDecimal latitude, java.math.BigDecimal longitude);
}
