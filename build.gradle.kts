
//build.gradle.kts(PurchaseTracker) - 루트 프로젝트 빌드 스크립트
//용도: 전체 프로젝트에 적용되는 글로벌 설정 정의
//주요 역할:
//공통 플러그인 정의 (application 모듈에는 'apply false'로 직접 적용하지 않음)
//하위 모듈에서 공통으로 사용할 버전, 저장소, 글로벌 속성 등 정의
//모든 서브 프로젝트/모듈에 적용될 설정 관리

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.kotlin.kapt) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.hilt.android) apply false
}