package com.password.domain.wifi;

import com.password.domain.store.Store;
import com.password.domain.store.StoreRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class WifiRepositoryTest {

    @Autowired
    private WifiRepository wifiRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void 와이파이를_저장하고_조회한다() {
        // arrange
        Store store = storeRepository.save(new Store(
                "naver-place-789",
                "이디야커피 선릉점",
                "서울시 강남구 선릉로 789",
                new BigDecimal("37.5045000"),
                new BigDecimal("127.0490000")
        ));
        Wifi saved = wifiRepository.save(new Wifi(store, "EDIYA_5G", "ediya1234"));
        entityManager.flush();
        entityManager.clear();

        // act
        Wifi found = wifiRepository.findById(saved.getId()).orElseThrow();

        // assert
        assertThat(found.getSsid()).isEqualTo("EDIYA_5G");
        assertThat(found.getPassword()).isEqualTo("ediya1234");
        assertThat(found.getCreatedAt()).isNotNull();
    }

    @Test
    void 상점ID로_와이파이_목록을_조회한다() {
        // arrange
        Store store = storeRepository.save(new Store(
                "naver-place-789",
                "이디야커피 선릉점",
                "서울시 강남구 선릉로 789",
                new BigDecimal("37.5045000"),
                new BigDecimal("127.0490000")
        ));
        wifiRepository.save(new Wifi(store, "EDIYA_2G", "ediya1234"));
        wifiRepository.save(new Wifi(store, "EDIYA_5G", "ediya5678"));
        entityManager.flush();
        entityManager.clear();

        // act
        List<Wifi> wifiList = wifiRepository.findByStoreId(store.getId());

        // assert
        assertThat(wifiList).hasSize(2);
        assertThat(wifiList).allSatisfy(wifi -> {
            assertThat(wifi.getStore()).isNotNull();
            assertThat(wifi.getStore().getName()).isEqualTo("이디야커피 선릉점");
            assertThat(wifi.getStore().getNaverPlaceId()).isEqualTo("naver-place-789");
        });
    }

    @Test
    void 개방형_와이파이를_저장하고_조회한다() {
        // arrange
        Store store = storeRepository.save(new Store(
                "naver-place-456",
                "스타벅스 역삼점",
                "서울시 강남구 역삼로 456",
                new BigDecimal("37.5000000"),
                new BigDecimal("127.0360000")
        ));
        Wifi saved = wifiRepository.save(new Wifi(store, "Starbucks_Free"));
        entityManager.flush();
        entityManager.clear();

        // act
        Wifi found = wifiRepository.findById(saved.getId()).orElseThrow();

        // assert
        assertThat(found.getSsid()).isEqualTo("Starbucks_Free");
        assertThat(found.getPassword()).isNull();
        assertThat(found.isOpen()).isTrue();
    }

    @Test
    void 와이파이_비밀번호를_수정한다() {
        // arrange
        Store store = storeRepository.save(new Store(
                "naver-place-789",
                "이디야커피 선릉점",
                "서울시 강남구 선릉로 789",
                new BigDecimal("37.5045000"),
                new BigDecimal("127.0490000")
        ));
        Wifi wifi = wifiRepository.save(
                new Wifi(store, "EDIYA_5G", "old_password")
        );

        // act
        wifi.updatePassword("new_password");
        entityManager.flush();
        entityManager.clear();

        // assert
        Wifi found = wifiRepository.findById(wifi.getId()).orElseThrow();
        assertThat(found.getPassword()).isEqualTo("new_password");
        assertThat(found.getUpdatedAt()).isNotNull();
    }
}
