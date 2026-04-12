package com.password.domain.store;

import com.password.domain.PasswordRepositoryTest;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@PasswordRepositoryTest
class StoreRepositoryTest {

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void 상점을_저장하고_ID로_조회한다() {
        // arrange
        Store store = new Store(
                "스타벅스 강남점",
                "서울시 강남구 테헤란로 123",
                new BigDecimal("37.4979462"),
                new BigDecimal("127.0276368")
        );
        Store saved = storeRepository.save(store);
        entityManager.flush();
        entityManager.clear();

        // act
        Optional<Store> found = storeRepository.findById(saved.getId());

        // assert
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("스타벅스 강남점");
    }

    @Test
    void 이름과_위경도로_조회한다() {
        // arrange
        Store store = new Store(
                "투썸플레이스 역삼점",
                "서울시 강남구 역삼동 456",
                new BigDecimal("37.5000000"),
                new BigDecimal("127.0360000")
        );
        storeRepository.save(store);
        entityManager.flush();
        entityManager.clear();

        // act
        Optional<Store> found = storeRepository.findByNameAndLatitudeAndLongitude(
                "투썸플레이스 역삼점",
                new BigDecimal("37.5000000"),
                new BigDecimal("127.0360000")
        );

        // assert
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("투썸플레이스 역삼점");
    }

    @Test
    void 존재하지_않는_이름과_위경도로_조회하면_빈값을_반환한다() {
        // act
        Optional<Store> found = storeRepository.findByNameAndLatitudeAndLongitude(
                "존재하지않는상점",
                new BigDecimal("0.0000000"),
                new BigDecimal("0.0000000")
        );

        // assert
        assertThat(found).isEmpty();
    }
}
