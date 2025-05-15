# PurchaseTracker

UI 레이어
* Jetpack Compose: 선언적 UI 구현
* Material 3: 디자인 시스템 적용
* Glance API: 홈 화면 위젯 구현
* Compose Navigation: 화면 전환 관리
* Accompanist 라이브러리: 권한 관리, SwipeRefresh 등 추가 기능

아키텍처 및 상태 관리
* MVI (Model-View-Intent) 패턴:
    * Intent: 사용자 액션 정의
    * Model: 비즈니스 로직 및 데이터 처리
    * ViewState: UI 상태 관리
    * Side Effect: 일회성 이벤트 처리
* Repository 패턴: 데이터 소스 추상화

데이터 레이어
* Room Database: 로컬 데이터 저장
* EncryptedSharedPreferences: 보안이 필요한 계정 정보 암호화 저장
* DataStore: 앱 설정 저장
* Kotlin Serialization: 데이터 직렬화/역직렬화

비동기 처리
* Kotlin Coroutines: 비동기 작업 처리
* Flow: 반응형 데이터 스트림 처리
* StateFlow/SharedFlow: 상태 관리 및 이벤트 전달

의존성 주입
* Hilt: 의존성 주입 프레임워크

알림 및 백그라운드 작업
* WorkManager: 예약 발매일 체크 및 알림 예약
* NotificationManager: 알림 표시
