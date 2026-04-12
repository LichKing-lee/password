package com.password.api.store;

import com.password.api.PasswordMvcTest;
import com.password.domain.store.Store;
import com.password.domain.store.StoreRepository;
import com.password.domain.wifi.Wifi;
import com.password.domain.wifi.WifiRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.assertj.MvcTestResult;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@PasswordMvcTest
class StoreControllerTest {

    @Autowired
    private MockMvcTester mockMvc;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private WifiRepository wifiRepository;

    @Test
    void 상점을_등록한다() {
        // arrange
        String request = """
                {
                  "name": "이디야커피 선릉점",
                  "address": "서울시 강남구 선릉로 789",
                  "latitude": 37.5045000,
                  "longitude": 127.0490000
                }
                """;

        // act
        MvcTestResult result = mockMvc.post().uri("/api/stores")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request)
                .exchange();

        // assert
        assertThat(result).hasStatus(201);
        assertThat(result).bodyJson()
                .extractingPath("$.storeId").asNumber().isNotNull();
        assertThat(result).bodyJson()
                .extractingPath("$.name").asString().isEqualTo("이디야커피 선릉점");
        assertThat(result).bodyJson()
                .extractingPath("$.address").asString().isEqualTo("서울시 강남구 선릉로 789");
        assertThat(result).bodyJson()
                .extractingPath("$.latitude").asNumber().isNotNull();
        assertThat(result).bodyJson()
                .extractingPath("$.longitude").asNumber().isNotNull();
    }

    @Test
    void 이미_등록된_상점이면_기존_상점을_반환한다() throws Exception {
        // arrange
        String request = """
                {
                  "name": "스타벅스 역삼점",
                  "address": "서울시 강남구 역삼로 456",
                  "latitude": 37.5000000,
                  "longitude": 127.0360000
                }
                """;

        MvcTestResult first = mockMvc.post().uri("/api/stores")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request)
                .exchange();

        // act
        MvcTestResult second = mockMvc.post().uri("/api/stores")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request)
                .exchange();

        // assert
        assertThat(first).hasStatus(201);
        assertThat(second).hasStatus(201);
        assertThat(second).bodyJson()
                .isEqualTo(first.getResponse().getContentAsString());
    }

    @Test
    void 상점을_삭제한다() {
        // arrange
        Store store = storeRepository.save(new Store(
                "할리스커피 강남점", "서울시 강남구 강남대로 200",
                new BigDecimal("37.4970000"), new BigDecimal("127.0280000")
        ));
        wifiRepository.save(Wifi.secured(store, "HOLLYS_5G", "hollys1234"));
        wifiRepository.save(Wifi.open(store, "HOLLYS_Free"));

        // act
        MvcTestResult result = mockMvc.delete()
                .uri("/api/stores/{storeId}", store.getId())
                .exchange();

        // assert
        assertThat(result).hasStatus(204);
        assertThat(storeRepository.findById(store.getId())).isEmpty();
        assertThat(wifiRepository.findByStoreId(store.getId())).isEmpty();
    }

    @Test
    void 존재하지_않는_상점을_삭제하면_예외가_발생한다() {
        // act
        MvcTestResult result = mockMvc.delete()
                .uri("/api/stores/{storeId}", 999999L)
                .exchange();

        // assert
        assertThat(result).hasStatus(400);
        assertThat(result).bodyJson()
                .extractingPath("$.message").asString().isEqualTo("존재하지 않는 상점입니다.");
    }
}
