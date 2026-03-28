# 프로젝트 가이드
## 프로젝트 개요
- 지도 API 를 사용하여 상점들의 와이파이 패스워드와 화장실 비밀번호를 공유하는 웹/앱 애플리케이션의 백엔드 개발 프로젝트
- 사용자들이 직접 정보를 입력할 수 있으며, 국내 서비스로 한정한다
- 지도 API 는 네이버지도를 사용한다

## 🛠 기술 스택 (Tech Stack)
- JDK 25
- Spring Boot 4.0.5
- Spring Boot Web
- Spring Data JPA
- Gradle (Kotlin DSL)

## 프로젝트 세팅 룰
- 형상관리로 git 사용
- 초기 프로젝트 세팅은 spring initializr 를 사용하여 생성
- spring bom 을 통해 버전관리가 되는 라이브러리는 버전을 명시하지 않음
- gradle 기반 multi module 프로젝트
- 각 모듈은 password-* 네이밍 규칙을 가진다
- 가장 초기에는 rest api 을 응답하는 api 모듈만 존재한다
- JPA 관련 repository, entity 등은 domain 모듈에 위치한다