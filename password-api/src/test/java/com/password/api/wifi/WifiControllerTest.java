package com.password.api.wifi;

import com.password.domain.store.Store;
import com.password.domain.store.StoreRepository;
import com.password.domain.wifi.Wifi;
import com.password.domain.wifi.WifiRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.assertj.MvcTestResult;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class WifiControllerTest {

    @Autowired
    private MockMvcTester mockMvc;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private WifiRepository wifiRepository;

    @Test
    void 와이파이를_등록한다() {
        // arrange
        Store store = storeRepository.save(new Store(
                "naver-place-wifi-1", "이디야커피 선릉점", "서울시 강남구 선릉로 789",
                new BigDecimal("37.5045000"), new BigDecimal("127.0490000")
        ));

        String request = """
                {
                  "ssid": "EDIYA_5G",
                  "open": false,
                  "password": "ediya1234"
                }
                """;

        // act
        MvcTestResult result = mockMvc.post()
                .uri("/api/stores/{storeId}/wifis", store.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(request)
                .exchange();

        // assert
        assertThat(result).hasStatus(201);
        assertThat(result).bodyJson()
                .extractingPath("$.wifiId").asNumber().isNotNull();
        assertThat(result).bodyJson()
                .extractingPath("$.storeId").asNumber().isNotNull();
        assertThat(result).bodyJson()
                .extractingPath("$.ssid").asString().isEqualTo("EDIYA_5G");
        assertThat(result).bodyJson()
                .extractingPath("$.password").asString().isEqualTo("ediya1234");
        assertThat(result).bodyJson()
                .extractingPath("$.open").asBoolean().isFalse();
        assertThat(result).bodyJson()
                .extractingPath("$.createdAt").asString().isNotEmpty();
    }

    @Test
    void 개방형_와이파이를_등록한다() {
        // arrange
        Store store = storeRepository.save(new Store(
                "naver-place-wifi-2", "스타벅스 역삼점", "서울시 강남구 역삼로 456",
                new BigDecimal("37.5000000"), new BigDecimal("127.0360000")
        ));

        String request = """
                {
                  "ssid": "Starbucks_Free",
                  "open": true
                }
                """;

        // act
        MvcTestResult result = mockMvc.post()
                .uri("/api/stores/{storeId}/wifis", store.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(request)
                .exchange();

        // assert
        assertThat(result).hasStatus(201);
        assertThat(result).bodyJson()
                .extractingPath("$.storeId").asNumber().isNotNull();
        assertThat(result).bodyJson()
                .extractingPath("$.ssid").asString().isEqualTo("Starbucks_Free");
        assertThat(result).bodyJson()
                .extractingPath("$.password").isNull();
        assertThat(result).bodyJson()
                .extractingPath("$.open").asBoolean().isTrue();
        assertThat(result).bodyJson()
                .extractingPath("$.createdAt").asString().isNotEmpty();
    }

    @Test
    void 와이파이_비밀번호를_수정한다() {
        // arrange
        Store store = storeRepository.save(new Store(
                "naver-place-wifi-update-1", "이디야커피 선릉점", "서울시 강남구 선릉로 789",
                new BigDecimal("37.5045000"), new BigDecimal("127.0490000")
        ));
        Wifi wifi = wifiRepository.save(Wifi.secured(store, "EDIYA_5G", "ediya1234"));

        String request = """
                {
                  "open": false,
                  "password": "newPassword123"
                }
                """;

        // act
        MvcTestResult result = mockMvc.patch()
                .uri("/api/stores/{storeId}/wifis/{wifiId}", store.getId(), wifi.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(request)
                .exchange();

        // assert
        assertThat(result).hasStatus(200);
        assertThat(result).bodyJson()
                .extractingPath("$.wifiId").asNumber().isEqualTo(wifi.getId().intValue());
        assertThat(result).bodyJson()
                .extractingPath("$.storeId").asNumber().isEqualTo(store.getId().intValue());
        assertThat(result).bodyJson()
                .extractingPath("$.ssid").asString().isEqualTo("EDIYA_5G");
        assertThat(result).bodyJson()
                .extractingPath("$.password").asString().isEqualTo("newPassword123");
        assertThat(result).bodyJson()
                .extractingPath("$.open").asBoolean().isFalse();
        assertThat(result).bodyJson()
                .extractingPath("$.createdAt").asString().isNotEmpty();
        assertThat(result).bodyJson()
                .extractingPath("$.updatedAt").asString().isNotEmpty();
    }

    @Test
    void 와이파이를_개방형으로_변경한다() {
        // arrange
        Store store = storeRepository.save(new Store(
                "naver-place-wifi-update-2", "스타벅스 역삼점", "서울시 강남구 역삼로 456",
                new BigDecimal("37.5000000"), new BigDecimal("127.0360000")
        ));
        Wifi wifi = wifiRepository.save(Wifi.secured(store, "Starbucks_5G", "starbucks1234"));

        String request = """
                {
                  "open": true
                }
                """;

        // act
        MvcTestResult result = mockMvc.patch()
                .uri("/api/stores/{storeId}/wifis/{wifiId}", store.getId(), wifi.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(request)
                .exchange();

        // assert
        assertThat(result).hasStatus(200);
        assertThat(result).bodyJson()
                .extractingPath("$.wifiId").asNumber().isEqualTo(wifi.getId().intValue());
        assertThat(result).bodyJson()
                .extractingPath("$.password").isNull();
        assertThat(result).bodyJson()
                .extractingPath("$.open").asBoolean().isTrue();
        assertThat(result).bodyJson()
                .extractingPath("$.updatedAt").asString().isNotEmpty();
    }

    @Test
    void 존재하지_않는_와이파이를_수정하면_예외가_발생한다() {
        // arrange
        String request = """
                {
                  "open": false,
                  "password": "newPassword123"
                }
                """;

        // act
        MvcTestResult result = mockMvc.patch()
                .uri("/api/stores/{storeId}/wifis/{wifiId}", 999999L, 999999L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(request)
                .exchange();

        // assert
        assertThat(result).hasStatus(400);
        assertThat(result).bodyJson()
                .extractingPath("$.message").asString().isEqualTo("존재하지 않는 와이파이입니다.");
    }

    @Test
    void 상점의_와이파이_목록을_조회한다() {
        // arrange
        Store store = storeRepository.save(new Store(
                "naver-place-wifi-3", "투썸플레이스 강남점", "서울시 강남구 강남대로 123",
                new BigDecimal("37.4980000"), new BigDecimal("127.0280000")
        ));
        wifiRepository.save(Wifi.secured(store, "TWOSOME_5G", "twosome1234"));
        wifiRepository.save(Wifi.open(store, "TWOSOME_Free"));

        // act
        MvcTestResult result = mockMvc.get()
                .uri("/api/stores/{storeId}/wifis", store.getId())
                .exchange();

        // assert
        assertThat(result).hasStatus(200);
        assertThat(result).bodyJson()
                .extractingPath("$.wifis.length()").asNumber().isEqualTo(2);
        assertThat(result).bodyJson()
                .extractingPath("$.wifis[0].wifiId").asNumber().isNotNull();
        assertThat(result).bodyJson()
                .extractingPath("$.wifis[0].storeId").asNumber().isNotNull();
        assertThat(result).bodyJson()
                .extractingPath("$.wifis[0].ssid").asString().isEqualTo("TWOSOME_5G");
        assertThat(result).bodyJson()
                .extractingPath("$.wifis[0].password").asString().isEqualTo("twosome1234");
        assertThat(result).bodyJson()
                .extractingPath("$.wifis[0].open").asBoolean().isFalse();
        assertThat(result).bodyJson()
                .extractingPath("$.wifis[0].createdAt").asString().isNotEmpty();
        assertThat(result).bodyJson()
                .extractingPath("$.wifis[1].ssid").asString().isEqualTo("TWOSOME_Free");
        assertThat(result).bodyJson()
                .extractingPath("$.wifis[1].open").asBoolean().isTrue();
    }
}
