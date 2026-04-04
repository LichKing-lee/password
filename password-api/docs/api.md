# API 스펙

## 상점 등록

상점을 등록한다. 이미 등록된 상점(naverPlaceId 기준)이면 기존 상점을 반환한다.

### Request

```
POST /api/stores
Content-Type: application/json
```

#### Body

| 필드         | 타입       | 필수 | 설명               |
|--------------|------------|------|--------------------|
| naverPlaceId | String     | O    | 네이버 플레이스 ID |
| name         | String     | O    | 상점명             |
| address      | String     | O    | 주소               |
| latitude     | BigDecimal | O    | 위도               |
| longitude    | BigDecimal | O    | 경도               |

#### 예시

```json
{
  "naverPlaceId": "naver-place-789",
  "name": "이디야커피 선릉점",
  "address": "서울시 강남구 선릉로 789",
  "latitude": 37.5045000,
  "longitude": 127.0490000
}
```

### Response

#### 성공 (201 Created)

| 필드         | 타입       | 설명               |
|--------------|------------|--------------------|
| storeId      | Long       | 상점 ID            |
| naverPlaceId | String     | 네이버 플레이스 ID |
| name         | String     | 상점명             |
| address      | String     | 주소               |
| latitude     | BigDecimal | 위도               |
| longitude    | BigDecimal | 경도               |

```json
{
  "storeId": 1,
  "naverPlaceId": "naver-place-789",
  "name": "이디야커피 선릉점",
  "address": "서울시 강남구 선릉로 789",
  "latitude": 37.5045000,
  "longitude": 127.0490000
}
```

---

## 와이파이 등록

상점의 와이파이를 등록한다. 비밀번호가 없는 개방형 와이파이도 등록 가능하다.
상점은 사전에 등록되어 있어야 한다.

### Request

```
POST /api/wifis
Content-Type: application/json
```

#### Body

| 필드           | 타입       | 필수 | 설명                                |
|----------------|------------|------|-------------------------------------|
| storeId        | Long       | O    | 상점 ID                             |
| ssid           | String     | O    | 와이파이 SSID                       |
| open           | boolean    | O    | 개방형 여부 (true이면 password 무시)|
| password       | String     | X    | 와이파이 비밀번호 (open=true이면 무시됨) |

#### 예시

비밀번호가 있는 경우:

```json
{
  "storeId": 1,
  "ssid": "EDIYA_5G",
  "open": false,
  "password": "ediya1234"
}
```

개방형 와이파이인 경우:

```json
{
  "storeId": 2,
  "ssid": "Starbucks_Free",
  "open": true
}
```

### Response

#### 성공 (201 Created)

| 필드           | 타입    | 설명                              |
|----------------|---------|-----------------------------------|
| wifiId         | Long    | 와이파이 ID                       |
| storeId        | Long    | 상점 ID                           |
| ssid           | String  | 와이파이 SSID                     |
| password       | String  | 와이파이 비밀번호 (개방형이면 null)|
| open           | boolean | 개방형 여부                       |
| createdAt      | String  | 등록일시 (ISO 8601)               |

```json
{
  "wifiId": 1,
  "storeId": 1,
  "ssid": "EDIYA_5G",
  "password": "ediya1234",
  "open": false,
  "createdAt": "2026-03-29T11:00:00"
}
```

---

## 와이파이 조회

상점의 와이파이 목록을 조회한다.

### Request

```
GET /api/stores/{storeId}/wifis
```

#### Path Parameters

| 필드    | 타입 | 필수 | 설명    |
|---------|------|------|---------|
| storeId | Long | O    | 상점 ID |

#### 예시

```
GET /api/stores/1/wifis
```

### Response

#### 성공 (200 OK)

| 필드                  | 타입    | 설명                              |
|-----------------------|---------|-----------------------------------|
| wifis                 | Array   | 와이파이 목록                     |
| wifis[].wifiId        | Long    | 와이파이 ID                       |
| wifis[].storeId       | Long    | 상점 ID                           |
| wifis[].ssid          | String  | 와이파이 SSID                     |
| wifis[].password      | String  | 와이파이 비밀번호 (개방형이면 null)|
| wifis[].open          | boolean | 개방형 여부                       |
| wifis[].createdAt     | String  | 등록일시 (ISO 8601)               |

```json
{
  "wifis": [
    {
      "wifiId": 1,
      "storeId": 1,
      "ssid": "EDIYA_5G",
      "password": "ediya1234",
      "open": false,
      "createdAt": "2026-03-29T11:00:00"
    },
    {
      "wifiId": 2,
      "storeId": 1,
      "ssid": "EDIYA_Free",
      "password": null,
      "open": true,
      "createdAt": "2026-03-29T11:05:00"
    }
  ]
}
```
