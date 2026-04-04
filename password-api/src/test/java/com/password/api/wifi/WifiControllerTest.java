package com.password.api.wifi;

import com.password.domain.store.Store;
import com.password.domain.store.StoreRepository;
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

    @Test
    void 와이파이를_등록한다() {
        // arrange
        Store store = storeRepository.save(new Store(
                "naver-place-wifi-1", "이디야커피 선릉점", "서울시 강남구 선릉로 789",
                new BigDecimal("37.5045000"), new BigDecimal("127.0490000")
        ));

        String request = """
                {
                  "storeId": %d,
                  "ssid": "EDIYA_5G",
                  "open": false,
                  "password": "ediya1234"
                }
                """.formatted(store.getId());

        // act
        MvcTestResult result = mockMvc.post().uri("/api/wifis")
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
                  "storeId": %d,
                  "ssid": "Starbucks_Free",
                  "open": true
                }
                """.formatted(store.getId());

        // act
        MvcTestResult result = mockMvc.post().uri("/api/wifis")
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
}
