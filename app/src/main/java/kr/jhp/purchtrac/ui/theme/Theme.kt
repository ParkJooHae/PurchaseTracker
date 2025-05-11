package kr.jhp.purchtrac.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// 라이트 모드 색상 스키마 정의
// Material 3 컴포넌트에 적용될 다양한 색상 속성을 지정합니다
private val LightColorScheme = lightColorScheme(
    // 주요 색상 - 앱의 브랜드 색상, 주요 버튼 등에 사용
    primary = LightSkyBlue,
    // 주요 색상 위의 텍스트/아이콘 색상
    onPrimary = Black,
    // 주요 색상이 포함된 컨테이너 (버튼, 칩 등)
    primaryContainer = LightBlueVariant,
    // 주요 색상 컨테이너 위의 텍스트/아이콘 색상
    onPrimaryContainer = Black,

    // 보조 색상 - 강조나 액션 요소에 사용
    secondary = BlueAccent,
    // 보조 색상 위의 텍스트/아이콘
    onSecondary = White,
    // 보조 색상이 포함된 컨테이너
    secondaryContainer = LightBlueVariant,
    // 보조 색상 컨테이너 위의 텍스트/아이콘
    onSecondaryContainer = Black,

    // 3차 색상 - 주요/보조 색상과 보완적으로 사용
    tertiary = BlueAccent,
    // 3차 색상 위의 텍스트/아이콘
    onTertiary = White,

    // 배경 색상 - 화면 배경에 사용
    background = White,
    // 배경 위의 텍스트/아이콘
    onBackground = Black,

    // 표면 색상 - 카드, 시트, 대화상자 등에 사용
    surface = LightSurface,
    // 표면 위의 텍스트/아이콘
    onSurface = Black,

    // 오류 색상 - 에러 표시, 경고 등에 사용
    error = ErrorRed,
    // 오류 색상 위의 텍스트/아이콘
    onError = White
)

// 다크 모드 색상 스키마 정의
private val DarkColorScheme = darkColorScheme(
    // 주요 색상 관련
    primary = DarkBlue,
    onPrimary = White,
    primaryContainer = DarkBlueVariant,
    onPrimaryContainer = White,

    // 보조 색상 관련
    secondary = BlueAccent,
    onSecondary = Black,
    secondaryContainer = DarkBlueVariant,
    onSecondaryContainer = White,

    // 3차 색상 관련
    tertiary = BlueAccent,
    onTertiary = Black,

    // 배경 색상 관련
    background = Black,
    onBackground = White,

    // 표면 색상 관련
    surface = DarkSurface,
    onSurface = White,

    // 오류 색상 관련
    error = ErrorRed,
    onError = White
)

// 앱의 테마를 정의하는 Composable 함수
@Composable
fun PurchaseTrackerTheme(
    // 다크 테마 사용 여부 (기본값은 시스템 설정에 따름)
    darkTheme: Boolean = isSystemInDarkTheme(),
    // 다이나믹 컬러 사용 여부 (Android 12+ 전용 기능)
    dynamicColor: Boolean = false,
    // 테마가 적용될 컨텐츠
    content: @Composable () -> Unit
) {
    // 적용할 색상 스키마 결정
    val colorScheme = when {
        // 다이나믹 컬러가 활성화되고 Android 12 이상인 경우
        // 사용자 기기의 배경화면 등에서 추출한 색상을 사용
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        // 다크 테마인 경우 다크 색상 스키마 사용
        darkTheme -> DarkColorScheme
        // 그 외에는 라이트 색상 스키마 사용
        else -> LightColorScheme
    }

    // 시스템 UI(상태 바 등)의 색상 설정
    val view = LocalView.current
    // 미리보기에서는 실행하지 않음
    if (!view.isInEditMode) {
        // SideEffect를 사용해 컴포지션이 완료된 후 UI 업데이트
        SideEffect {
            val window = (view.context as Activity).window
            // 상태 바 색상을 테마의 주요 색상으로 설정
            window.statusBarColor = colorScheme.primary.toArgb()
            // 상태 바의 텍스트/아이콘이 배경과 잘 대비되도록 설정
            // 다크 테마에서는 밝은 아이콘, 라이트 테마에서는 어두운 아이콘
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    // MaterialTheme을 사용해 정의된 테마 속성들을 컨텐츠에 적용
    MaterialTheme(
        // 색상 스키마
        colorScheme = colorScheme,
        // 타이포그래피
        typography = Typography,
        // 모양
        shapes = Shapes,
        // 실제 UI 컨텐츠
        content = content
    )
}