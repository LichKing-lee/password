package com.password.domain.store;

import com.password.domain.PasswordRepositoryTest;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.util.List;
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
    void BBox_범위_안의_상점만_조회한다() {
        // arrange
        Store inside = storeRepository.save(new Store(
                "안쪽 상점", "주소1",
                new BigDecimal("37.5000000"), new BigDecimal("127.0300000")
        ));
        storeRepository.save(new Store(
                "위도 밖", "주소2",
                new BigDecimal("37.6000000"), new BigDecimal("127.0300000")
        ));
        storeRepository.save(new Store(
                "경도 밖", "주소3",
                new BigDecimal("37.5000000"), new BigDecimal("126.9000000")
        ));
        entityManager.flush();
        entityManager.clear();

        // act
        List<Store> found = storeRepository.findInBoundingBox(
                new BigDecimal("37.4900000"), new BigDecimal("37.5100000"),
                new BigDecimal("127.0200000"), new BigDecimal("127.0500000"),
                PageRequest.of(0, 100)
        );

        // assert
        assertThat(found).hasSize(1);
        assertThat(found.get(0).getId()).isEqualTo(inside.getId());
    }

    @Test
    void BBox_조회시_limit으로_결과_개수를_제한한다() {
        // arrange
        for (int i = 0; i < 5; i++) {
            storeRepository.save(new Store(
                    "상점" + i, "주소" + i,
                    new BigDecimal("37.5000000").add(new BigDecimal("0.000" + i + "00")),
                    new BigDecimal("127.0300000")
            ));
        }
        entityManager.flush();
        entityManager.clear();

        // act
        List<Store> found = storeRepository.findInBoundingBox(
                new BigDecimal("37.4900000"), new BigDecimal("37.5100000"),
                new BigDecimal("127.0200000"), new BigDecimal("127.0400000"),
                PageRequest.of(0, 2)
        );

        // assert
        assertThat(found).hasSize(2);
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
