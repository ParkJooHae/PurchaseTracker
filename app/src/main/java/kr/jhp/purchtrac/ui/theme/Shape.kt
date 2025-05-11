package kr.jhp.purchtrac.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

// 앱 내 다양한 UI 요소의 모서리 둥근 정도를 일관되게 관리합니다
val Shapes = Shapes(
    // 작은 컴포넌트 (버튼, 칩 등)에 사용되는 모양
    small = RoundedCornerShape(4.dp),

    // 중간 크기 컴포넌트 (카드, 작은 대화상자 등)에 사용되는 모양
    medium = RoundedCornerShape(8.dp),

    // 큰 컴포넌트 (큰 카드, 대화상자 등)에 사용되는 모양
    large = RoundedCornerShape(12.dp)
)