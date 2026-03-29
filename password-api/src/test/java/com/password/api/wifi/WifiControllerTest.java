package com.password.api.wifi;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WifiController.class)
class WifiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void 와이파이를_등록한다() throws Exception {
        // arrange
        String request = """
                {
                  "store": {
                    "naverPlaceId": "naver-place-789",
                    "name": "이디야커피 선릉점",
                    "address": "서울시 강남구 선릉로 789",
                    "latitude": 37.5045000,
                    "longitude": 127.0490000
                  },
                  "ssid": "EDIYA_5G",
                  "open": false,
                  "password": "ediya1234"
                }
                """;

        // act & assert
        mockMvc.perform(post("/api/wifis")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.wifiId").value(1))
                .andExpect(jsonPath("$.store.storeId").value(1))
                .andExpect(jsonPath("$.store.name").value("이디야커피 선릉점"))
                .andExpect(jsonPath("$.ssid").value("EDIYA_5G"))
                .andExpect(jsonPath("$.password").value("ediya1234"))
                .andExpect(jsonPath("$.open").value(false))
                .andExpect(jsonPath("$.createdAt").isNotEmpty());
    }

    @Test
    void 개방형_와이파이를_등록한다() throws Exception {
        // arrange
        String request = """
                {
                  "store": {
                    "naverPlaceId": "naver-place-456",
                    "name": "스타벅스 역삼점",
                    "address": "서울시 강남구 역삼로 456",
                    "latitude": 37.5000000,
                    "longitude": 127.0360000
                  },
                  "ssid": "Starbucks_Free",
                  "open": true
                }
                """;

        // act & assert
        mockMvc.perform(post("/api/wifis")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.store.name").value("스타벅스 역삼점"))
                .andExpect(jsonPath("$.ssid").value("Starbucks_Free"))
                .andExpect(jsonPath("$.password").doesNotExist())
                .andExpect(jsonPath("$.open").value(true))
                .andExpect(jsonPath("$.createdAt").isNotEmpty());
    }
}
