package kr.jhp.purchtrac.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Material 3 기반의 타이포그래피 설정
// Typography 클래스는 앱 전체에서 일관된 텍스트 스타일을 제공합니다
val Typography = Typography(
    // 큰 제목 - 페이지 제목이나 중요한 섹션에 사용
    displayLarge = TextStyle(
        fontFamily = FontFamily.Default,  // 기본 시스템 폰트 사용
        fontWeight = FontWeight.Normal,   // 일반 굵기
        fontSize = 57.sp,                 // 글자 크기
        lineHeight = 64.sp,               // 줄 간격
        letterSpacing = (-0.25).sp        // 자간 (글자 간격)
    ),

    // 중간 크기 제목 - 섹션 제목이나 중요 콘텐츠에 사용
    displayMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 45.sp,
        lineHeight = 52.sp,
        letterSpacing = 0.sp
    ),

    // 작은 제목 - 부제목이나 강조 텍스트에 사용
    displaySmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 36.sp,
        lineHeight = 44.sp,
        letterSpacing = 0.sp
    ),

    // 큰 헤드라인 - 섹션 구분이나 중요 정보에 사용
    headlineLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 32.sp,
        lineHeight = 40.sp,
        letterSpacing = 0.sp
    ),

    // 중간 헤드라인
    headlineMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 28.sp,
        lineHeight = 36.sp,
        letterSpacing = 0.sp
    ),

    // 작은 헤드라인
    headlineSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 24.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.sp
    ),

    // 큰 제목 - 카드 제목이나 다이얼로그 제목에 사용
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),

    // 중간 제목 - 목록 항목 제목이나 작은 컴포넌트 제목에 사용
    titleMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,  // 중간 굵기
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.15.sp
    ),

    // 작은 제목 - 부가 정보나 레이블에 사용
    titleSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    ),

    // 큰 본문 텍스트 - 주요 콘텐츠 텍스트에 사용
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.15.sp
    ),

    // 중간 본문 텍스트 - 일반적인 텍스트 콘텐츠에 사용
    bodyMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp
    ),

    // 작은 본문 텍스트 - 부가 설명이나 작은 정보에 사용
    bodySmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.4.sp
    ),

    // 큰 레이블 - 버튼 텍스트나 강조된 작은 요소에 사용
    labelLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    ),

    // 중간 레이블 - 작은 버튼이나 칩 등에 사용
    labelMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    ),

    // 작은 레이블 - 매우 작은 텍스트 요소에 사용
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
)