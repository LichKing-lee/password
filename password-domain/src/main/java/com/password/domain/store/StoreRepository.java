package com.password.domain.store;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface StoreRepository extends JpaRepository<Store, Long> {

    Optional<Store> findByNameAndLatitudeAndLongitude(String name, BigDecimal latitude, BigDecimal longitude);

    @Query("""
            select s from Store s
            where s.latitude between :minLat and :maxLat
              and s.longitude between :minLng and :maxLng
            order by s.id asc
            """)
    List<Store> findInBoundingBox(
            @Param("minLat") BigDecimal minLat,
            @Param("maxLat") BigDecimal maxLat,
            @Param("minLng") BigDecimal minLng,
            @Param("maxLng") BigDecimal maxLng,
            Pageable pageable
    );
}
