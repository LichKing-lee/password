package com.password.domain.wifi;

import com.password.domain.store.Store;
import com.password.domain.store.StoreRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class WifiPasswordRepositoryTest {

    @Autowired
    private WifiPasswordRepository wifiPasswordRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Test
    void 와이파이_비밀번호를_저장하고_조회한다() {
        // arrange
        Store store = storeRepository.save(new Store(
                "naver-place-789",
                "이디야커피 선릉점",
                "서울시 강남구 선릉로 789",
                new BigDecimal("37.5045000"),
                new BigDecimal("127.0490000"),
                "카페"
        ));
        WifiPassword wifiPassword = new WifiPassword(store, "EDIYA_5G", "ediya1234");

        // act
        WifiPassword saved = wifiPasswordRepository.save(wifiPassword);
        WifiPassword found = wifiPasswordRepository.findById(saved.getId()).orElseThrow();

        // assert
        assertThat(found.getSsid()).isEqualTo("EDIYA_5G");
        assertThat(found.getPassword()).isEqualTo("ediya1234");
        assertThat(found.getCreatedAt()).isNotNull();
    }

    @Test
    void 상점ID로_와이파이_비밀번호_목록을_조회한다() {
        // arrange
        Store store = storeRepository.save(new Store(
                "naver-place-789",
                "이디야커피 선릉점",
                "서울시 강남구 선릉로 789",
                new BigDecimal("37.5045000"),
                new BigDecimal("127.0490000"),
                "카페"
        ));
        wifiPasswordRepository.save(new WifiPassword(store, "EDIYA_2G", "ediya1234"));
        wifiPasswordRepository.save(new WifiPassword(store, "EDIYA_5G", "ediya5678"));

        // act
        List<WifiPassword> passwords = wifiPasswordRepository.findByStoreId(store.getId());

        // assert
        assertThat(passwords).hasSize(2);
    }

    @Test
    void 와이파이_비밀번호를_수정한다() {
        // arrange
        Store store = storeRepository.save(new Store(
                "naver-place-789",
                "이디야커피 선릉점",
                "서울시 강남구 선릉로 789",
                new BigDecimal("37.5045000"),
                new BigDecimal("127.0490000"),
                "카페"
        ));
        WifiPassword wifiPassword = wifiPasswordRepository.save(
                new WifiPassword(store, "EDIYA_5G", "old_password")
        );

        // act
        wifiPassword.updatePassword("new_password");
        wifiPasswordRepository.flush();

        // assert
        WifiPassword found = wifiPasswordRepository.findById(wifiPassword.getId()).orElseThrow();
        assertThat(found.getPassword()).isEqualTo("new_password");
        assertThat(found.getUpdatedAt()).isNotNull();
    }
}
