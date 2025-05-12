// 필요한 Gradle 플러그인을 선언하는 블록
plugins {
    // 안드로이드 애플리케이션 빌드를 위한 기본 플러그인
    alias(libs.plugins.android.application)

    // Kotlin으로 안드로이드 개발을 하기 위한 플러그인
    alias(libs.plugins.kotlin.android)

    // Jetpack Compose를 사용하기 위한 Kotlin 컴파일러 플러그인
    alias(libs.plugins.kotlin.compose)

    // Kotlin 어노테이션 프로세싱을 위한 플러그인 (Room, Hilt 등에 필요)
//    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.ksp)

    // Kotlin 직렬화/역직렬화 라이브러리를 사용하기 위한 플러그인
    alias(libs.plugins.kotlin.serialization)

    // Hilt 의존성 주입 프레임워크 사용을 위한 플러그인
    alias(libs.plugins.hilt.android)
}

// 안드로이드 애플리케이션 설정 블록
android {
    // 앱의 패키지 이름 (리소스 접근 등에 사용)
    namespace = "kr.jhp.purchtrac"

    // 앱을 컴파일할 안드로이드 SDK 버전
    compileSdk = 35

    // 앱의 기본 설정 블록
    defaultConfig {
        // Google Play에서 앱을 식별하는 유일한 ID
        applicationId = "kr.jhp.purchtrac"

        // 앱이 실행되기 위한 최소 안드로이드 버전 (Android 10)
        minSdk = 29

        // 앱이 타겟팅하는 안드로이드 버전
        targetSdk = 35

        // 앱의 버전 코드 (업데이트시 증가)
        versionCode = 1

        // 사용자에게 표시되는 앱 버전 이름
        versionName = "1.0"

        // 안드로이드 계측 테스트를 실행하기 위한 테스트 러너
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    // 빌드 유형 설정 (debug, release 등)
    buildTypes {
        // 릴리스 버전 빌드 설정
        release {
            // 코드 축소 사용 여부 (false면 비활성화)
            isMinifyEnabled = false

            // ProGuard 규칙 파일 (코드 축소, 난독화 등에 사용)
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    // Java 컴파일러 옵션 설정
    compileOptions {
        // 소스 및 타겟 Java 버전 지정
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    // Kotlin 컴파일러 옵션 설정
    kotlinOptions {
        // Kotlin이 생성할 JVM 바이트코드 버전
        jvmTarget = "11"
    }

    // 빌드 기능 활성화 설정
    buildFeatures {
        // Jetpack Compose 사용 활성화
        compose = true
    }

    // Compose 컴파일러 옵션 설정
    composeOptions {
        // Compose 컴파일러 버전 지정 (Kotlin 버전과 호환 필요)
        kotlinCompilerExtensionVersion = "1.5.10"
    }
}

// 프로젝트 의존성 선언 블록
dependencies {
    // === 기존 의존성 ===

    // AndroidX 코어 라이브러리
    implementation(libs.androidx.core.ktx)

    // 수명 주기 관련 라이브러리
    implementation(libs.androidx.lifecycle.runtime.ktx)

    // Compose 활동 라이브러리
    implementation(libs.androidx.activity.compose)

    // Compose BOM (Bill of Materials) - 일관된 Compose 버전 제공
    implementation(platform(libs.androidx.compose.bom))

    // Compose UI 기본 라이브러리
    implementation(libs.androidx.ui)

    // Compose 그래픽 라이브러리
    implementation(libs.androidx.ui.graphics)

    // Compose UI 툴링 미리보기
    implementation(libs.androidx.ui.tooling.preview)

    // Material 3 디자인 시스템
    implementation(libs.androidx.material3)

    // === 추가 필요한 의존성 ===

    // 화면 탐색을 위한 Navigation Compose
    implementation(libs.androidx.navigation.compose)

    // Accompanist - Compose와 함께 사용하는 확장 라이브러리
    // 권한 관리 라이브러리
    implementation(libs.accompanist.permissions)
    // 스와이프 새로고침 기능
    implementation(libs.accompanist.swiperefresh)

    // Hilt - 의존성 주입 프레임워크
    implementation(libs.hilt.android)
    implementation(libs.androidx.room.common.jvm)
    ksp(libs.hilt.compiler) // 어노테이션 프로세싱을 위한 컴파일러
    implementation(libs.hilt.navigation.compose) // Compose와 Hilt 통합

    // Room - 로컬 데이터베이스
    implementation(libs.room.runtime) // Room 기본 라이브러리
    implementation(libs.room.ktx) // Kotlin 확장 기능
    ksp(libs.room.compiler) // 어노테이션 프로세싱을 위한 컴파일러

    // DataStore - 설정 및 데이터 저장
    implementation(libs.datastore.preferences)

    // Coroutines - 비동기 작업 처리
    implementation(libs.kotlinx.coroutines.android)

    // Kotlin Serialization - 데이터 직렬화/역직렬화
    implementation(libs.kotlinx.serialization.json)

    // Glance - 홈 화면 위젯
    implementation(libs.glance)
    implementation(libs.glance.appwidget)

    // WorkManager - 백그라운드 작업 및 예약 알림
    implementation(libs.work.runtime.ktx)

    // 보안 관련 라이브러리
    // 데이터 암호화를 위한 라이브러리
    implementation(libs.security.crypto)
    // 생체 인증 기능
    implementation(libs.biometric)

    implementation(libs.material.icons.extended)

    // === 테스트 의존성 ===

    // JUnit - 단위 테스트 프레임워크
    testImplementation(libs.junit)

    // AndroidX 테스트 JUnit 확장
    androidTestImplementation(libs.androidx.junit)

    // Espresso - UI 테스트 프레임워크
    androidTestImplementation(libs.androidx.espresso.core)

    // Compose 테스트를 위한 BOM
    androidTestImplementation(platform(libs.androidx.compose.bom))

    // Compose UI 테스트 라이브러리
    androidTestImplementation(libs.androidx.ui.test.junit4)

    // 디버그 빌드에만 포함되는 의존성
    // Compose UI 툴링 (Preview 등)
    debugImplementation(libs.androidx.ui.tooling)

    // Compose 테스트 매니페스트
    debugImplementation(libs.androidx.ui.test.manifest)
}