# PurchaseTracker

점점 늘어나는 부모님의 계정을 쉽게 관리하기 위해 개발한 앱입니다. 
Jetpack Compose와 Clean Architecture를 활용하여 메모, 예약 구매, 계정 정보를 안전하게 관리합니다.

---

## 🎯 주요 기능

### 메모 관리 (Memo)
메모 생성, 조회, 수정, 삭제 (CRUD)
- 중요도 표시 및 필터링
- 제목/내용 검색
최근 수정 시간 기반 정렬

###  예약 구매 관리 (Product)
- 제품 예약 정보 저장
- 상태 관리: PLANNED → PURCHASED → CANCELED
- 발매 예정일 기반 정렬
- 구매 알림 설정 및 토글
- 가격, 사이트명, 구매처 정보 관리
제품명/사이트명 검색 및 상태별 필터링

### 계정 정보 관리 (Account)
- 웹사이트 계정 정보 안전 저장
- 비밀번호 암호화 저장 (EncryptedSharedPreferences)
- 사이트명, 사용자명, URL, 노트 저장
- 사이트명/사용자명/URL 다중 검색

###  다중 사용자 지원
- 사용자 프로필 지원: 나, 어머니, 아버지
- 사용자별 데이터 완전 분리

---

## 🏗️ 아키텍처

### Clean Architecture + MVI 패턴
```
┌─────────────────────────────────────────┐
│           UI Layer (Compose)             │
│  (Screens, ViewModels, States/Events)   │
└────────────────┬────────────────────────┘
                 │
┌────────────────▼────────────────────────┐
│         Domain Layer (Use Cases)         │
│  (Business Logic, Repository Interface)  │
└────────────────┬────────────────────────┘
                 │
┌────────────────▼────────────────────────┐
│         Data Layer (Repository)          │
│  (Room DB, EncryptedPreferences, DAOs)  │
└─────────────────────────────────────────┘
```

**핵심 패턴:**
- **MVI**: Intent → ViewModel → State/Event 단방향 흐름
- **Repository Pattern**: 데이터 소스 추상화
- **의존성 역전**: 상위 계층이 하위 계층의 인터페이스에만 의존
- **Flow/StateFlow**: 반응형 상태 관리

---

## 📱 기술 스택

### UI/Compose (2024.09.00)
- **Jetpack Compose**: 선언적 UI 구현
- **Material 3**: Material Design 시스템
- **Compose Navigation**: 화면 전환 라우팅
- **Glance API**: 홈 화면 위젯 (예정)

### 아키텍처 및 DI
- **Hilt (2.56.2)**: 의존성 주입
- **MVI 패턴**: 상태 관리

### 데이터 레이어
- **Room (2.7.1)**: 로컬 SQLite 데이터베이스
- **EncryptedSharedPreferences (1.0.0)**: 민감 데이터 암호화
- **DataStore (1.0.0)**: 앱 설정 저장
- **Kotlin Serialization (1.6.0)**: JSON 직렬화

### 비동기 처리
- **Kotlin Coroutines (1.7.3)**: 비동기 작업
- **Flow/StateFlow**: 반응형 데이터 스트림
- **WorkManager (2.9.0)**: 백그라운드 작업 예약 (예정)

### 기타
- **Biometric (1.1.0)**: 생체 인증 (예정)
- **Security Crypto (1.0.0)**: 암호화 저장

---

## 🧪 테스트 커버리지

### Unit Tests (단위 테스트)
- **ErrorHandler Tests**: 에러 핸들링 및 사용자 메시지 매핑
- **MemoRepository Tests**: 메모 모델 및 로직
- **ProductRepository Tests**: 상품 상태 전환 및 필터링
- **AccountRepository Tests**: 계정 관리 및 검색

**테스트 항목:**
- 예외 처리 및 복구 가능성 판단
- 상태 전환 로직
- 필터링 및 검색 기능
- 데이터 모델 구조

### Integration Tests (통합 테스트)
- **MemoViewModelIntegrationTest**: 메모 로딩, 필터링, 검색
- **ProductViewModelIntegrationTest**: 상태 전환, 알림, 필터링
- **AccountViewModelIntegrationTest**: 생성, 검색, 업데이트, 삭제

**테스트 항목:**
- ViewModel ↔ Repository 통합
- 상태 관리 일관성
- UI 상태 동기화

### 테스트 실행
```bash
# Unit Tests
./gradlew test

# Integration Tests
./gradlew connectedAndroidTest

# 전체 테스트
./gradlew build
```

---

## 🛡️ 에러 처리 전략

### Custom Exception 구조
```kotlin
sealed class PurchaseTrackerException : Exception {
    data class DatabaseException(val exception: Throwable)
    data class EntityNotFoundException(val entityName: String, val entityId: Long)
    data class ValidationException(val fieldName: String)
    data class ConflictException(val message: String)
    data class PermissionException()
    data class UnknownException(val exception: Throwable)
}
```

### ErrorHandler 유틸리티
```kotlin
// 사용자 친화적 메시지
ErrorHandler.getUserMessage(exception)
// → "데이터베이스 오류가 발생했습니다"

// 개발자용 상세 메시지
ErrorHandler.getDetailedMessage(exception)
// → "DatabaseException: Connection failed\nCause: ..."

// 복구 가능 여부 판단
ErrorHandler.isRecoverable(exception)
// → true/false
```

### ViewModel에서의 통합
```kotlin
try {
    deleteMemoUseCase(memoId)
    _event.emit(MemoEvent.ShowToast("메모가 삭제되었습니다"))
} catch (e: Exception) {
    val userMessage = ErrorHandler.getUserMessage(e)
    _event.emit(MemoEvent.ShowToast(userMessage))
}
```

---

## 📊 데이터베이스 구조

### ER Diagram
```
[users]
  ├─ id (PK)
  ├─ name: String
  └─ type: UserType

[accounts] ─→ users.id
  ├─ id (PK)
  ├─ userId (FK)
  ├─ siteName, username
  ├─ password (encrypted)
  └─ notes

[products] ─→ users.id
  ├─ id (PK)
  ├─ userId (FK)
  ├─ name, price
  ├─ releaseDate (indexed)
  ├─ status: ProductStatus
  └─ reminderEnabled

[memos] ─→ users.id
  ├─ id (PK)
  ├─ userId (FK)
  ├─ title, content
  └─ isImportant
```

**특징:**
- CASCADE 삭제 정책 (사용자 삭제 시 관련 데이터 자동 삭제)
- userId, releaseDate 인덱싱
- TypeConverter로 Enum 저장

---

## 🚀 프로젝트 구조

```
app/src/main/java/kr/jhp/purchtrac/
├── domain/                          # 비즈니스 로직
│   ├── error/                       # 에러 핸들링
│   │   ├── PurchaseTrackerException.kt
│   │   └── ErrorHandler.kt
│   ├── model/                       # 도메인 모델
│   ├── repository/                  # 저장소 인터페이스
│   └── usecase/                     # 비즈니스 유스케이스
│
├── data/                            # 데이터 계층
│   └── local/
│       ├── database/                # Room Database
│       ├── entity/                  # DB 엔티티
│       └── dao/                     # Data Access Objects
│
├── ui/                              # UI 계층
│   ├── screens/                     # 화면 컴포저블
│   ├── state/                       # MVI 상태
│   ├── components/                  # 재사용 컴포넌트
│   ├── navigation/                  # 네비게이션
│   └── theme/                       # 테마
│
├── di/                              # 의존성 주입
└── MainActivity.kt
```

---

