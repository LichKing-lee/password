# 프로젝트 가이드
## 프로젝트 개요
- 지도 API 를 사용하여 상점들의 와이파이 패스워드를 공유하는 웹/앱 애플리케이션의 백엔드 개발 프로젝트
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
- 각 모듈의 루트 패키지는 com.password.{모듈명} 으로 시작한다
- 가장 초기에는 rest api 을 응답하는 api 모듈만 존재한다
- JPA 관련 repository, entity 등은 domain 모듈에 위치한다
- java bean 관련 메서드는 lombok 을 사용한다
- spring 설정 파일은 yml 형식으로 작성한다

## 프로젝트 테스트 룰
- 작성되는 코드는 모두 테스트 코드가 작성되어야 한다
- 테스트 코드는 JUnit 5 를 사용하여 실행되며, Assertion 은 AssertJ 를 사용한다
- spring context 를 사용하는 통합 테스트는 controller, repository 에 대해서만 작성한다
- service 는 테스트 코드를 작성하지 않는다
- mockito 와 같은 mock 라이브러리를 사용하지 않는다
- 테스트 셋업은 @BeforeEach 애노테이션을 사용하지 않고 테스트 코드 내에서 직접 작성한다
- 테스트 메서드만으로 테스트의 모든것을 확인하기 위함이다
- 테스트 메서드 이름은 테스트의 목적이 드러나도록 한국어로 작성한다
- 테스트 메서드 구현은 arrange-act-assert 패턴을 따른다
- 코드 변경 후 전체 테스트를 실행하여 모든 테스트가 통과하는지 확인한다

## 프로젝트 개발 룰
- JPA entity 들의 모든 연관관계는 무조건 LAZY 로 설정한다
- 다만 연관관계가 걸려있는 entity 조회시 JPQL 을 사용하여 fetch join 을 통해 조회한다