# WiFi API

## 와이파이 등록

상점의 와이파이를 등록한다. 비밀번호가 없는 개방형 와이파이도 등록 가능하다.

### Request

```
POST /api/wifis
Content-Type: application/json
```

#### Body

| 필드 | 타입 | 필수 | 설명 |
|------|------|------|------|
| naverPlaceId | String | O | 네이버 플레이스 ID |
| storeName | String | O | 상점명 |
| address | String | O | 주소 |
| latitude | BigDecimal | O | 위도 |
| longitude | BigDecimal | O | 경도 |
| ssid | String | O | 와이파이 SSID |
| open | boolean | O | 개방형 여부 (true이면 password 무시) |
| password | String | X | 와이파이 비밀번호 (open=true이면 무시됨) |

#### 예시

비밀번호가 있는 경우:

```json
{
  "naverPlaceId": "naver-place-789",
  "storeName": "이디야커피 선릉점",
  "address": "서울시 강남구 선릉로 789",
  "latitude": 37.5045000,
  "longitude": 127.0490000,
  "ssid": "EDIYA_5G",
  "open": false,
  "password": "ediya1234"
}
```

개방형 와이파이인 경우:

```json
{
  "naverPlaceId": "naver-place-456",
  "storeName": "스타벅스 역삼점",
  "address": "서울시 강남구 역삼로 456",
  "latitude": 37.5000000,
  "longitude": 127.0360000,
  "ssid": "Starbucks_Free",
  "open": true
}
```

### Response

#### 성공 (201 Created)

| 필드 | 타입 | 설명 |
|------|------|------|
| wifiId | Long | 와이파이 ID |
| storeId | Long | 상점 ID |
| storeName | String | 상점명 |
| ssid | String | 와이파이 SSID |
| password | String | 와이파이 비밀번호 (개방형이면 null) |
| open | boolean | 개방형 여부 |
| createdAt | String | 등록일시 (ISO 8601) |

```json
{
  "wifiId": 1,
  "storeId": 1,
  "storeName": "이디야커피 선릉점",
  "ssid": "EDIYA_5G",
  "password": "ediya1234",
  "open": false,
  "createdAt": "2026-03-29T11:00:00"
}
```
