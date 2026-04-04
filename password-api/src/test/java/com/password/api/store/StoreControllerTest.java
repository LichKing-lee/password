package com.password.api.store;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.assertj.MvcTestResult;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class StoreControllerTest {

    @Autowired
    private MockMvcTester mockMvc;

    @Test
    void 상점을_등록한다() {
        // arrange
        String request = """
                {
                  "naverPlaceId": "naver-place-789",
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
                .extractingPath("$.naverPlaceId").asString().isEqualTo("naver-place-789");
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
                  "naverPlaceId": "naver-place-duplicate",
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
}
