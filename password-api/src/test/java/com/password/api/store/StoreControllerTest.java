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
    void 범위_안에_있는_상점만_조회한다() {
        // arrange
        Store inside1 = storeRepository.save(new Store(
                "이디야 강남점", "서울시 강남구 1",
                new BigDecimal("37.5000000"), new BigDecimal("127.0300000")
        ));
        Store inside2 = storeRepository.save(new Store(
                "스타벅스 강남점", "서울시 강남구 2",
                new BigDecimal("37.5050000"), new BigDecimal("127.0400000")
        ));
        storeRepository.save(new Store(
                "범위밖 상점", "서울시 종로구",
                new BigDecimal("37.5700000"), new BigDecimal("126.9800000")
        ));

        // act
        MvcTestResult result = mockMvc.get().uri("/api/stores"
                        + "?minLat=37.4900000&maxLat=37.5100000"
                        + "&minLng=127.0200000&maxLng=127.0500000")
                .exchange();

        // assert
        assertThat(result).hasStatus(200);
        assertThat(result).bodyJson()
                .extractingPath("$.stores").asArray().hasSize(2);
        assertThat(result).bodyJson()
                .extractingPath("$.stores[*].storeId").asArray()
                .containsExactlyInAnyOrder(inside1.getId().intValue(), inside2.getId().intValue());
    }

    @Test
    void 범위에_상점이_없으면_빈_배열을_반환한다() {
        // act
        MvcTestResult result = mockMvc.get().uri("/api/stores"
                        + "?minLat=35.0000000&maxLat=35.1000000"
                        + "&minLng=129.0000000&maxLng=129.1000000")
                .exchange();

        // assert
        assertThat(result).hasStatus(200);
        assertThat(result).bodyJson()
                .extractingPath("$.stores").asArray().isEmpty();
    }

    @Test
    void limit_파라미터로_결과_개수를_제한한다() {
        // arrange
        for (int i = 0; i < 5; i++) {
            storeRepository.save(new Store(
                    "상점" + i, "주소" + i,
                    new BigDecimal("37.5000000").add(new BigDecimal("0.000" + i + "000")),
                    new BigDecimal("127.0300000")
            ));
        }

        // act
        MvcTestResult result = mockMvc.get().uri("/api/stores"
                        + "?minLat=37.4900000&maxLat=37.5100000"
                        + "&minLng=127.0200000&maxLng=127.0400000"
                        + "&limit=2")
                .exchange();

        // assert
        assertThat(result).hasStatus(200);
        assertThat(result).bodyJson()
                .extractingPath("$.stores").asArray().hasSize(2);
    }

    @Test
    void minLat이_maxLat보다_크면_예외가_발생한다() {
        // act
        MvcTestResult result = mockMvc.get().uri("/api/stores"
                        + "?minLat=37.5100000&maxLat=37.4900000"
                        + "&minLng=127.0200000&maxLng=127.0500000")
                .exchange();

        // assert
        assertThat(result).hasStatus(400);
        assertThat(result).bodyJson()
                .extractingPath("$.message").asString().isEqualTo("잘못된 검색 범위입니다.");
    }

    @Test
    void 위도_차가_0_5를_초과하면_예외가_발생한다() {
        // act
        MvcTestResult result = mockMvc.get().uri("/api/stores"
                        + "?minLat=37.0000000&maxLat=37.6000000"
                        + "&minLng=127.0200000&maxLng=127.0500000")
                .exchange();

        // assert
        assertThat(result).hasStatus(400);
        assertThat(result).bodyJson()
                .extractingPath("$.message").asString().isEqualTo("잘못된 검색 범위입니다.");
    }

    @Test
    void limit이_500을_초과하면_예외가_발생한다() {
        // act
        MvcTestResult result = mockMvc.get().uri("/api/stores"
                        + "?minLat=37.4900000&maxLat=37.5100000"
                        + "&minLng=127.0200000&maxLng=127.0500000"
                        + "&limit=501")
                .exchange();

        // assert
        assertThat(result).hasStatus(400);
        assertThat(result).bodyJson()
                .extractingPath("$.message").asString().isEqualTo("잘못된 검색 범위입니다.");
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
